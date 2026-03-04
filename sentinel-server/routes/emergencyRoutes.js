const express = require('express');
const router = express.Router();
const rateLimit = require('express-rate-limit');
const { triggerEmergency, resolveEmergency, verifyLedger } = require('../controllers/emergencyController');
const { protect, verifyHmac } = require('../middleware/auth');

// ✅ HARDENING: Rate Limit Emergency Endpoint (5 req/min)
const emergencyLimiter = rateLimit({
    windowMs: 1 * 60 * 1000, // 1 minute
    max: 5, // Limit each IP/Agent to 5 requests per windowMs
    message: { message: "Too many emergency triggers created from this IP, please try again after a minute" }
});

// Advanced Network Hardening: verifyHmac ensures payload hasn't been tampered with
router.post('/trigger', protect, verifyHmac, emergencyLimiter, triggerEmergency);
router.get('/verify-ledger', protect, verifyLedger);
router.put('/:id/resolve', protect, resolveEmergency);

module.exports = router;
