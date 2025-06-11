import requests
import streamlit as st

def test_gemini_api(api_key):
    headers = {
        "Authorization": f"Bearer {api_key}",
        "HTTP-Referer": "https://stroke-risk-prediction-grp-1.streamlit.app/",
        "X-Title": "Stroke Risk Chat AI"
    }

    try:
        response = requests.get(
            "https://openrouter.ai/api/v1/health",
            headers=headers,
            timeout=15  # Adjust timeout as needed
        )
        response.raise_for_status()
        return response.json()

    except requests.exceptions.HTTPError as e:
        return f"❌ HTTP error from Gemini: {e}\nResponse: {response.text}"

    except requests.exceptions.RequestException as e:
        return f"⚠️ Request error: {e}"

    except Exception as e:
        return f"😵 Unexpected error: {e}"

# Streamlit UI code here

# Main function to test the API key
def main():
    st.title("Gemini API Key Tester")
    api_key = st.secrets.get("openrouter_api_key")

    if not api_key:
        st.error("❌ API key not found. Please check your Streamlit secrets.")
        st.stop()

    st.write("Testing Gemini API key...")

    result = test_gemini_api(api_key)

    if isinstance(result, dict) and 'status' in result and result['status'] == 'healthy':
        st.success("✅ Gemini API key is valid and operational.")
    else:
        st.error("❌ Gemini API key test failed.")

# Run the app
if __name__ == '__main__':
    main()
