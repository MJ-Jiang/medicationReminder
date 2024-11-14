import React from 'react';
import '../App.css'; 

const ReminderItem = ({ reminder, onToggleComplete, onItemClick, isDateChanged }) => {
    return (
        <div className={`reminder-item ${reminder.completed ? 'completed' : ''} ${isDateChanged ? 'flash' : ''}`}>
            <div
                onClick={onItemClick} // Click on an entry to navigate to detailed information
                className="reminder-content"
            >
                <img 
                    src="src/assets/pill.png" 
                    alt="Pill"
                />
              
                <div className="text-content">
                    <span className="name">
                        {reminder.name}
                    </span>
                 
                    <div className="time-dosage">
                        <span>{reminder.time}</span>
                        <span>{reminder.dosage}</span>
                    </div>
                </div>
            </div>
          
            <input
                type="checkbox"
                checked={reminder.completed || false} // If undefined, defaults to false
                onChange={() => onToggleComplete(reminder.reminderId)} // Toggle Checkbox
            />
        </div>
    );
};

export default ReminderItem;
