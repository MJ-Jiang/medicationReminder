import React, { useState } from 'react';
import ReminderItem from '../components/ReminderItem';
import ReminderDetails from '../components/ReminderDetails';
import { useNavigate } from 'react-router-dom';
import SearchBar from '../components/SearchBar';
import DatePicker from '../components/DatePicker';
import { useTranslation } from 'react-i18next';

const TodayRemindersPage = ({ reminders }) => {
    const navigate = useNavigate();
    const [showDetails, setShowDetails] = useState(false);
    const [selectedReminder, setSelectedReminder] = useState(null);
    const [query, setQuery] = useState('');
    const [selectedDate, setSelectedDate] = useState(new Date().toISOString().slice(0, 10)); 
    const { t, i18n } = useTranslation();

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
            reminder.times.forEach((time, timeIndex) => {
                // 为每个提醒项生成唯一的 id
                const reminderId = `${reminder.name}-${reminder.startDate}-${time}-${timeIndex}`;
                acc[reminderId] = false; // 默认未完成
            });
            return acc;
        }, {})
    );

    // 切换完成状态的函数
    const handleToggleComplete = (reminderId) => {
        setCompletedReminders((prev) => {
            const newCompleted = { ...prev };
            newCompleted[reminderId] = !newCompleted[reminderId]; // 根据唯一 id 更新状态
            return newCompleted;
        });
    };

    const handleItemClick = (reminder) => {
        setSelectedReminder({
            ...reminder,
            startDate: reminder.originalStartDate || reminder.startDate,
            endDate: reminder.originalEndDate || reminder.endDate,
        });
        setShowDetails(true); 
    };

    const handleClose = () => {
        setShowDetails(false);
        setSelectedReminder(null); 
    };

    const handleDateChange = (newDate) => {
        setSelectedDate(newDate);
    };

    // 提取所有提醒项，并按时间排序
    const allReminderItems = filteredReminders.flatMap((reminder) => 
        reminder.times.map((time, timeIndex) => {
            const reminderId = `${reminder.name}-${reminder.startDate}-${time}-${timeIndex}`;
            return {
                ...reminder, 
                time, 
                completed: completedReminders[reminderId], // 使用唯一 id 获取状态
                reminderId, // 为每个提醒项添加唯一的 id
            };
        })
    );

    // 按时间排序
    const sortedReminderItems = allReminderItems.sort((a, b) => {
        const timeA = new Date(`${selectedDate}T${a.time}:00`);
        const timeB = new Date(`${selectedDate}T${b.time}:00`);
        return timeA - timeB; 
    });

    return (
        <div>
            <h1>Pill Reminders</h1>
            <div style={{ position: 'absolute', top: 20, right: 20 }}>
                <DatePicker onDateChange={handleDateChange} initialDate={selectedDate} />
            </div>
            <SearchBar onSearch={setQuery} />
            {sortedReminderItems.length === 0 ? (
                <p>No reminders for {selectedDate}.</p>  
            ) : (
                sortedReminderItems.map((reminder, index) => (
                    <ReminderItem
                        key={reminder.reminderId}  // 使用唯一的 reminderId 作为 key
                        reminder={reminder}
                        onToggleComplete={() => handleToggleComplete(reminder.reminderId)} // 传递唯一 id 给事件
                        onItemClick={() => handleItemClick(reminder)} 
                    />
                ))
            )}
            <button onClick={() => navigate('/create-reminder')}>{t('+ New Reminder')}</button>
            {showDetails && <ReminderDetails reminder={selectedReminder} onClose={handleClose} />}
        </div>
    );
};

export default TodayRemindersPage;
