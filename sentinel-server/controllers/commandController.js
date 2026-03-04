// In-memory array for pending commands (for PoC)
// In production, this would be stored in MongoDB or Redis with a deviceId targeting system.
let pendingCommands = [];

exports.issueCommand = async (req, res) => {
    try {
        const { action, targetDevice } = req.body; // action: 'LOCK_DEVICE', 'WIPE_DATA'

        console.log(`[Dashboard] Issuing command: ${action} to device: ${targetDevice || 'ALL'}`);

        pendingCommands.push({
            id: Date.now().toString(),
            action: action,
            targetDevice: targetDevice,
            issuedAt: new Date()
        });

        res.status(200).json({ success: true, message: `Command ${action} queued successfully.` });
    } catch (error) {
        console.error("Error issuing command:", error);
        res.status(500).json({ success: false, message: "Server Error" });
    }
};

exports.getPendingCommands = async (req, res) => {
    try {
        const deviceId = req.user.deviceId; // From protect middleware

        // Filter commands meant for this device, or general commands if targetDevice is empty
        const commandsForDevice = pendingCommands.filter(c => !c.targetDevice || c.targetDevice === deviceId);

        // We will just send them and then clear them so they aren't processed twice.
        // In prod, Android would specifically ACK the command.
        if (commandsForDevice.length > 0) {
            console.log(`[Agent] Consuming ${commandsForDevice.length} command(s) for ${deviceId}`);
            pendingCommands = pendingCommands.filter(c => c.targetDevice && c.targetDevice !== deviceId);
        }

        res.status(200).json({ success: true, commands: commandsForDevice });
    } catch (error) {
        console.error("Error fetching pending commands:", error);
        res.status(500).json({ success: false, message: "Server Error" });
    }
};
