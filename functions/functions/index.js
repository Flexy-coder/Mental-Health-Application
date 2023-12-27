const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

exports.logUserLogin = functions.analytics.event('user_login').onLog((event) => {
    const eventData = event.data;

    // Extract user ID and timestamp from the event data
    const userId = eventData.user_id;
    const timestamp = eventData.user_login_timestamp;

    // Create an object to store usage data
    const usageData = {
        userId: userId,
        timestamp: timestamp
    };

    // Reference to the Realtime Database
    const db = admin.database();

    // Path to where the data should be stored in the Realtime Database
    const usageStatisticsRef = db.ref('usage_statistics');

    // Push the usage data to the Realtime Database
    return usageStatisticsRef.push(usageData);
});
