const express = require('express');
const router = express.Router();
const commandController = require('../controllers/commandController');
const { protect } = require('../middleware/auth');

// Note: In a real prod app, the dashboard would also be protected by auth.
// Here we temporarily leave issue open for the PoC, or we can just protect the polling endpoint.

// Dashboard requests a command (Remote Lock, Wipe)
router.post('/issue', commandController.issueCommand);

// Android device polls for commands
router.get('/pending', protect, commandController.getPendingCommands);

module.exports = router;
