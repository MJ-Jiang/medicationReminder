import React, { useState } from 'react';
import ReminderItem from '../components/ReminderItem';
import { useNavigate } from 'react-router-dom';

const TodayRemindersPage = ({ reminders }) => {
    const navigate = useNavigate();
    
    // 维护每个提醒的完成状态
    const [completedReminders, setCompletedReminders] = useState(
        reminders.reduce((acc, reminder) => {
            reminder.times.forEach(() => {
                acc[reminder.name] = acc[reminder.name] || [];
                acc[reminder.name].push(false); // 默认未完成
            });
            return acc;
        }, {})
    );

    // 切换完成状态的函数
    const handleToggleComplete = (reminderName, index) => {
        setCompletedReminders((prev) => {
            const newCompleted = { ...prev };
            newCompleted[reminderName][index] = !newCompleted[reminderName][index];
            return newCompleted;
        });
    };

    return (
        <div>
            <h1>Today's Reminders</h1>
            {reminders.length === 0 ? (
                <p>No reminders for today.</p>
            ) : (
                reminders.map((reminder) =>
                    reminder.times.map((time, timeIndex) => (
                        <ReminderItem
                            key={`${reminder.name}-${timeIndex}`}
                            reminder={{ ...reminder, time, completed: completedReminders[reminder.name]?.[timeIndex] }}
                            onToggleComplete={() => handleToggleComplete(reminder.name, timeIndex)} // 切换复选框状态
                            onItemClick={() => navigate(`/reminder/${reminder.name}`)} // 点击其他地方时导航到详细信息页面
                        />
                    ))
                )
            )}
            <button onClick={() => navigate('/create-reminder')}>+ New Reminder</button>
        </div>
    );
};

export default TodayRemindersPage;
