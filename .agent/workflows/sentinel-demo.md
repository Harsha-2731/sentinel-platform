---
description: How to demonstrate the Sentinel AI Behavioral Firewall
---
# Sentinel AI Demo Workflow

Follow these steps to demonstrate the full-stack security capabilities of the Sentinel EDR system.

### Phase 1: Backend Initialization
1. Navigate to the server directory:
   ```bash
   cd sentinel-server
   ```
2. Install dependencies:
   // turbo
   ```bash
   npm install
   ```
3. Start the Node.js server:
   // turbo
   ```bash
   npm run dev
   ```
   *Note: The server will remain online even if MongoDB is not running (Resilient Mode).*

### Phase 2: Android App Deployment
1. Open the `/SentinelApp` project in **Android Studio**.
2. Build and run the app on a physical device or emulator.
3. Log in with any demo credentials (email/password) to authorize the agent.

### Phase 3: Executing a Rogue Agent Attack
1. On the **Dashboard**, observe the 0% Risk Meter.
2. Tap the **"ROGUE AGENT"** button at the bottom.
3. This triggers a simulate command from a "Shopping Agent" trying to spend **₹10,000** (exceeding the ₹100 safety threshold).
4. **Observe the Firewall Reaction**:
   *   The Risk Meter jumps to **100%**.
   *   The **System Integrity Status** bar turns **RED (COMPROMISED)**.
   *   The **Emergency Lockdown UI** (Red Screen) appears immediately.
   *   A 5-second countdown starts for identity revocation.

### Phase 4: Verifying the Agent Task Storer
1. After the lockdown, tap the **"TASK LOG"** button on the Dashboard.
2. Verify that the intercepted "blocked" task is visible in the local history.
3. (Optional) Check the Node.js console to see the cryptographically chained log being anchored to the server.

### Phase 5: Managed Settings (Biometric Guard)
1. Tap the **Settings (Shield Icon)** in the top right.
2. Provide your **Biometric/PIN** to authorize access.
3. Change the "Max Agent Spending" threshold.
4. Save and verify that the new policy is persisted.
