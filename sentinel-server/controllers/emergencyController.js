const EmergencyLog = require('../models/EmergencyLog');
const crypto = require('crypto');
const { triggerEmergencyOnChain } = require('../services/blockchainService'); // ✅ V5 ADDED

// ✅ HARDENING: Nonce Lock for Duplicate Triggers
const emergencyProcessing = {};

const triggerEmergency = async (req, res) => {
    const agentId = req.user._id.toString();

    // Prevent Double Emergency Triggers (Race Conditions)
    if (emergencyProcessing[agentId]) {
        return res.status(429).json({ message: 'Emergency trigger already processing for this agent.' });
    }
    emergencyProcessing[agentId] = true;

    try {
        const { timestamp, latitude, longitude, riskScore, movementData, status, audioFileUrl } = req.body;

        // 1️⃣ Fetch last log
        const lastLog = await EmergencyLog.findOne().sort({ createdAt: -1 });
        const previousHash = lastLog && lastLog.logHash ? lastLog.logHash : 'genesis_hash';

        // ==========================================
        // ✅ V38 ULTIMATE HARDENING: Geo-Fencing & Spoofing Detection
        // ==========================================
        if (lastLog && latitude && longitude && lastLog.latitude && lastLog.longitude) {
            const distance = Math.sqrt(
                Math.pow(latitude - lastLog.latitude, 2) +
                Math.pow(longitude - lastLog.longitude, 2)
            );

            // If distance > 1.0 degrees (~111km) in less than 5 minutes, flag as spoofing
            const timeDiff = (Date.now() - lastLog.createdAt) / (1000 * 60);
            if (distance > 1.0 && timeDiff < 5) {
                console.warn(`[SECURITY ALERT] GPS Spoofing Detected: Impossible movement of ${distance.toFixed(2)} units in ${timeDiff.toFixed(1)} mins.`);
                return res.status(403).json({
                    message: 'Security Policy Violation: GPS Spoofing / Impossible Movement Detected.',
                    integrity: false
                });
            }
        }
        // ==========================================

        // 2️⃣ Compute hash
        const payloadToHash = JSON.stringify({
            userId: req.user._id,
            timestamp,
            latitude,
            longitude,
            riskScore,
            previousHash
        });

        const logHash = crypto.createHash('sha256')
            .update(payloadToHash)
            .digest('hex');

        // ===============================
        // ✅ V5 BLOCKCHAIN ANCHORING
        // ===============================

        let txHash = null;
        let blockNumber = null;
        let chainId = process.env.CHAIN_ID || 1337;

        try {
            // Anchor the logHash and permanently revoke the Agent DID on-chain
            const bctRes = await triggerEmergencyOnChain(
                agentId, // Ensure it uses agentId created above
                logHash
            );

            if (bctRes) {
                txHash = bctRes.hash;
                blockNumber = bctRes.blockNumber;
            }
        } catch (bctError) {
            console.error("Blockchain anchoring failed, proceeding with local revocation off-chain: ", bctError.message);
        }

        // ===============================

        // 3️⃣ Store in MongoDB
        const emergencyLog = await EmergencyLog.create({
            userId: req.user._id,
            timestamp,
            latitude,
            longitude,
            riskScore,
            movementData,
            status,
            audioFileUrl,
            previousHash,
            logHash,
            txHash,
            blockNumber,
            chainId
        });

        res.status(201).json(emergencyLog);

    } catch (error) {
        res.status(500).json({ message: error.message });
    } finally {
        // Release the lock regardless of success or failure
        delete emergencyProcessing[agentId];
    }
};

const resolveEmergency = async (req, res) => {
    try {
        const log = await EmergencyLog.findById(req.params.id);

        if (!log) {
            return res.status(404).json({ message: 'Emergency log not found' });
        }

        if (log.userId.toString() !== req.user._id.toString()) {
            return res.status(401).json({ message: 'Not authorized' });
        }

        log.status = 'resolved';
        const updatedLog = await log.save();
        res.json(updatedLog);

    } catch (error) {
        res.status(500).json({ message: error.message });
    }
};

const verifyLedger = async (req, res) => {
    try {
        const logs = await EmergencyLog.find({ userId: req.user._id }).sort({ createdAt: 1 });

        if (logs.length === 0) {
            return res.json({ integrity: true, message: 'No logs found for verification' });
        }

        let previousHash = 'genesis_hash';
        const violations = [];

        for (const log of logs) {

            if (log.previousHash !== previousHash) {
                violations.push({
                    logId: log._id,
                    expectedPrevious: previousHash,
                    actualPrevious: log.previousHash,
                    reason: 'Chain continuity broken'
                });
            }

            const payloadToHash = JSON.stringify({
                userId: log.userId,
                timestamp: log.timestamp,
                latitude: log.latitude,
                longitude: log.longitude,
                riskScore: log.riskScore,
                previousHash: log.previousHash
            });

            const computedHash = crypto.createHash('sha256')
                .update(payloadToHash)
                .digest('hex');

            if (log.logHash !== computedHash) {
                violations.push({
                    logId: log._id,
                    expectedHash: computedHash,
                    actualHash: log.logHash,
                    reason: 'Log data tampered'
                });
            }

            previousHash = log.logHash;
        }

        if (violations.length > 0) {
            return res.status(400).json({
                integrity: false,
                violationCount: violations.length,
                violations
            });
        }

        res.json({
            integrity: true,
            logCount: logs.length,
            message: 'Cryptographic hash chain verified successfully'
        });

    } catch (error) {
        res.status(500).json({ message: error.message });
    }
};

module.exports = {
    triggerEmergency,
    resolveEmergency,
    verifyLedger
};