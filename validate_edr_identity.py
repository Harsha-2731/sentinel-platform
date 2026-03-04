import base64
import time
import subprocess
import os

# --- CONFIGURATION (Matches Sentinel's Whitelist) ---
PRIVATE_KEY_B64 = "MHcCAQEEIHznd6LNrOIwEnx2xtDwlPZ7pjjxah+e9jD8PZ2uYDwUoAoGCCqGSM49AwEHoUQDQgAE9erU8xRDGj+xbSbqaJ83UHLN17EXREKHh3rwdOVY1+C8cms5XIKqIjfZsvpeKmyCsMylH7XU2k8KGILEptaGWg=="

def generate_test_intent():
    # Helper to sign using OpenSSL (avoids 'cryptography' library dependency)
    def sign_with_openssl(message, priv_b64):
        with open("tmp_priv.der", "wb") as f:
            f.write(base64.b64decode(priv_b64))
        
        with open("tmp_msg.txt", "wb") as f:
            f.write(message.encode('utf-8'))
            
        # Convert DER to PEM for openssl dgst
        subprocess.run(["openssl", "ec", "-inform", "DER", "-in", "tmp_priv.der", "-out", "tmp_priv.pem"], 
                       capture_output=True, check=True)
        
        # Sign the message
        result = subprocess.run(["openssl", "dgst", "-sha256", "-sign", "tmp_priv.pem", "tmp_msg.txt"], 
                                capture_output=True, check=True)
        
        # Cleanup
        for f in ["tmp_priv.der", "tmp_msg.txt", "tmp_priv.pem"]:
            if os.path.exists(f): os.remove(f)
            
        return base64.b64encode(result.stdout).decode('utf-8')

    print("\n--- SENTINEL EDR IDENTITY VALIDATION (Zero-Dependency Version) ---")
    
    # Action Data
    AGENT_ID = "com.ai.shopping.agent"
    NAME = "Shopping Bot"
    TYPE = "PAYMENT"
    AMOUNT = 4500
    TIMESTAMP = int(time.time() * 1000)

    # Construct Payload: "name:type:amount:timestamp"
    payload = f"{NAME}:{TYPE}:{AMOUNT}:{TIMESTAMP}"

    print(f"Generating signature for: {payload}...")
    
    try:
        sig_b64 = sign_with_openssl(payload, PRIVATE_KEY_B64)
    except Exception as e:
        print(f"ERROR: OpenSSL signing failed: {e}")
        print("Please ensure 'openssl' is installed on your system.")
        return

    # Generate ADB Command
    adb_cmd = [
        "adb", "shell", "am", "broadcast",
        "-a", "com.sentinel.agent.ACTION_AGENT_EXECUTE",
        "--get-receivers",
        "--es", "AGENT_NAME", f"'{NAME}'",
        "--es", "ACTION_TYPE", f"'{TYPE}'",
        "--ei", "AMOUNT", str(AMOUNT),
        "--es", "com.sentinel.agent.EXTRA_AGENT_ID", f"'{AGENT_ID}'",
        "--el", "com.sentinel.agent.EXTRA_TIMESTAMP", str(TIMESTAMP),
        "--es", "com.sentinel.agent.EXTRA_SIGNATURE", f"'{sig_b64}'"
    ]

    print(f"\n[SUCCESS] Generated Security Extras:")
    print(f"  Agent ID:  {AGENT_ID}")
    print(f"  Signature: {sig_b64}")
    
    print("\n[COMMAND 1] Valid Signed Intent (Copy-Paste this):")
    print(" ".join(adb_cmd))

    print("\n[COMMAND 2] Invalid (Spoofed) Intent (Will be blocked):")
    spoofed_cmd = adb_cmd[:-1] + [f"'{sig_b64}_invalid'"]
    print(" ".join(spoofed_cmd))

if __name__ == "__main__":
    generate_test_intent()
