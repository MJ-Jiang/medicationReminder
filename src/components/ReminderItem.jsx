{/**Displays individual reminders, show as lists on the TodayRemindersPage */}
import React from 'react';

const ReminderItem = ({ reminder, onToggle }) => {
    return (
        <div style={{ display: 'flex', alignItems: 'center', margin: '5px 0' }}>
            <input
                type="checkbox"
                onChange={() => onToggle(reminder.name)} // Trigger the toggle function on change
            />
            <div style={{ marginLeft: '10px' }}>
                <strong>{reminder.name}</strong> - {reminder.dosage} 
                <br />
                Time: {reminder.time}
            </div>
        </div>
    );
};

export default ReminderItem;
