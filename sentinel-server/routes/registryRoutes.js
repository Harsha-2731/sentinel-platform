const express = require('express');
const router = express.Router();
const TrustedAgent = require('../models/TrustedAgent');

/**
 * GET /api/registry/list
 * List all trusted agents.
 */
router.get('/list', async (req, res) => {
    try {
        const agents = await TrustedAgent.find().sort({ createdAt: -1 });
        res.json(agents);
    } catch (err) {
        res.status(500).json({ message: err.message });
    }
});

/**
 * POST /api/registry/register
 * Add a new trusted agent to the registry.
 * (In a production app, this would be protected by an Admin API Key)
 */
router.post('/register', async (req, res) => {
    const { agentId, publicKey, allowedActions } = req.body;

    if (!agentId || !publicKey) {
        return res.status(400).json({ message: "agentId and publicKey are required." });
    }

    try {
        // Check if already exists
        let agent = await TrustedAgent.findOne({ agentId });
        if (agent) {
            return res.status(400).json({ message: "Agent ID already exists in registry." });
        }

        agent = new TrustedAgent({
            agentId,
            publicKey,
            allowedActions: allowedActions || []
        });

        await agent.save();
        res.status(201).json({ message: "Agent successfully registered.", agent });
    } catch (err) {
        res.status(500).json({ message: err.message });
    }
});

/**
 * DELETE /api/registry/:id
 * Remove an agent from the trusted registry.
 */
router.delete('/:id', async (req, res) => {
    try {
        const agent = await TrustedAgent.findByIdAndDelete(req.params.id);
        if (!agent) {
            return res.status(404).json({ message: "Agent not found." });
        }
        res.json({ message: "Agent removed from registry." });
    } catch (err) {
        res.status(500).json({ message: err.message });
    }
});

module.exports = router;
