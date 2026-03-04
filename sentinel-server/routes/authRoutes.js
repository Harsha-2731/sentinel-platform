const express = require('express');
const router = express.Router();
const { registerUser, loginUser, revokeToken } = require('../controllers/authController');
const { protect } = require('../middleware/auth');

router.post('/register', registerUser);
router.post('/login', loginUser);
router.post('/revoke', protect, revokeToken);

module.exports = router;
