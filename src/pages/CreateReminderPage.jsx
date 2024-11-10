import React, { useState } from 'react';
import ReminderForm from '../components/ReminderForm';
import ReminderDetails from '../components/ReminderDetails';
import SearchBar from '../components/SearchBar';
import BackButton from '../components/BackButton';

const CreateReminderPage = ({ onAddReminder }) => {
    const [showDetails, setShowDetails] = useState(false);
    const [reminder, setReminder] = useState(null);
    const [query, setQuery] = useState('');

    const handleAddReminder = (newReminder) => {
        const { startDate, endDate, frequency, times } = newReminder;
        const remindersToAdd = [];
        
        // 保留原始的开始和结束日期
        const originalStartDate = new Date(startDate).toISOString().slice(0, 10);
        const originalEndDate = new Date(endDate).toISOString().slice(0, 10);
    
        // 根据频率生成提醒
        if (frequency === 'daily') {
            let currentDate = new Date(startDate);
            const endDateObj = new Date(endDate);
    
            while (currentDate <= endDateObj) {
                remindersToAdd.push({
                    ...newReminder,
                    startDate: currentDate.toISOString().slice(0, 10), // 这里是单日的startDate
                    endDate: currentDate.toISOString().slice(0, 10),
                    times: times,
                    originalStartDate, // 添加原始开始日期
                    originalEndDate, 
                });
                currentDate.setDate(currentDate.getDate() + 1); // 每日增加
            }
        } else if (frequency === 'weekly') {
            let currentDate = new Date(startDate);
            const endDateObj = new Date(endDate);
    
            while (currentDate <= endDateObj) {
                remindersToAdd.push({
                    ...newReminder,
                    startDate: currentDate.toISOString().slice(0, 10),
                    endDate: currentDate.toISOString().slice(0, 10),
                    times: times,
                    originalStartDate, // 添加原始开始日期
                    originalEndDate, 
                });
                currentDate.setDate(currentDate.getDate() + 7); // 每周增加7天
            }
        }
    
    
        // 添加所有提醒到主应用状态
        if (remindersToAdd.length > 0) {
            remindersToAdd.forEach((reminder) => onAddReminder(reminder));
    
            // 保留整个周期的提醒信息传递给详情
            setReminder({
                ...newReminder,
                startDate: originalStartDate,
                endDate: originalEndDate
            });
            setShowDetails(true);
        } else {
            console.log("No reminders were created.");
        }
    };
    

    const handleClose = () => {
        setShowDetails(false);
        setReminder(null); // 关闭时清空提醒
    };

    return (
        <div>
            <BackButton />
            <h1>Create Reminder</h1>
            <SearchBar onSearch={setQuery} /> {/* 传递 setQuery 给 SearchBar */}
            
            <ReminderForm onAddReminder={handleAddReminder} />
            {showDetails && <ReminderDetails reminder={reminder} onClose={handleClose} />}
        </div>
    );
};

export default CreateReminderPage;
