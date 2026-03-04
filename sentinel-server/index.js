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

// Security Middleware
app.use(helmet());
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

// Serve static public files
app.use('/public', express.static(path.join(__dirname, 'public')));

// Serve explicit dashboard path safely by reading file into memory
const fs = require('fs');
const dashboardPath = path.join(__dirname, 'public', 'dashboard.html');
const dashboardHtml = fs.existsSync(dashboardPath) ? fs.readFileSync(dashboardPath, 'utf8') : 'Dashboard not found';

app.get(['/dashboard', '/dashboard/'], (req, res) => {
    res.send(dashboardHtml);
});

// Basic Route with Animated HTML Landing Page
app.get('/', (req, res) => {
    res.send(`
        <!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Sentinel AI Node</title>
            <style>
                body {
                    margin: 0;
                    padding: 0;
                    height: 100vh;
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    background-color: #050510;
                    color: #00ffcc;
                    font-family: 'Courier New', Courier, monospace;
                    overflow: hidden;
                }
                .container {
                    text-align: center;
                    position: relative;
                }
                .agent-icon {
                    width: 100px;
                    height: 100px;
                    border-radius: 50%;
                    border: 3px solid #00ffcc;
                    box-shadow: 0 0 20px #00ffcc, inset 0 0 20px #00ffcc;
                    margin: 0 auto 30px auto;
                    position: relative;
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    animation: pulse 2s infinite ease-in-out, float 4s infinite ease-in-out;
                }
                .core {
                    width: 50px;
                    height: 50px;
                    background-color: #00ffcc;
                    border-radius: 50%;
                    box-shadow: 0 0 30px #ffffff;
                    animation: heartbeat 1.5s infinite alternate;
                }
                h1 {
                    font-size: 2rem;
                    text-transform: uppercase;
                    letter-spacing: 5px;
                    margin-bottom: 10px;
                    text-shadow: 0 0 10px #00ffcc;
                }
                p {
                    font-size: 1.2rem;
                    opacity: 0.8;
                }
                .scanner-line {
                    position: absolute;
                    top: 0;
                    left: 0;
                    width: 100%;
                    height: 2px;
                    background-color: #ffffff;
                    opacity: 0.5;
                    box-shadow: 0 0 10px #ffffff;
                    animation: scan 3s infinite linear;
                }
                
                @keyframes pulse {
                    0% { transform: scale(1); box-shadow: 0 0 20px #00ffcc, inset 0 0 20px #00ffcc; }
                    50% { transform: scale(1.05); box-shadow: 0 0 40px #00eeaa, inset 0 0 40px #00eeaa; }
                    100% { transform: scale(1); box-shadow: 0 0 20px #00ffcc, inset 0 0 20px #00ffcc; }
                }
                @keyframes float {
                    0%, 100% { top: 0; }
                    50% { top: -15px; }
                }
                @keyframes heartbeat {
                    0% { transform: scale(0.8); opacity: 0.8; }
                    100% { transform: scale(1.1); opacity: 1; }
                }
                @keyframes scan {
                    0% { top: 10%; opacity: 0; }
                    10% { opacity: 1; }
                    90% { opacity: 1; }
                    100% { top: 90%; opacity: 0; }
                }
                .typewriter-text {
                    border-right: 2px solid #00ffcc; 
                    white-space: nowrap; 
                    overflow: hidden;
                    margin: 0 auto;
                    animation: typing 3.5s steps(40, end), blink-caret .75s step-end infinite;
                }
                @keyframes typing {
                    from { width: 0 }
                    to { width: 100% }
                }
                @keyframes blink-caret {
                    from, to { border-color: transparent }
                    50% { border-color: #00ffcc; }
                }
            </style>
        </head>
        <body>
            <a href="/dashboard" style="
                position: absolute;
                top: 20px;
                right: 20px;
                padding: 10px 20px;
                background: transparent;
                border: 1px solid #00ffcc;
                border-radius: 5px;
                color: #00ffcc;
                text-decoration: none;
                font-size: 0.8rem;
                font-weight: bold;
                text-transform: uppercase;
                letter-spacing: 1px;
                transition: all 0.3s ease;
                z-index: 100;
            " onmouseover="this.style.background='#00ffcc'; this.style.color='#050510'; this.style.boxShadow='0 0 15px #00ffcc';" 
               onmouseout="this.style.background='transparent'; this.style.color='#00ffcc'; this.style.boxShadow='none';">
                Dashboard
            </a>

            <div class="container">
                <div class="agent-icon">
                    <div class="core"></div>
                    <div class="scanner-line"></div>
                </div>
                <h1>Sentinel Node Online</h1>
                <p class="typewriter-text">Personal AI Guardian Active. Protecting Intent...</p>
                <div style="margin-top: 20px; font-size: 0.7rem; opacity: 0.5; letter-spacing: 2px;">
                    SECURE HUB :: NODE_ID_${process.env.DEVICE_ID || 'CORE'}
                </div>
            </div>
        </body>
        </html>
    `);
});

// Error handling middleware
app.use((err, req, res, next) => {
    console.error(err.stack);
    res.status(500).json({ message: 'Something went wrong on the server' });
});

const PORT = process.env.PORT || 3000;

app.listen(PORT, '0.0.0.0', () => {
    console.log(`Sentinel Server running on port ${PORT}`);
});
