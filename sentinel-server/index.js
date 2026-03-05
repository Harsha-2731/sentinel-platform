require('dotenv').config();
const express = require('express');
const helmet = require('helmet');
const cors = require('cors');
const rateLimit = require('express-rate-limit');
const connectDB = require('./config/db');

const authRoutes = require('./routes/authRoutes');
const emergencyRoutes = require('./routes/emergencyRoutes');
const userRoutes = require('./routes/userRoutes');
const taskRoutes = require('./routes/taskRoutes');
const commandRoutes = require('./routes/commandRoutes');
const registryRoutes = require('./routes/registryRoutes');
const collectorRoutes = require('./routes/collectorRoutes');

const path = require('path');
const { ethers } = require("ethers");

// Connect to Database
connectDB();

// ✅ HARDENING: Verify Smart Contract Address
if (!ethers.isAddress(process.env.CONTRACT_ADDRESS)) {
    throw new Error("Invalid Ethereum CONTRACT_ADDRESS provided in .env!");
}

const app = express();

// ✅ DEV MODE: NUCLEAR PROTOCOL RESET
// We disable all security headers entirely to prevent browsers from forcing HTTPS.
app.use(cors());

// Limit repeated API requests
const limiter = rateLimit({
    windowMs: 15 * 60 * 1000, // 15 minutes
    max: 100 // limit each IP to 100 requests per windowMs
});
app.use('/api', limiter);

// Body parser
app.use(express.json());

// Routes
app.use('/api/auth', authRoutes);
app.use('/api/emergency', emergencyRoutes);
app.use('/api/user', userRoutes);
app.use('/api/tasks', taskRoutes);
app.use('/api/commands', commandRoutes);
app.use('/api/registry', registryRoutes);
app.use('/api/collector', collectorRoutes);

// V2 Architecture: Explicit Device & Heartbeat Tracking
const deviceRoutes = require('./routes/deviceRoutes');
app.use('/api/device', deviceRoutes);

// Serve static public files
app.use('/public', express.static(path.join(__dirname, 'public')));

app.get(['/login', '/login/'], (req, res) => {
    res.sendFile('login.html', { root: path.join(__dirname, 'public') });
});

app.get(['/dashboard', '/dashboard/'], (req, res) => {
    res.sendFile('dashboard.html', { root: path.join(__dirname, 'public') });
});

// Landing Page Route
app.get('/', (req, res) => {
    res.send(`<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sentinel AI Node</title>
    <script>
        // Force HTTP Downgrade
        if (window.location.protocol === 'https:') {
            window.location.href = window.location.href.replace('https:', 'http:');
        }
    </script>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700;800&display=swap" rel="stylesheet">
    <style>
        :root { --bg:#050510; --card:#0D1117; --border:#1F2937; --accent:#00FFCC; --danger:#FF3366; --text:#E5E7EB; --dim:#6B7280; }
        * { box-sizing:border-box; margin:0; padding:0; }
        body { background:var(--bg); color:var(--text); font-family:'Inter',sans-serif; min-height:100vh; overflow-x:hidden; }

        /* NAV */
        .nav {
            display:flex; align-items:center; justify-content:space-between;
            padding:16px 32px; border-bottom:1px solid var(--border);
            background:rgba(5,5,16,0.95); backdrop-filter:blur(12px);
            position:sticky; top:0; z-index:1000;
        }
        .nav-logo { font-size:1.2rem; font-weight:800; letter-spacing:4px; color:var(--accent); text-shadow:0 0 10px var(--accent); }
        .nav-links { display:flex; gap:10px; align-items:center; }
        .nav-btn { padding:8px 18px; border-radius:7px; font-size:0.75rem; font-weight:600; letter-spacing:1px; text-transform:uppercase; cursor:pointer; border:none; transition:all 0.2s; }
        .btn-ghost { background:transparent; color:var(--dim); border:1px solid var(--border); }
        .btn-ghost:hover { border-color:var(--accent); color:var(--accent); }
        .btn-primary { background:var(--accent); color:#050510; font-weight:800; }
        .btn-primary:hover { box-shadow:0 0 25px rgba(0,255,204,0.4); }

        /* HERO */
        .hero { text-align:center; padding:90px 20px 60px; max-width:800px; margin:0 auto; }
        .hero-badge { display:inline-flex; align-items:center; gap:8px; border:1px solid rgba(0,255,204,0.3); border-radius:20px; padding:5px 14px; font-size:0.65rem; letter-spacing:2px; color:var(--accent); margin-bottom:28px; }
        .pulse { width:6px; height:6px; border-radius:50%; background:var(--accent); animation:pulse 1.5s infinite; }
        @keyframes pulse { 0%,100%{opacity:1;} 50%{opacity:0.3;} }
        .hero h1 { font-size:3.2rem; font-weight:800; line-height:1.1; letter-spacing:-1px; margin-bottom:20px; }
        .hero h1 span { color:var(--accent); text-shadow:0 0 20px var(--accent); }
        .hero p { font-size:1rem; color:var(--dim); max-width:560px; margin:0 auto 36px; line-height:1.7; }
        .hero-actions { display:flex; gap:14px; justify-content:center; flex-wrap:wrap; position: relative; z-index: 100; }
        .hero-btn { padding:14px 32px; border-radius:10px; font-size:0.85rem; font-weight:700; letter-spacing:1px; text-transform:uppercase; cursor:pointer; border:none; transition:all 0.3s; pointer-events: auto; }
        .hero-btn-primary { background:var(--accent); color:#050510; }
        .hero-btn-primary:hover { box-shadow:0 0 35px rgba(0,255,204,0.4); transform:translateY(-2px); }
        .hero-btn-secondary { background:transparent; color:var(--text); border:1px solid var(--border); }
        .hero-btn-secondary:hover { border-color:var(--accent); color:var(--accent); }

        /* ICON ORB */
        .orb-wrap { display:flex; justify-content:center; margin:40px 0; pointer-events: none; }
        .orb { width:100px; height:100px; border-radius:50%; border:2px solid var(--accent); box-shadow:0 0 30px var(--accent),inset 0 0 30px rgba(0,255,204,0.1); display:flex; align-items:center; justify-content:center; animation:float 4s ease-in-out infinite; pointer-events: none; }
        .orb-core { width:48px; height:48px; border-radius:50%; background:var(--accent); box-shadow:0 0 20px var(--accent); animation:heartbeat 1.5s infinite alternate; }
        @keyframes float { 0%,100%{transform:translateY(0);} 50%{transform:translateY(-14px);} }
        @keyframes heartbeat { 0%{transform:scale(0.85);} 100%{transform:scale(1.1);} }

        /* FEATURES */
        .features { max-width:1100px; margin:0 auto; padding:20px 24px 80px; display:grid; grid-template-columns:repeat(auto-fit,minmax(240px,1fr)); gap:20px; }
        .feat-card { background:var(--card); border:1px solid var(--border); border-radius:14px; padding:24px; transition:border-color 0.2s; }
        .feat-card:hover { border-color:rgba(0,255,204,0.3); }
        .feat-icon { width:40px; height:40px; border-radius:10px; display:flex; align-items:center; justify-content:center; font-size:1.2rem; margin-bottom:16px; }
        .feat-title { font-size:0.85rem; font-weight:700; margin-bottom:8px; letter-spacing:0.5px; }
        .feat-desc { font-size:0.75rem; color:var(--dim); line-height:1.6; }

        /* NODE ID FOOTER */
        .node-id { text-align:center; font-size:0.6rem; letter-spacing:3px; color:var(--dim); padding:20px; border-top:1px solid var(--border); }
    </style>
</head>
<body>

<nav class="nav">
    <div class="nav-logo">SENTINEL</div>
    <div class="nav-links">
        <a class="nav-btn btn-ghost" href="/" style="text-decoration:none">HOME</a>
        <a class="nav-btn btn-ghost" href="/dashboard" style="text-decoration:none">DASHBOARD</a>
        <a class="nav-btn btn-primary" id="loginBtn" href="/login" style="text-decoration:none; display:flex; align-items:center; justify-content:center;">LOGIN</a>
    </div>
</nav>

<div class="hero">
    <div class="hero-badge"><div class="pulse"></div> SENTINEL GUARDIAN NODE ONLINE</div>
    <div class="orb-wrap"><div class="orb"><div class="orb-core"></div></div></div>
    <h1>Your Personal <span>AI Security</span><br>Guardian</h1>
    <p>Real-time behavioral firewall, tamper detection, and remote device control — all from one place.</p>
    <div class="hero-actions">
        <a class="hero-btn hero-btn-primary" href="/dashboard" style="text-decoration:none; display:flex; align-items:center; justify-content:center;">Open Dashboard</a>
        <a class="hero-btn hero-btn-secondary" href="/login" style="text-decoration:none; display:flex; align-items:center; justify-content:center;">Login / Register</a>
    </div>

    <!-- Troubleshooting -->
    <div style="margin-top: 40px; font-size: 0.75rem; color: var(--danger); border: 1px solid rgba(255,51,102,0.2); border-radius: 10px; padding: 15px; display: none;" id="sslWarning">
        <b>⚠️ CONNECTION ALERT:</b> If you see "Not Secure" or SSL errors, please ensure you are using <b>HTTP</b> (not HTTPS) for local development: 
        <br><a href="/" style="color:var(--accent); text-decoration: underline;">Switch to HTTP</a>
    </div>
</div>

<div class="features">
    <div class="feat-card">
        <div class="feat-icon" style="background:rgba(0,255,204,0.1);">🛡️</div>
        <div class="feat-title">Behavioral Firewall</div>
        <div class="feat-desc">Real-time AI anomaly detection monitors every action your device takes, 24/7.</div>
    </div>
    <div class="feat-card">
        <div class="feat-icon" style="background:rgba(255,51,102,0.1);">🔒</div>
        <div class="feat-title">Remote Lock & Wipe</div>
        <div class="feat-desc">Instantly lock or wipe your device from anywhere if a breach is detected.</div>
    </div>
    <div class="feat-card">
        <div class="feat-icon" style="background:rgba(255,204,0,0.1);">⛓️</div>
        <div class="feat-title">Blockchain Audit Trail</div>
        <div class="feat-desc">Immutable, tamper-proof security logs anchored to the Ethereum blockchain.</div>
    </div>
    <div class="feat-card">
        <div class="feat-icon" style="background:rgba(0,255,204,0.1);">🧠</div>
        <div class="feat-title">EDR Risk Engine V2</div>
        <div class="feat-desc">Probabilistic risk scoring with state-based escalation: SECURE → VIGILANT → LOCKDOWN.</div>
    </div>
</div>

<div class="node-id">SENTINEL COMMAND NODE :: ID_${process.env.DEVICE_ID || 'CORE'} :: ALL SYSTEMS NOMINAL</div>

<script>
    // Update nav safely
    document.addEventListener("DOMContentLoaded", () => {
        try {
            const token = localStorage.getItem('sentinel_token');
            const userData = localStorage.getItem('sentinel_user');
            const user = userData ? JSON.parse(userData) : null;
            
            if (token && user && user.name) {
                const btn = document.getElementById('loginBtn');
                if (btn) {
                    btn.innerText = user.name.toUpperCase().slice(0,12);
                    btn.onclick = (e) => { 
                        e.preventDefault(); 
                        window.location.href = '/dashboard'; 
                    };
                }
            }
        } catch (e) { console.warn("Auth check failed", e); }
    });
</script>
    <script>
        // Hero SSL Warning
        if (window.location.protocol === 'https:') {
            const warning = document.getElementById('sslWarning');
            if (warning) warning.style.display = 'block';
        }
    </script>
</body>
</html>`);
});

// Error handling middleware
app.use((err, req, res, next) => {
    console.error(err.stack);
    res.status(500).json({ message: 'Something went wrong on the server' });
});

const PORT = process.env.PORT || 8080;

app.listen(PORT, '0.0.0.0', () => {
    console.log(`Sentinel Server running on port ${PORT}`);
});
