const jwt = require('jsonwebtoken');
const crypto = require('crypto');
const User = require('../models/User');
const RevokedToken = require('../models/RevokedToken');
const { isAgentRevokedOnChain } = require('../services/blockchainService');

const protect = async (req, res, next) => {
    let token;

    if (
        req.headers.authorization &&
        req.headers.authorization.startsWith('Bearer')
    ) {
        try {
            token = req.headers.authorization.split(' ')[1];

            // Advanced Network Hardening: Check if token is blacklisted in local MongoDB
            const isRevokedLocally = await RevokedToken.findOne({ token });
            if (isRevokedLocally) {
                return res.status(401).json({ message: 'Token has been revoked locally (Emergency Kill-Switch Active)' });
            }

            const decoded = jwt.verify(token, process.env.JWT_SECRET);

            // ==========================================
            // ✅ V5 BLOCKCHAIN CONSENSUS: On-Chain Check
            // ==========================================
            try {
                const isRevokedOnChain = await isAgentRevokedOnChain(decoded.id.toString());
                if (isRevokedOnChain) {
                    return res.status(401).json({ message: 'Token has been permanently revoked ON THE BLOCKCHAIN (Immutable Kill-Switch Active)' });
                }
            } catch (err) {
                console.error("Critical BCT Failure in Middleware:", err.message);
                return res.status(503).json({ message: 'Blockchain consensus unavailable. Failing Closed for Security.' });
            }
            // ==========================================

            req.user = await User.findById(decoded.id).select('-password');
            if (!req.user) {
                return res.status(401).json({ message: 'User no longer exists' });
            }

            // ==========================================
            // ✅ V38 ULTIMATE HARDENING: Session Fingerprinting
            // ==========================================
            const deviceId = req.headers['x-sentinel-device-id'];
            const userAgent = req.headers['user-agent'];

            if (req.user.deviceId && deviceId && req.user.deviceId !== deviceId) {
                console.warn(`[SECURITY ALERT] Session Fingerprint Mismatch: Expected ${req.user.deviceId}, got ${deviceId}`);
                return res.status(403).json({ message: 'Session Binding Violation. Device Fingerprint Mismatch.' });
            }

            // Simple Velocity Check (Impossible Travel)
            const clientIp = req.ip;
            if (req.user.lastLoginIp && req.user.lastLoginIp !== clientIp) {
                const now = Date.now();
                const lastSeen = req.user.lastSeenAt || 0;
                if (now - lastSeen < 60000) { // 1 minute window
                    console.warn(`[SECURITY ALERT] Velocity Violation: Instant jump detected from ${req.user.lastLoginIp} to ${clientIp}`);
                    return res.status(403).json({ message: 'Security Policy Violation: Impossible Travel Detected.' });
                }
            }

            // Update session metadata
            req.user.lastSeenAt = Date.now();
            req.user.lastLoginIp = clientIp;
            await req.user.save();
            // ==========================================

            next();
        } catch (error) {
            console.error(error);
            res.status(401).json({ message: 'Not authorized, token failed' });
        }
    }

    if (!token) {
        res.status(401).json({ message: 'Not authorized, no token' });
    }
};

// Keep track of nonces in memory (simple replay cache)
const nonceCache = new Set();
// Clean cache periodically to prevent memory leaks
setInterval(() => nonceCache.clear(), 60000);

// Advanced Network Hardening: HMAC Signature Validation
const verifyHmac = (req, res, next) => {
    const clientSignature = req.headers['x-sentinel-signature'];
    const timestamp = req.headers['x-sentinel-timestamp'];
    const nonce = req.headers['x-sentinel-nonce'];

    if (!clientSignature || !timestamp || !nonce) {
        // Enforce HMAC strictly
        return res.status(403).json({ message: 'Missing Request Signature or Replay Headers. Possible Tampering.' });
    }

    // 1. Replay Protection: Check Freshness (< 30s)
    const now = Date.now();
    const timeDiff = now - parseInt(timestamp, 10);
    if (Math.abs(timeDiff) > 30000) {
        return res.status(403).json({ message: 'Request expired. Replay attack detected.' });
    }

    // 2. Replay Protection: Check Nonce
    if (nonceCache.has(nonce)) {
        return res.status(403).json({ message: 'Nonce reused. Replay attack detected.' });
    }
    nonceCache.add(nonce);

    // Hash the stringified body prepended with timestamp and nonce
    const payload = JSON.stringify(req.body);
    const stringToSign = `\${timestamp}:\${nonce}:\${payload}`;

    // Use dynamic device-specific secret if available (from Auth protect middleware)
    const hmacSecret = (req.user && req.user.hmacSecret) ? req.user.hmacSecret : process.env.JWT_SECRET;

    const expectedSignature = crypto.createHmac('sha256', hmacSecret)
        .update(stringToSign)
        .digest('hex');

    if (clientSignature !== expectedSignature) {
        return res.status(403).json({ message: 'Invalid Request Signature. MITM/Tampering Detected.' });
    }

    next();
};

module.exports = { protect, verifyHmac };
