import React from 'react';
import '../App.css'; 

const ReminderItem = ({ reminder, onToggleComplete, onItemClick, isDateChanged }) => {
    return (
        <div className={`reminder-item ${reminder.completed ? 'completed' : ''} ${isDateChanged ? 'flash' : ''}`}>
            <div
                onClick={onItemClick} // 点击条目导航到详细信息
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
                checked={reminder.completed || false} // 如果未定义则默认为 false
                onChange={() => onToggleComplete(reminder.reminderId)} // 切换复选框
            />
        </div>
    );
};

export default ReminderItem;
