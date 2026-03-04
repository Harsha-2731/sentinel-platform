const mongoose = require('mongoose');

const connectDB = async () => {
    try {
        const conn = await mongoose.connect(process.env.MONGODB_URI);
        console.log(`MongoDB Connected: ${conn.connection.host}`);
    } catch (error) {
        console.error(`[CRITICAL] MongoDB Connection Failed: ${error.message}`);
        console.warn('Backend is running in DEGRADED MODE (Database Offline)');
    }
};

module.exports = connectDB;
