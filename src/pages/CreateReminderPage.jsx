import React, { useState } from 'react';
import ReminderForm from '../components/ReminderForm';
import ReminderDetails from '../components/ReminderDetails';

const CreateReminderPage = ({ onAddReminder }) => {
    const [showDetails, setShowDetails] = useState(false);
    const [reminder, setReminder] = useState(null);

    const handleAddReminder = (newReminder) => {
        onAddReminder(newReminder);
        setReminder(newReminder); // 设置新创建的提醒
        setShowDetails(true); // 显示弹窗
    };

    const handleClose = () => {
        setShowDetails(false);
        setReminder(null); // 关闭时清空提醒
    };

    return (
        <div>
            <h1>Create Reminder</h1>
            <ReminderForm onAddReminder={handleAddReminder} />
            {showDetails && <ReminderDetails reminder={reminder} onClose={handleClose} />}
        </div>
    );
};

export default CreateReminderPage;
