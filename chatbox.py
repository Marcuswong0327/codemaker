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
    # Define the exact order of features your model expects based on your training data
    # This MUST match the order of columns in your original training dataset (e.g., from your CSV)
    # Based on the screenshot you provided, this appears to be the column order.
    expected_feature_order = [
        'gender',
        'age',             # Numerical, will be scaled
        'hypertension',
        'heart_disease',
        'ever_married',
        'work_type',
        'Residence_type',
        'avg_glucose_level', # Numerical, will be scaled
        'bmi',             # Numerical, will be scaled
        'smoking_status'
    ]

    # List of features that specifically need scaling
    features_to_scale = ['age', 'avg_glucose_level', 'bmi']

    # This list will hold the final, correctly ordered and processed features
    processed_features = []

    # Iterate through the expected order and process each feature
    for feature_name in expected_feature_order:
        value = features[feature_name] # Get the raw value from the 'features' dictionary

        if feature_name in features_to_scale:
            # If the feature needs scaling, apply the corresponding scaler
            scaler = scalers[feature_name]
            # Scaler expects a 2D array, even for a single value: [[value]]
            scaled_value = scaler.transform(np.array([[value]]))[0][0]
            processed_features.append(scaled_value)
        else:
            # If the feature does not need scaling (categorical), append its raw value
            processed_features.append(value)

    # Make the prediction with the correctly ordered and scaled features
    prediction = model.predict([processed_features])[0]
    # Get probability of stroke (class 1)
    probability = model.predict_proba([processed_features])[0][1]
    return prediction, probability

# Gemini chat function (remains unchanged as the issue was feature ordering)
def ask_gemini_about_result(question, features):
    api_key = st.secrets.get("openrouter_api_key", None)

    if not api_key:
        return "❌ API key not found. Please check your Streamlit secrets (`.streamlit/secrets.toml`)."

    headers = {
        "Authorization": f"Bearer {api_key}",
        "HTTP-Referer": "https://stroke-risk-prediction-grp-1.streamlit.app/",
        "X-Title": "Stroke Risk Chat AI"
    }

    model_id = "google/gemini-2.5-flash-preview-05-20" # Confirmed from your screenshot
    context = "\n".join([f"{k.replace('_', ' ').title()}: {v}" for k, v in features.items()])

    prompt = f"""You are a health assistant AI helping users understand their stroke risk.
Here is the user’s health information:
{context}

Now answer this question: {question}
Please explain in simple and friendly terms."""

    data = {
        "model": model_id,
        "messages": [
            {"role": "user", "content": prompt}
        ]
    }

    try:
        response = requests.post(
            "https://openrouter.ai/api/v1/chat/completions",
            headers=headers,
            json=data,
            timeout=30 # Increased timeout for potentially longer AI responses
        )

        response.raise_for_status()

        reply = response.json()['choices'][0]['message']['content']

        st.write(f"📡 HTTP status: {response.status_code}")
        st.code(response.text, language="json")
        return reply

    except requests.exceptions.Timeout:
        return "⏰ The request to Gemini timed out after 30 seconds. Please try again later or with a simpler question."
    except requests.exceptions.ConnectionError:
        return "🌐 Network error! Please check your internet connection and try again."
    except requests.exceptions.HTTPError as e:
        return f"❌ HTTP error from Gemini: {e}\nResponse: {response.text}"
    except Exception as e:
        return f"😵 Unexpected error during Gemini API call: {e}"

# Basic health advice
def advice_on_values(age, bmi, glucose):
    advice = []
    if age < 0 or age > 120:
        advice.append("❗ Invalid age input, please enter between 0 and 120.")
    if bmi < 10 or bmi > 60:
        advice.append("❗ BMI input seems off, please double-check. A typical healthy BMI range is 18.5 to 24.9.")
    elif bmi < 18.5:
        advice.append("⚠️ BMI is low, consider improving nutrition and gaining healthy weight if advised by a professional.")
    elif bmi > 24.9: # Added advice for overweight/obese BMI
        advice.append("⚠️ BMI is elevated, consider healthy eating and exercise to manage weight.")
    if glucose < 30 or glucose > 500:
        advice.append("❗ Blood glucose value abnormal, please verify your input. Normal fasting blood glucose is usually below 100 mg/dL.")
    return advice

# Streamlit UI
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
        
        work_type_options = { # Mapping user-friendly names to numerical values
            "children": 0,
            "Govt_job": 1,
            "Private": 2,
            "Self-employed": 3,
            "Never_worked": 4
        }
        work_type_display = list(work_type_options.keys()) # Get keys for display
        selected_work_type_str = st.selectbox("Work Type?", work_type_display, help="Your current work type.")
        work_type = work_type_options[selected_work_type_str] # Get the numerical value from the dictionary

        Residence = st.selectbox("Where you live?", [0, 1], format_func=lambda x: 'Urban' if x == 1 else 'Rural', help="Urban or Rural residence?")
    
    # Smoking status: assuming 0=never smoked, 1=formerly smoked, 2=smokes as common encoding
    # Based on your previous format_func, it seems you were mapping 0 to 'never smoked' and 1 to 'smokes/formerly smoked'.
    # If your model expects 3 categories (0,1,2), you need to adjust this.
    # For now, adhering to your previous logic, but making it explicit:
    smoking_status_options_map = {
        'never smoked': 0,
        'smokes/formerly smoked': 1 # Assuming this covers both formerly smoked and smokes for your model's '1'
    }
    smoking_status_display_options = list(smoking_status_options_map.keys())
    selected_smoking_status_str = st.selectbox(
        "Smoking Status",
        options=smoking_status_display_options,
        help="Your current smoking status (e.g., 'never smoked', 'smokes/formerly smoked')."
    )
    smoking_status = smoking_status_options_map[selected_smoking_status_str] # Get the numerical value

    st.subheader("📊 Numerical Measures")
    bmi = st.number_input("BMI (Body Mass Index)", min_value=0.0, max_value=100.0, value=25.0, step=0.1, help="Body Mass Index (e.g., 25.0 for average)")
    age = st.number_input("Age", min_value=0, max_value=120, value=40, step=1, help="Your age in years.")
    glucose = st.number_input("Average Glucose Level", min_value=0.0, max_value=300.0, value=90.0, step=0.1, help="Your average blood glucose level (e.g., 90.0 mg/dL)")

    # Convert gender to numeric (assuming Female=1, Male=0 based on typical encoding)
    gender_numeric = 1 if gender == 'Female' else 0

    # Store all features in a dictionary, keyed by their original names
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

    # Predict button
    if st.button("Predict Stroke Risk", type="primary"):
        # Display health advice first
        advices = advice_on_values(age, bmi, glucose)
        if advices:
            st.subheader("💡 Health Advice:")
            for adv in advices:
                st.info(adv)
        else:
            st.success("Inputs seem within typical ranges for prediction.")

        # Make prediction
        prediction, prob = predict_stroke(features)

        st.subheader("📊 Stroke Risk Prediction Result")
        st.metric("🧠 Probability of Stroke", f"{prob:.2%}")

        if prediction == 1:
            st.error("🔴 Based on the provided data, there is a **predicted risk of stroke**. It is highly recommended to consult a doctor promptly for a professional evaluation.")
        else:
            st.success("🟢 Based on the provided data, there is **no predicted risk of stroke**.")
        
        st.write("*(Please note: This is a predictive model result and not medical advice. Always consult a healthcare professional for diagnosis and treatment.)*")

        st.markdown("---") # Separator

        # 💬 Gemini Chatbox with form
        with st.expander("💬 Ask AI about your health or risk results"):
            st.write("Have a question about your results or general health related to stroke risk? Ask Gemini AI!")
            with st.form("ask_gemini_form"):
                user_q = st.text_input("What do you want to ask Gemini AI?", placeholder="e.g., What are common symptoms of stroke?")
                submitted = st.form_submit_button("Send Question")

            if submitted and user_q:
                st.write("✅ Submitted question:", user_q)
                with st.spinner("Gemini is thinking... 🧠 This may take a moment."):
                    reply = ask_gemini_about_result(user_q, features)
                    st.markdown("---")
                    st.markdown("### 🤖 Gemini says:")
                    st.write(reply)
                    st.markdown("---")


# Run the app
if __name__ == '__main__':
    main()
