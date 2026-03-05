const mongoose = require('mongoose');

const deviceSchema = mongoose.Schema({
    userEmail: {
        type: String,
        required: true,
        index: true
    },
    deviceId: {
        type: String,
        required: true,
        unique: true
    },
    deviceName: {
        type: String,
        required: true
    },
    model: {
        type: String
    },
    battery: {
        type: Number,
        default: 100
    },
    securityScore: {
        type: Number,
        default: 100
    },
    status: {
        type: String,
        default: 'secure'
    },
    latitude: {
        type: Number,
        default: 0
    },
    longitude: {
        type: Number,
        default: 0
    },
    lastSeen: {
        type: Date,
        default: Date.now
    }
}, {
    timestamps: true
});

module.exports = mongoose.model('Device', deviceSchema);
