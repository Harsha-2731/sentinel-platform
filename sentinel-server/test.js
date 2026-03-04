const express = require('express');
const app = express();
const path = require('path');
const fs = require('fs');

const file = path.join(__dirname, 'public', 'dashboard.html');
console.log('File path:', file);
console.log('Exists:', fs.existsSync(file));

app.get('/dashboard', (req, res) => {
    res.sendFile(file, (err) => {
        if (err) {
            console.error('Send file error:', err);
            res.status(500).send('Error');
        }
    });
});
app.listen(3002, () => console.log('Listening on 3002'));
