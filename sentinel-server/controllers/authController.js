const User = require('../models/User');
const RevokedToken = require('../models/RevokedToken');
const jwt = require('jsonwebtoken');
const bcrypt = require('bcrypt');
const crypto = require('crypto');

const generateToken = (id) => {
    return jwt.sign({ id }, process.env.JWT_SECRET, {
        expiresIn: '15m',
    });
};

const registerUser = async (req, res) => {
    const { name, email, password, deviceId, trustedContacts, riskPreference } = req.body;
    console.log(`[AUTH] Incoming Registration Attempt: ${email} (Device: ${deviceId})`);

    try {
        const userExists = await User.findOne({ email });

        if (userExists) {
            return res.status(400).json({ message: 'User already exists' });
        }

        const salt = await bcrypt.genSalt(10);
        const hashedPassword = await bcrypt.hash(password, salt);

        const hmacSecret = crypto.randomBytes(32).toString('hex');

        const user = await User.create({
            name,
            email,
            password: hashedPassword,
            deviceId,
            hmacSecret,
            trustedContacts: trustedContacts || [],
            riskPreference: riskPreference || 'medium',
        });

        if (user) {
            res.status(201).json({
                _id: user.id,
                name: user.name,
                email: user.email,
                token: generateToken(user._id),
                hmacSecret: user.hmacSecret
            });
        } else {
            res.status(400).json({ message: 'Invalid user data' });
        }
    } catch (error) {
        res.status(500).json({ message: error.message });
    }
};

const loginUser = async (req, res) => {
    const { email, password, deviceId } = req.body;
    console.log(`[AUTH] Incoming Login Attempt: ${email} (Device: ${deviceId})`);

    try {
        const user = await User.findOne({ email });

        if (user && (await bcrypt.compare(password, user.password))) {

            // Advanced Network Hardening: Single-session Device Binding
            if (user.deviceId !== deviceId) {
                return res.status(403).json({ message: 'Session conflict: This account is bound to a different device.' });
            }

            // Generate new session HMAC secret
            const hmacSecret = crypto.randomBytes(32).toString('hex');
            user.hmacSecret = hmacSecret;
            await user.save();

            res.json({
                _id: user.id,
                name: user.name,
                email: user.email,
                token: generateToken(user._id),
                hmacSecret: user.hmacSecret
            });
        } else {
            res.status(401).json({ message: 'Invalid email or password' });
        }
    } catch (error) {
        res.status(500).json({ message: error.message });
    }
};

const revokeToken = async (req, res) => {
    try {
        let token = req.headers.authorization.split(' ')[1];

        // V3.3: Idempotency check 
        const alreadyRevoked = await RevokedToken.findOne({ token });
        if (alreadyRevoked) {
            return res.status(200).json({ message: 'Token was already revoked.' });
        }

        await RevokedToken.create({ token });
        res.status(200).json({ message: 'Token successfully revoked and session blacklisted.' });
    } catch (error) {
        res.status(500).json({ message: error.message });
    }
};

module.exports = {
    registerUser,
    loginUser,
    revokeToken
};
