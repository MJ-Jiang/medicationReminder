import React, { useState } from 'react';
import ReminderForm from '../components/ReminderForm';
import ReminderDetails from '../components/ReminderDetails';
import SearchBar from '../components/SearchBar';


const CreateReminderPage = ({ onAddReminder }) => {
    const [showDetails, setShowDetails] = useState(false);
    const [reminder, setReminder] = useState(null);
    const [query, setQuery] = useState(''); 

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
            <SearchBar onSearch={setQuery} /> {/* 传递 setQuery 给 SearchBar */}
            
            <ReminderForm onAddReminder={handleAddReminder} />
            {showDetails && <ReminderDetails reminder={reminder} onClose={handleClose} />}
        </div>
    );
};

export default CreateReminderPage;
