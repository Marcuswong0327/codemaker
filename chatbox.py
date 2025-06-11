import streamlit as st
import requests

def test_openrouter_api_key(api_key):
    headers = {
        "Authorization": f"Bearer {api_key}",
        "HTTP-Referer": "https://stroke-risk-prediction-grp-1.streamlit.app/",
        "X-Title": "Test Gemini API Key"
    }

    data = {
        "model": "google/gemini-2.5-flash-preview-05-20",
        "messages": [
            {"role": "user", "content": "Say hello in a friendly way!"}
        ]
    }

    try:
        response = requests.post(
            "https://openrouter.ai/api/v1/chat/completions",
            headers=headers,
            json=data,
            timeout=15
        )
        response.raise_for_status()

        return {"success": True, "response": response.json()}

    except requests.exceptions.HTTPError as e:
        return {
            "success": False,
            "error": f"HTTP error: {e}",
            "status_code": response.status_code,
            "response_text": response.text
        }

    except requests.exceptions.RequestException as e:
        return {"success": False, "error": f"Request error: {e}"}

    except Exception as e:
        return {"success": False, "error": f"Unexpected error: {e}"}


def main():
    st.title("🔐 Gemini API Key Tester")

    api_key = st.secrets.get("openrouter_api_key", None)
    if not api_key:
        st.error("❌ API key not found in Streamlit secrets.")
        st.stop()

    st.info("Testing Gemini API key via OpenRouter...")

    result = test_openrouter_api_key(api_key)

    if result["success"]:
        st.success("✅ API key is working!")
        st.subheader("🤖 Gemini says:")
        reply = result["response"]["choices"][0]["message"]["content"]
        st.write(reply)

    else:
        st.error("❌ API key test failed.")
        st.subheader("⚠️ Error Details:")
        st.code(result.get("error", "No error provided"), language="text")
        if "response_text" in result:
            st.subheader("📨 Response Text:")
            st.code(result["response_text"], language="json")

        if "status_code" in result:
            st.write(f"HTTP Status Code: {result['status_code']}")


if __name__ == "__main__":
    main()
