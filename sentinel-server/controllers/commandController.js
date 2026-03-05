const Command = require('../models/Command');

// @desc    Issue a new remote command to a specific device
// @route   POST /api/commands/issue
// @access  Protected (from Web Dashboard)
exports.issueCommand = async (req, res) => {
    try {
        const { action, targetDeviceId } = req.body;

        // Ensure the requester is trying to control a device they own (or specified one)
        const target = targetDeviceId || req.user.deviceId;

        if (!target) {
            return res.status(400).json({ success: false, message: "No device ID provided or bound to this account." });
        }

        console.log(`[Dashboard] Issuing command: ${action} to device: ${target}`);

        const newCommand = await Command.create({
            commandId: "CMD_" + Date.now().toString(),
            deviceId: target,
            commandType: action,
            status: 'PENDING'
        });

        res.status(200).json({ success: true, message: `Command ${action} queued for your device.`, commandId: newCommand.commandId });
    } catch (error) {
        console.error("Error issuing command:", error);
        res.status(500).json({ success: false, message: "Server Error" });
    }
};

// @desc    Poll for pending commands
// @route   GET /api/commands/pending
// @access  Protected (from Android App)
exports.getPendingCommands = async (req, res) => {
    try {
        // App passes deviceId explicitly or we get it from token
        const deviceId = req.query.deviceId || req.user.deviceId;

        if (!deviceId) {
            return res.status(400).json({ success: false, message: "Device ID required" });
        }

        // Find all commands for this device that are still PENDING
        const pendingCommands = await Command.find({ deviceId: deviceId, status: 'PENDING' });

        if (pendingCommands.length > 0) {
            console.log(`[Agent] Consuming ${pendingCommands.length} command(s) for device: ${deviceId}`);
            // Do NOT delete them immediately. Wait for the app to acknowledge it completed them.
        }

        // Map them to the old format expected by the app for compatibility
        const formattedCommands = pendingCommands.map(cmd => ({
            id: cmd.commandId,
            action: cmd.commandType,
            targetDevice: cmd.deviceId,
            issuedAt: cmd.timestamp
        }));

        res.status(200).json({ success: true, commands: formattedCommands });
    } catch (error) {
        console.error("Error fetching pending commands:", error);
        res.status(500).json({ success: false, message: "Server Error" });
    }
};

// @desc    Acknowledge a command was executed
// @route   POST /api/commands/complete
// @access  Protected (from Android App)
exports.completeCommand = async (req, res) => {
    try {
        const { commandId } = req.body;

        if (!commandId) {
            return res.status(400).json({ success: false, message: "Command ID required" });
        }

        const command = await Command.findOne({ commandId });

        if (!command) {
            return res.status(404).json({ success: false, message: "Command not found" });
        }

        command.status = 'EXECUTED';
        await command.save();

        console.log(`[Agent] Command ${commandId} successfully executed by device.`);
        res.status(200).json({ success: true, message: "Command marked as executed" });
    } catch (error) {
        console.error("Error completing command:", error);
        res.status(500).json({ success: false, message: "Server Error" });
    }
};
