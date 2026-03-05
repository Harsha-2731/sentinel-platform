const Device = require('../models/Device');

// @desc    Register a new device or update existing on app launch
// @route   POST /api/device/register
// @access  Public (from mobile agent)
const registerDevice = async (req, res) => {
    const { userEmail, deviceId, deviceName, model } = req.body;

    if (!userEmail || !deviceId || !deviceName) {
        return res.status(400).json({ message: 'Missing required fields' });
    }

    try {
        let device = await Device.findOne({ deviceId });

        if (device) {
            // Update an existing device record
            device.userEmail = userEmail;
            device.deviceName = deviceName;
            device.model = model;
            device.lastSeen = Date.now();
            await device.save();
            return res.status(200).json({ message: 'Device re-registered successfully', deviceId });
        } else {
            // Create a completely new device binding
            device = await Device.create({
                userEmail,
                deviceId,
                deviceName,
                model
            });
            return res.status(201).json({ message: 'Device registered successfully', deviceId });
        }
    } catch (error) {
        res.status(500).json({ message: error.message });
    }
};

// @desc    Receive periodic heartbeat telemetry from device
// @route   POST /api/device/heartbeat
// @access  Public (from mobile agent)
const receiveHeartbeat = async (req, res) => {
    const { deviceId, battery, securityScore, status, latitude, longitude } = req.body;

    if (!deviceId) return res.status(400).json({ message: 'deviceId is required' });

    try {
        const device = await Device.findOne({ deviceId });

        if (!device) {
            return res.status(404).json({ message: 'Device not found in registry' });
        }

        device.battery = battery !== undefined ? battery : device.battery;
        device.securityScore = securityScore !== undefined ? securityScore : device.securityScore;
        device.status = status || device.status;
        device.latitude = latitude !== undefined ? latitude : device.latitude;
        device.longitude = longitude !== undefined ? longitude : device.longitude;
        device.lastSeen = Date.now();

        await device.save();

        res.status(200).json({ message: 'Heartbeat acknowledged' });
    } catch (error) {
        res.status(500).json({ message: error.message });
    }
};

// @desc    Fetch device telemetry for the Web Dashboard based on User Email
// @route   GET /api/device/info?email=...
// @access  Protected
const getDeviceInfo = async (req, res) => {
    const { email } = req.query;

    if (!email) return res.status(400).json({ message: 'User email is required' });

    try {
        const device = await Device.findOne({ userEmail: email });

        if (!device) {
            return res.status(404).json({ message: 'No registered device bound to this email' });
        }

        res.status(200).json(device);
    } catch (error) {
        res.status(500).json({ message: error.message });
    }
};

module.exports = {
    registerDevice,
    receiveHeartbeat,
    getDeviceInfo
};
