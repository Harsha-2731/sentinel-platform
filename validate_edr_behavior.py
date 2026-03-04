import base64
import time
import subprocess
import os

# --- CONFIGURATION ---
PRIVATE_KEY_B64 = "MHcCAQEEIHznd6LNrOIwEnx2xtDwlPZ7pjjxah+e9jD8PZ2uYDwUoAoGCCqGSM49AwEHoUQDQgAE9erU8xRDGj+xbSbqaJ83UHLN17EXREKHh3rwdOVY1+C8cms5XIKqIjfZsvpeKmyCsMylH7XU2k8KGILEptaGWg=="

def generate_test_intent(burst=False):
    # Helper to sign using OpenSSL
    def sign_with_openssl(message, priv_b64):
        with open("tmp_priv.der", "wb") as f:
            f.write(base64.b64decode(priv_b64))
        with open("tmp_msg.txt", "wb") as f:
            f.write(message.encode('utf-8'))
        subprocess.run(["openssl", "ec", "-inform", "DER", "-in", "tmp_priv.der", "-out", "tmp_priv.pem"], capture_output=True, check=True)
        result = subprocess.run(["openssl", "dgst", "-sha256", "-sign", "tmp_priv.pem", "tmp_msg.txt"], capture_output=True, check=True)
        for f in ["tmp_priv.der", "tmp_msg.txt", "tmp_priv.pem"]:
            if os.path.exists(f): os.remove(f)
        return base64.b64encode(result.stdout).decode('utf-8')

    AGENT_ID = "com.ai.shopping.agent"
    NAME = "Shopping Bot"

    def fire_intent(index):
        ts = int(time.time() * 1000)
        payload = f"{NAME}:PAYMENT:100:{ts}"
        sig = sign_with_openssl(payload, PRIVATE_KEY_B64)
        
        cmd = [
            "adb", "shell", "am", "broadcast",
            "-a", "com.sentinel.agent.ACTION_AGENT_EXECUTE",
            "--es", "AGENT_NAME", f"'{NAME}'",
            "--es", "ACTION_TYPE", "'PAYMENT'",
            "--ei", "AMOUNT", "100",
            "--es", "com.sentinel.agent.EXTRA_AGENT_ID", f"'{AGENT_ID}'",
            "--el", "com.sentinel.agent.EXTRA_TIMESTAMP", str(ts),
            "--es", "com.sentinel.agent.EXTRA_SIGNATURE", f"'{sig}'"
        ]
        print(f"[{index}] Firing Intent: {payload}")
        subprocess.run(cmd, capture_output=True)

    if not burst:
        print("\n--- SENTINEL BEHAVIORAL AI: NORMAL ACTIVITY ---")
        fire_intent(1)
        print("Done. Check Dashboard - Risk should remain NORMAL.")
    else:
        print("\n--- SENTINEL BEHAVIORAL AI: BURST ATTACK SIMULATION (5 Actions in 5s) ---")
        for i in range(1, 6):
            fire_intent(i)
            time.sleep(0.5)
        print("Burst Complete. Check Dashboard - Risk should spike to SUSPICION/EMERGENCY due to 'BEHAVIOR_BURST'.")

if __name__ == "__main__":
    import sys
    is_burst = "--burst" in sys.argv
    generate_test_intent(burst=is_burst)
