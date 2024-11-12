import React, { useState } from 'react';
import ReminderForm from '../components/ReminderForm';
import ReminderDetails from '../components/ReminderDetails';
import SearchBar from '../components/SearchBar';
import BackButton from '../components/BackButton';
import { useTranslation } from 'react-i18next';
import { Container, Row, Col } from 'react-bootstrap';
import '../App.css';

const CreateReminderPage = ({ onAddReminder }) => {
    const [showDetails, setShowDetails] = useState(false);
    const [reminder, setReminder] = useState(null);
    const [query, setQuery] = useState('');
    const { t, i18n } = useTranslation();

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
                    id: `${newReminder.name}-${currentDate.toISOString().slice(0, 10)}-${times.join('-')}`,  // 生成唯一 id
                    startDate: currentDate.toISOString().slice(0, 10),
                    endDate: currentDate.toISOString().slice(0, 10),
                    times: times,
                    originalStartDate, 
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
                    id: `${newReminder.name}-${currentDate.toISOString().slice(0, 10)}-${times.join('-')}`,  // 生成唯一 id
                    startDate: currentDate.toISOString().slice(0, 10),
                    endDate: currentDate.toISOString().slice(0, 10),
                    times: times,
                    originalStartDate, 
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
        <div className="centered-container">
            <BackButton />
            <h1>New Pill Reminder</h1>
            <SearchBar onSearch={setQuery} />
            <ReminderForm onAddReminder={handleAddReminder} />
            {showDetails && <ReminderDetails reminder={reminder} onClose={handleClose} />}
        </div>
    );
};

export default CreateReminderPage;
