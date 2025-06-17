# Stroke Prediction Web App

This is a user-friendly Streamlit application that predicts the likelihood of a **stroke** based on user health information. The app uses a trained **stacking classifier model** and provides both predictions and **health-related advice**. It also includes a **chatbot assistant** (powered by DeepSeek via OpenRouter API) to answer questions about your results.

---

## 📌 Features

- Predict stroke risk with personalized input (age, BMI, glucose, etc.)
- Displays stroke probability and risk diagnosis.
- AI chatbot assistant (DeepSeek) to explain results in simple language.
- Real-time feedback and advice on abnormal input values.
- Scikit-learn model and scaler loaded using `joblib`.

---

## Tech Stack

- **Frontend**: [Streamlit](https://streamlit.io/)
- **Backend**: Python, Scikit-learn, DeepSeek AI via OpenRouter API
- **Model**: Stacking Classifier (pre-trained and serialized)
- **Deployment**: Streamlit Cloud

---

## How It Works

1. The user inputs their medical and lifestyle data.
2. The app preprocesses and scales numerical features (`bmi`, `age`, `avg_glucose_level`).
3. A machine learning model makes a prediction (0 = No Risk, 1 = Risk).
4. A chatbot can be queried for simple health explanations based on the user's data.

---

Try the app here:  
https://chatbox-zcr7j3lerdmrusdhi4tv3m.streamlit.app/

---

### Input Features

| Feature | Description |
|--------|-------------|
| Gender | Male / Female |
| Hypertension | 0 = No, 1 = Yes |
| Heart Disease | 0 = No, 1 = Yes |
| Ever Married | 0 = No, 1 = Yes |
| Work Type | Categorical (e.g. Govt Job, Private, etc.) |
| Residence Type | Urban / Rural |
| Smoking Status | 0 = Never Smoked, 1 = Smokes/Formerly Smoked |
| BMI | Body Mass Index |
| Age | Age in years |
| Avg Glucose Level | Average blood glucose level (mg/dL) |

---

## Getting Started

### Prerequisites

- Python 3.7+
- `joblib`, `numpy`, `requests`, `streamlit`, `scikit-learn`

