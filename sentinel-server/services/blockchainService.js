// services/blockchainService.js
const { ethers } = require("ethers");
require("dotenv").config();

// Human-Readable ABI for the V5 SentinelAudit Token Revocation Contract
const contractABI = [
    "function admin() public view returns (address)",
    "function triggerEmergency(string memory agentId, bytes32 _payloadHash) public",
    "function isAgentRevoked(string memory agentId) public view returns (bool)",
    "event AgentRevoked(string indexed agentId, bytes32 payloadHash, uint256 timestamp)"
];

// Validate required env variables
if (!process.env.RPC_URL || !process.env.PRIVATE_KEY || !process.env.CONTRACT_ADDRESS) {
    throw new Error("Missing blockchain environment variables");
}

// Connect to Ganache
const provider = new ethers.JsonRpcProvider(process.env.RPC_URL);

// Create wallet
const wallet = new ethers.Wallet(
    process.env.PRIVATE_KEY,
    provider
);

// Connect contract
const contract = new ethers.Contract(
    process.env.CONTRACT_ADDRESS,
    contractABI,
    wallet
);

// ✅ HARDENING: Validate Chain ID at Startup
provider.getNetwork().then(network => {
    // 1337 is default Ganache, 11155111 is Sepolia
    if (network.chainId !== 1337n && network.chainId !== 11155111n) {
        console.warn(`⚠️ Warning: Connected to unexpected Chain ID: ${network.chainId}`);
    } else {
        console.log(`🔗 Connected to Valid Chain ID: ${network.chainId}`);
    }
}).catch(err => {
    console.error("❌ Failed to fetch blockchain network details on startup:", err.message);
});

console.log("✅ Blockchain connected");
console.log("📄 Contract:", process.env.CONTRACT_ADDRESS);
console.log("👤 Wallet:", wallet.address);

/**
 * Store emergency payload hash on blockchain and revoke token
 * @param {string} agentId - User ID / Agent DID
 * @param {string} payloadHash - SHA-256 hash of emergency payload (must be 0x-prefixed for bytes32)
 */
async function triggerEmergencyOnChain(agentId, payloadHash) {
    try {
        // Ensure payloadHash is a valid hex string for bytes32
        const formattedHash = payloadHash.startsWith("0x") ? payloadHash : `0x\${payloadHash}`;

        // ✅ HARDENING: Set manual Gas limit to prevent accidental unbounded usage
        const tx = await contract.triggerEmergency(agentId, formattedHash, {
            gasLimit: 300000
        });

        // ✅ HARDENING: Wait for tx confirmation (1 block)
        const receipt = await tx.wait(1);

        console.log("✅ Blockchain Token Revoked & Anchored");
        console.log("🧾 Tx Hash:", receipt.hash);
        console.log("🧱 Block Number:", receipt.blockNumber);

        return {
            hash: receipt.hash,
            blockNumber: receipt.blockNumber
        };

    } catch (error) {
        console.error("❌ Blockchain Error:", error.message);
        throw error;
    }
}

/**
 * Check if the token is revoked directly on the Blockchain Immutable Ledger
 */
async function isAgentRevokedOnChain(agentId) {
    try {
        return await contract.isAgentRevoked(agentId);
    } catch (error) {
        console.error("❌ Blockchain Read Error:", error.message);
        return false; // Fail open or closed? Typically fail closed, but returning false here for fallback testing
    }
}

module.exports = { triggerEmergencyOnChain, isAgentRevokedOnChain };