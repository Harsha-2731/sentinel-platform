import base64
import time
import subprocess
import os

# --- CONFIGURATION (Matches Sentinel's Whitelist) ---
PRIVATE_KEY_B64 = "MHcCAQEEIHznd6LNrOIwEnx2xtDwlPZ7pjjxah+e9jD8PZ2uYDwUoAoGCCqGSM49AwEHoUQDQgAE9erU8xRDGj+xbSbqaJ83UHLN17EXREKHh3rwdOVY1+C8cms5XIKqIjfZsvpeKmyCsMylH7XU2k8KGILEptaGWg=="

def generate_test_intent():
    # Helper to sign using OpenSSL
    def sign_with_openssl(message, priv_b64):
        with open("tmp_priv.der", "wb") as f:
            f.write(base64.b64decode(priv_b64))
        
        with open("tmp_msg.txt", "wb") as f:
            f.write(message.encode('utf-8'))
            
        subprocess.run(["openssl", "ec", "-inform", "DER", "-in", "tmp_priv.der", "-out", "tmp_priv.pem"], 
                       capture_output=True, check=True)
        
        result = subprocess.run(["openssl", "dgst", "-sha256", "-sign", "tmp_priv.pem", "tmp_msg.txt"], 
                                capture_output=True, check=True)
        
        for f in ["tmp_priv.der", "tmp_msg.txt", "tmp_priv.pem"]:
            if os.path.exists(f): os.remove(f)
            
        return base64.b64encode(result.stdout).decode('utf-8')

    print("\n--- SENTINEL EDR REGISTRY & POLICY VALIDATION ---")
    
    AGENT_ID = "com.ai.shopping.agent"
    NAME = "Shopping Bot"
    TIMESTAMP = int(time.time() * 1000)

    # 1. AUTHORIZED ACTION
    TYPE_AUTH = "PAYMENT"
    PAYLOAD_AUTH = f"{NAME}:{TYPE_AUTH}:4500:{TIMESTAMP}"
    SIG_AUTH = sign_with_openssl(PAYLOAD_AUTH, PRIVATE_KEY_B64)

    # 2. UNAUTHORIZED ACTION (Least-Privilege Violation)
    TYPE_UNAUTH = "SEND_SMS"
    PAYLOAD_UNAUTH = f"{NAME}:{TYPE_UNAUTH}:0:{TIMESTAMP}"
    SIG_UNAUTH = sign_with_openssl(PAYLOAD_UNAUTH, PRIVATE_KEY_B64)

    def get_adb(type, sig, amount=0):
        return [
            "adb", "shell", "am", "broadcast",
            "-a", "com.sentinel.agent.ACTION_AGENT_EXECUTE",
            "--es", "AGENT_NAME", f"'{NAME}'",
            "--es", "ACTION_TYPE", f"'{type}'",
            "--ei", "AMOUNT", str(amount),
            "--es", "com.sentinel.agent.EXTRA_AGENT_ID", f"'{AGENT_ID}'",
            "--el", "com.sentinel.agent.EXTRA_TIMESTAMP", str(TIMESTAMP),
            "--es", "com.sentinel.agent.EXTRA_SIGNATURE", f"'{sig}'"
        ]

    print("\n[COMMAND 1] Authorized Action (PAYMENT):")
    print(" ".join(get_adb(TYPE_AUTH, SIG_AUTH, 4500)))

    print("\n[COMMAND 2] Unauthorized Action (SEND_SMS - Policy Violation):")
    print(" ".join(get_adb(TYPE_UNAUTH, SIG_UNAUTH, 0)))

    print("\n[COMMAND 3] Unknown Agent (Malicious):")
    mal_adb = get_adb(TYPE_AUTH, SIG_AUTH, 4500)
    # Replace Agent ID with something unregistered
    mal_adb[11] = "'com.hacker.agent'"
    print(" ".join(mal_adb))

if __name__ == "__main__":
    generate_test_intent()
