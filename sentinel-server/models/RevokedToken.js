const mongoose = require('mongoose');

const revokedTokenSchema = new mongoose.Schema({
    token: {
        type: String,
        required: true,
        unique: true
    },
    revokedAt: {
        type: Date,
        default: Date.now,
        expires: '30d' // Automatically delete documents after 30 days
    }
});

module.exports = mongoose.model('RevokedToken', revokedTokenSchema);
