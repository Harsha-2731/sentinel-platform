const mongoose = require('mongoose');

const userSchema = new mongoose.Schema({
    name: {
        type: String,
        required: true
    },
    email: {
        type: String,
        required: true,
        unique: true
    },
    password: {
        type: String,
        required: true
    },
    deviceId: {
        type: String,
        required: true
    },
    hmacSecret: {
        type: String
    },
    trustedContacts: {
        type: [String],
        default: []
    },
    riskPreference: {
        type: String,
        enum: ['low', 'medium', 'high'],
        default: 'medium'
    },
    lastLoginIp: {
        type: String
    },
    lastSeenAt: {
        type: Number
    },
    createdAt: {
        type: Date,
        default: Date.now
    }
});

module.exports = mongoose.model('User', userSchema);
