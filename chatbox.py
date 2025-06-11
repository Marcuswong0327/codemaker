import streamlit as st
import joblib
import numpy as np
import requests

# Load model and scalers
# Ensure 'stacking_classifier_model.pkl' and 'scalers.pkl' are in the same directory
try:
    model = joblib.load('stacking_classifier_model.pkl')
    scalers = joblib.load('scalers.pkl')
except Exception as e:
    st.error(f"❌ Error loading model or scalers: {e}")
    st.stop() # Stop the app if model/scalers cannot be loaded

# Function to predict stroke risk
def predict_stroke(features):
    # Features that need to be scaled using pre-trained scalers
    features_to_scale = ['bmi', 'age', 'avg_glucose_level']
    scaled_features = []

    # Add non-scaled categorical features first in the order expected by the model
    scaled_features.extend([features[key] for key in [
        'gender', 'hypertension', 'heart_disease', 'ever_married',
        'work_type', 'Residence_type', 'smoking_status'
    ]])

    # Scale numerical features and append them
    for key in features_to_scale:
        scaler = scalers[key]
        # Reshape for scaler.transform and extract the single scaled value
        scaled_value = scaler.transform(np.array([[features[key]]]))[0][0]
        scaled_features.append(scaled_value)

    # Convert the list of features to a numpy array for prediction
    # model.predict expects a 2D array, even for a single sample
    prediction = model.predict([scaled_features])[0]
    # Get probability of stroke (class 1)
    probability = model.predict_proba([scaled_features])[0][1]
    return prediction, probability

# Function to interact with Gemini via OpenRouter API
def ask_gemini_about_result(question, features):
    # Retrieve API key from Streamlit secrets
    api_key = st.secrets.get("openrouter_api_key", None)

    if not api_key:
        return "❌ API key not found. Please check your Streamlit secrets (`.streamlit/secrets.toml`)."

    # Headers required for OpenRouter API
    headers = {
        "Authorization": f"Bearer {api_key}",
        # Referer and X-Title are good practice for OpenRouter for tracking/billing
        "HTTP-Referer": "https://stroke-risk-prediction-grp-1.streamlit.app/",
        "X-Title": "Stroke Risk Chat AI"
    }

    # The exact model ID for Gemini 2.5 Flash Preview 05-20 from OpenRouter
    model_id = "google/gemini-2.5-flash-preview-05-20"

    # Format user's health information into a readable context for the AI
    context = "\n".join([f"{k.replace('_', ' ').title()}: {v}" for k, v in features.items()])

    # Construct the full prompt for the AI
    prompt = f"""You are a health assistant AI helping users understand their stroke risk.
Here is the user’s health information:
{context}

Now answer this question: {question}
Please explain in simple and friendly terms."""

    # Payload for the API request
    data = {
        "model": model_id,
        "messages": [
            {"role": "user", "content": prompt}
        ]
    }

    try:
        # Make the POST request to OpenRouter API with a timeout
        response = requests.post(
            "https://openrouter.ai/api/v1/chat/completions",
            headers=headers,
            json=data,
            timeout=30 # Increased timeout for potentially longer AI responses
        )

        # Raise an HTTPError for bad responses (4xx or 5xx)
        response.raise_for_status()

        # Extract the AI's reply from the JSON response
        reply = response.json()['choices'][0]['message']['content']

        # Debugging output to show HTTP status and raw response
        st.write(f"📡 HTTP status: {response.status_code}")
        st.code(response.text, language="json")
        return reply

    except requests.exceptions.Timeout:
        return "⏰ The request to Gemini timed out after 30 seconds. Please try again later or with a simpler question."
    except requests.exceptions.ConnectionError:
        return "🌐 Network error! Please check your internet connection and try again."
    except requests.exceptions.HTTPError as e:
        # Provide detailed HTTP error with response text for debugging
        return f"❌ HTTP error from Gemini: {e}\nResponse: {response.text}"
    except Exception as e:
        # Catch any other unexpected errors
        return f"😵 Unexpected error during Gemini API call: {e}"

# Function to provide basic health advice based on input values
def advice_on_values(age, bmi, glucose):
    advice = []
    if age < 0 or age > 120:
        advice.append("❗ Invalid age input, please enter between 0 and 120.")
    if bmi < 10 or bmi > 60:
        advice.append("❗ BMI input seems off, please double-check. A typical healthy BMI range is 18.5 to 24.9.")
    elif bmi < 18.5:
        advice.append("⚠️ BMI is low, consider improving nutrition and gaining healthy weight if advised by a professional.")
    elif bmi > 24.9:
        advice.append("⚠️ BMI is elevated, consider healthy eating and exercise to manage weight.")
    if glucose < 30 or glucose > 500:
        advice.append("❗ Blood glucose value abnormal, please verify your input. Normal fasting blood glucose is usually below 100 mg/dL.")
    return advice

# Main Streamlit UI function
def main():
    st.set_page_config(page_title="Stroke Prediction App", layout="centered")

    st.title("🩺 Stroke Prediction App")
    st.write("Fill in your details below to find out your risk of stroke.")

    # --- User Input Section ---
    st.header("👤 Your Health Details")

    col1, col2 = st.columns(2)
    with col1:
        gender = st.selectbox("Gender", ['Male', 'Female'])
        hypertension = st.selectbox("Hypertension", [0, 1], format_func=lambda x: 'Yes' if x else 'No', help="Do you have high blood pressure?")
        heart_disease = st.selectbox("Heart Disease", [0, 1], format_func=lambda x: 'Yes' if x else 'No', help="Do you have any heart conditions?")
    with col2:
        ever_married = st.selectbox("Ever Married?", [0, 1], format_func=lambda x: 'Yes' if x else 'No', help="Have you ever been married?")
        
        work_type_dict = {
            0: "children",
            1: "Govt_job",
            2: "Private",
            3: "Self-employed",
            4: "Never_worked"
        }
        work_type_display = [f"{work_type_dict[k]} ({k})" for k in work_type_dict.keys()]
        work_type_selected_display = st.selectbox("Work Type?", options=work_type_display, format_func=lambda x: x.split(' (')[0], help="Your current work type.")
        work_type = int(work_type_selected_display.split('(')[1].strip(')')) # Extract numeric key


        Residence = st.selectbox("Where you live?", [0, 1], format_func=lambda x: 'Urban' if x == 1 else 'Rural', help="Urban or Rural residence?")
    
    smoking_status_dict = {
        0: "never smoked",
        1: "formerly smoked",
        2: "smokes"
    }
    # Create a mapping for display values to their numerical keys
    smoking_status_options = {
        'never smoked': 0,
        'formerly smoked': 1,
        'smokes': 2
    }
    # Display the user-friendly options, but store the numerical value
    smoking_status_display = st.selectbox(
        "Smoking Status",
        options=list(smoking_status_options.keys()),
        help="Your current smoking status."
    )
    smoking_status = smoking_status_options[smoking_status_display] # Get the numerical value


    st.subheader("📊 Numerical Measures")
    bmi = st.number_input("BMI (Body Mass Index)", min_value=0.0, max_value=100.0, value=25.0, step=0.1, help="Body Mass Index (e.g., 25.0 for average)")
    age = st.number_input("Age", min_value=0, max_value=120, value=40, step=1, help="Your age in years.")
    glucose = st.number_input("Average Glucose Level", min_value=0.0, max_value=300.0, value=90.0, step=0.1, help="Your average blood glucose level (e.g., 90.0 mg/dL)")

    # Convert gender to numeric (assuming Female=1, Male=0 based on typical encoding)
    gender_numeric = 1 if gender == 'Female' else 0

    # Store all features in a dictionary
    features = {
        'gender': gender_numeric,
        'hypertension': hypertension,
        'heart_disease': heart_disease,
        'ever_married': ever_married,
        'work_type': work_type,
        'Residence_type': Residence,
        'smoking_status': smoking_status,
        'bmi': bmi,
        'age': age,
        'avg_glucose_level': glucose
    }

    st.markdown("---") # Separator

    # Pred
