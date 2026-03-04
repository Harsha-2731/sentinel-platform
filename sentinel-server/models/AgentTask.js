const mongoose = require('mongoose');

const agentTaskSchema = new mongoose.Schema({
    userId: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'User',
        required: true
    },
    agentId: {
        type: String,
        required: true,
        default: 'unknown.agent'
    },
    agentName: {
        type: String,
        required: true
    },
    taskDescription: {
        type: String,
        required: true
    },
    actionType: {
        type: String,
        default: 'GENERIC'
    },
    amount: {
        type: Number
    },
    status: {
        type: String,
        enum: ['intercepted', 'allowed', 'blocked', 'unauthorized_agent', 'policy_violation', 'blocked_identity_fail'],
        default: 'intercepted'
    },
    timestamp: {
        type: Date,
        default: Date.now
    },
    previousHash: {
        type: String,
        required: true
    },
    currentHash: {
        type: String,
        required: true
    }
});

module.exports = mongoose.model('AgentTask', agentTaskSchema);
