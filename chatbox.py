import streamlit as st
import joblib
import numpy as np
import requests

# Load model and scalers
try:
    model = joblib.load('stacking_classifier_model.pkl')
    scalers = joblib.load('scalers.pkl')
except Exception as e:
    st.error(f"❌ Error loading model or scalers: {e}")
    st.stop()

# Stroke prediction function
def predict_stroke(features):
    features_to_scale = ['bmi', 'age', 'avg_glucose_level']
    scaled_features = []

    scaled_features.extend([features[key] for key in [
        'gender', 'hypertension', 'heart_disease', 'ever_married',
        'work_type', 'Residence_type', 'smoking_status'
    ]])

    for key in features_to_scale:
        scaler = scalers[key]
        scaled_value = scaler.transform(np.array([[features[key]]]))[0][0]
        scaled_features.append(scaled_value)

    prediction = model.predict([scaled_features])[0]
    probability = model.predict_proba([scaled_features])[0][1]
    return prediction, probability

# Gemini chat function
def ask_gemini_about_result(question, features):
    API_KEY = "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"

    headers = {
        "Authorization": f"Bearer {API_KEY}",
        "HTTP-Referer": "https://stroke-risk-prediction-grp-1.streamlit.app/",
        "X-Title": "Stroke Risk Chat AI"
    }

    model = "google/gemini-2.5-pro-preview"
    context = "\n".join([f"{k.replace('_', ' ').title()}: {v}" for k, v in features.items()])

    prompt = f"""You are a health assistant AI helping users understand their stroke risk. 
Here is the user’s health information:
{context}

Now answer this question: {question}
Please explain in simple and friendly terms."""

    data = {
        "model": model,
        "messages": [
            {"role": "user", "content": prompt}
        ]
    }

    try:
        res = requests.post("https://openrouter.ai/api/v1/chat/completions", headers=headers, json=data)
        res.raise_for_status()
        reply = res.json()['choices'][0]['message']['content']
        return reply
    except Exception as e:
        return f"❌ Error calling Gemini: {e}"

# Basic health advice
def advice_on_values(age, bmi, glucose):
    advice = []
    if age < 0 or age > 120:
        advice.append("❗ Invalid age input, please enter between 0 and 120.")
    if bmi < 10 or bmi > 60:
        advice.append("❗ BMI input seems off, please double-check.")
    elif bmi < 18.5:
        advice.append("⚠️ BMI is low, consider improving nutrition and gaining healthy weight.")
    if glucose < 30 or glucose > 500:
        advice.append("❗ Blood glucose value abnormal, please verify your input.")
    return advice

# Streamlit UI
def main():
    st.title("🩺 Stroke Prediction App")
    st.write("Fill in your details below to find out your risk of stroke.")

    gender = st.selectbox("Gender", ['Male', 'Female'])
    hypertension = st.selectbox("Hypertension", [0, 1], format_func=lambda x: 'Yes' if x else 'No')
    heart_disease = st.selectbox("Heart Disease", [0, 1], format_func=lambda x: 'Yes' if x else 'No')
    ever_married = st.selectbox("Ever Married?", [0, 1], format_func=lambda x: 'Yes' if x else 'No')

    work_type_dict = {
        0: "children",
        1: "Govt_job",
        2: "Private",
        3: "Self-employed",
        4: "Never_worked"
    }
    work_type = st.selectbox("Work Type?", list(work_type_dict.keys()), format_func=lambda x: work_type_dict[x])

    Residence = st.selectbox("Where you live?", [0, 1], format_func=lambda x: 'Urban' if x == 1 else 'Rural')
    smoking_status = st.selectbox("Smoking Status", [0, 1], format_func=lambda x: 'smokes/formerly smoked' if x else 'never smoked')

    bmi = st.number_input("BMI (Body Mass Index)", min_value=0.0, step=0.1)
    age = st.number_input("Age", min_value=0, step=1)
    glucose = st.number_input("Blood Glucose Level", min_value=0.0, step=0.1)

    gender_numeric = 1 if gender == 'Female' else 0

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

    if st.button("Predict"):
        advices = advice_on_values(age, bmi, glucose)
        for adv in advices:
            st.info(adv)

        prediction, prob = predict_stroke(features)

        st.subheader("📊 Stroke Risk Prediction")
        st.metric("🧠 Stroke Probability", f"{prob:.2%}")

        if prediction == 1:
            st.error("🔴 You may have a risk of stroke. Please consult a doctor promptly.")
        else:
            st.success("🟢 No predicted risk of stroke.")

        # ✨ Gemini Chatbox appears only after prediction
        with st.expander("💬 Ask AI about your health or risk results"):
            user_q = st.text_input("What do you want to ask Gemini AI?")
            if user_q:
                with st.spinner("Gemini is thinking... 🧠"):
                    reply = ask_gemini_about_result(user_q, features)
                    st.markdown("**🤖 Gemini says:**")
                    st.write(reply)

# Run the app
if __name__ == '__main__':
    main()
