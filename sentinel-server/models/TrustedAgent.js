const mongoose = require('mongoose');

/**
 * TrustedAgent Schema (EDR Part 2)
 * 
 * This model stores the "Source of Truth" for AI Agent identities.
 * It enforces Least-Privilege by defining which actions a specific
 * agent is authorized to perform on the user's device.
 */
const trustedAgentSchema = new mongoose.Schema({
    agentId: {
        type: String,
        required: true,
        unique: true,
        trim: true
    },
    publicKey: {
        type: String, // ECDSA Public Key in Base64
        required: true
    },
    allowedActions: {
        type: [String], // e.g., ["PAYMENT", "BROWSE", "LOCATION"]
        default: []
    },
    status: {
        type: String,
        enum: ['active', 'suspended', 'revoked'],
        default: 'active'
    },
    createdAt: {
        type: Date,
        default: Date.now
    }
});

module.exports = mongoose.model('TrustedAgent', trustedAgentSchema);
