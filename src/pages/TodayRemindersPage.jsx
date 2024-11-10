import React, { useState } from 'react';
import ReminderItem from '../components/ReminderItem';
import ReminderDetails from '../components/ReminderDetails';
import { useNavigate } from 'react-router-dom';
import SearchBar from '../components/SearchBar';
import DatePicker from '../components/DatePicker';


const TodayRemindersPage = ({ reminders }) => {
    const navigate = useNavigate();
    const [showDetails, setShowDetails] = useState(false);
    const [selectedReminder, setSelectedReminder] = useState(null);
    const [query, setQuery] = useState(''); // 使用 query 和 setQuery
    const [selectedDate, setSelectedDate] = useState(new Date().toISOString().slice(0, 10)); 

    // 根据搜索查询过滤提醒
    const filteredReminders = reminders.filter((reminder) => {
        const currentDate = new Date(selectedDate);
        const reminderStartDate = new Date(reminder.startDate);
        const reminderEndDate = new Date(reminder.endDate);
    
        return reminderStartDate <= currentDate && reminderEndDate >= currentDate;
    });
    
    

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

    const handleItemClick = (reminder) => {
        setSelectedReminder(reminder);
        setShowDetails(true); // 显示弹窗
    };

    const handleClose = () => {
        setShowDetails(false);
        setSelectedReminder(null); // 清空选择的提醒
    };

    const handleDateChange = (newDate) => {
        setSelectedDate(newDate);
    };
    return (
        <div>
            <h1>Today's Reminders</h1>
            <div style={{ position: 'absolute', top: 20, right: 20 }}>
                <DatePicker onDateChange={handleDateChange} initialDate={selectedDate} /> {/* 日期选择器 */}
            </div>
            <SearchBar onSearch={setQuery} /> {/* 保持传入 setQuery */}
            {filteredReminders.length === 0 ? (
                <p>No reminders for {selectedDate}.</p>  
            ) : (
                filteredReminders.map((reminder) =>
                    reminder.times.map((time, timeIndex) => (
                        <ReminderItem
                            key={`${reminder.name}-${timeIndex}`}
                            reminder={{ ...reminder, time, completed: completedReminders[reminder.name]?.[timeIndex] }}
                            onToggleComplete={() => handleToggleComplete(reminder.name, timeIndex)} // 切换复选框状态
                            onItemClick={() => handleItemClick(reminder)} // 点击项时显示弹窗
                        />
                    ))
                )
            )}
            <button onClick={() => navigate('/create-reminder')}>+ New Reminder</button>
            {showDetails && <ReminderDetails reminder={selectedReminder} onClose={handleClose} />}
        </div>
    );
};

export default TodayRemindersPage;
