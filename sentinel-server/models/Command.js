const mongoose = require('mongoose');

const commandSchema = mongoose.Schema({
    commandId: {
        type: String,
        required: true,
        unique: true
    },
    deviceId: {
        type: String,
        required: true,
        index: true
    },
    commandType: {
        type: String,
        required: true,
        enum: ['LOCK_DEVICE', 'WIPE_DATA', 'FORCE_SCAN', 'LOCATION']
    },
    status: {
        type: String,
        required: true,
        enum: ['PENDING', 'EXECUTED', 'FAILED'],
        default: 'PENDING'
    },
    timestamp: {
        type: Date,
        default: Date.now
    }
}, {
    timestamps: true
});

module.exports = mongoose.model('Command', commandSchema);
