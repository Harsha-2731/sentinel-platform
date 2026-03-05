const express = require('express');
const router = express.Router();
const { registerDevice, receiveHeartbeat, getDeviceInfo } = require('../controllers/deviceController');
const { protect } = require('../middleware/auth');

router.post('/register', registerDevice);
router.post('/heartbeat', receiveHeartbeat);
router.get('/info', protect, getDeviceInfo);

module.exports = router;
