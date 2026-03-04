const mongoose = require('mongoose');

const emergencyLogSchema = new mongoose.Schema({
    userId: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'User',
        required: true
    },
    timestamp: {
        type: Date,
        default: Date.now
    },
    latitude: {
        type: Number,
        required: true
    },
    longitude: {
        type: Number,
        required: true
    },
    riskScore: {
        type: Number,
        required: true
    },
    movementData: {
        type: mongoose.Schema.Types.Mixed,
        required: true
    },
    status: {
        type: String,
        enum: ['active', 'resolved'],
        default: 'active'
    },
    audioFileUrl: {
        type: String,
        default: null
    },
    // V3 Hardening: Decentralization (Proof-of-Integrity Log Anchoring)
    previousHash: {
        type: String,
        default: 'genesis_hash'
    },
    logHash: {
        type: String
    },
    // V5 Ethereum transaction hash integration
    txHash: {
        type: String,
        default: null
    },
    blockNumber: {
        type: Number,
        default: null
    },
    chainId: {
        type: Number,
        default: null
    }
}, {
    timestamps: true
});

module.exports = mongoose.model('EmergencyLog', emergencyLogSchema);
