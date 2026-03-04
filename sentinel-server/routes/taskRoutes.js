const express = require('express');
const router = express.Router();
const { storeTask, getTasks } = require('../controllers/taskController');
const { protect } = require('../middleware/auth');

router.post('/', protect, storeTask);
router.get('/', protect, getTasks);

module.exports = router;
