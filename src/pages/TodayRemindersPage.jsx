import React, { useState, useEffect } from 'react';
import ReminderItem from '../components/ReminderItem';
import ReminderDetails from '../components/ReminderDetails';
import { useNavigate } from 'react-router-dom';
import SearchBar from '../components/SearchBar';
import DatePicker from '../components/DatePicker';
import { useTranslation } from 'react-i18next';
import Button from 'react-bootstrap/Button';
import Dropdown from 'react-bootstrap/Dropdown';
import DropdownButton from 'react-bootstrap/DropdownButton';
import { changeLanguage } from 'i18next';
import { toast } from 'react-toastify';
import '../App.css';

const TodayRemindersPage = ({ reminders }) => {
    const navigate = useNavigate();
    const [showDetails, setShowDetails] = useState(false);
    const [selectedReminder, setSelectedReminder] = useState(null);
    const [query, setQuery] = useState('');
    const [selectedDate, setSelectedDate] = useState(new Date().toISOString().slice(0, 10));
    const { t, i18n } = useTranslation();
    const [triggeredReminders, setTriggeredReminders] = useState(new Set()); // 用来追踪已触发的提醒

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

    useEffect(() => {
        const interval = setInterval(() => {
            const currentTime = new Date();
    
            sortedReminderItems.forEach((reminder) => {
                const reminderTime = new Date(`${selectedDate}T${reminder.time}:00`);
    
                // 如果当前时间等于提醒时间，且该提醒尚未完成，且尚未触发过
                if (
                    currentTime >= reminderTime &&
                    !reminder.completed &&
                    !triggeredReminders.has(reminder.reminderId) &&
                    reminderTime > currentTime // 确保提醒时间在当前时间之后才触发
                ) {
                    // 显示Toast并将提醒标记为已触发
                    toast.info(`${reminder.name} - Time: ${reminder.time} - Dosage: ${reminder.dosage}`, {
                        autoClose: false, // 禁止自动关闭
                        closeButton: true, // 允许用户手动关闭
                    });
    
                    // 更新已触发的提醒状态
                    setTriggeredReminders((prev) => new Set(prev).add(reminder.reminderId));
                }
            });
        }, 1000); // 每秒钟检查一次
    
        // 清除定时器
        return () => clearInterval(interval);
    }, [selectedDate, sortedReminderItems, triggeredReminders]);
    

    return (
        <div className="centered-container">
            {/* 1. 在Pill Reminders的行下方添加空白 */}
            <div
                style={{
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'space-between',
                    width: '100%',
                    marginBottom: '20px',
                }}
            >
                <h3 style={{ margin: 0 }}>{t('Pill Reminders')}</h3>
                
                <Dropdown align="end">
                    <Dropdown.Toggle
                        as="img"
                        src="src/assets/lng.png"
                        alt="Language Selector"
                        style={{ width: '24px', height: '24px', cursor: 'pointer' }}
                    />
                    <Dropdown.Menu style={{ minWidth: 'auto', width: 'auto', padding: '0' }}>
                        <Dropdown.Item
                            eventKey="en"
                            onClick={() => changeLanguage('en')}
                            className="custom-dropdown-item"
                        >
                            EN
                        </Dropdown.Item>
                        <Dropdown.Item
                            eventKey="fi"
                            onClick={() => changeLanguage('fi')}
                            className="custom-dropdown-item"
                        >
                            FI
                        </Dropdown.Item>
                    </Dropdown.Menu>
                </Dropdown>
            </div>

            <SearchBar onSearch={setQuery} style={{ marginBottom: '20px' }} />

            <div style={{ display: 'flex', justifyContent: 'flex-start', marginBottom: '20px' }}>
                <DatePicker 
                    onDateChange={handleDateChange} 
                    initialDate={selectedDate} 
                    style={{ width: '35%' }}  // 在这里传递了新的样式
                />
            </div>

            {/* 显示提醒条目 */}
            {sortedReminderItems.length === 0 ? (
                <p>{t('No reminders for')} {selectedDate}.</p>
            ) : (
                sortedReminderItems.map((reminder, index) => (
                    <ReminderItem
                        key={reminder.reminderId} // 使用唯一的 reminderId 作为 key
                        reminder={reminder}
                        onToggleComplete={() => handleToggleComplete(reminder.reminderId)} // 传递唯一 id 给事件
                        onItemClick={() => handleItemClick(reminder)}
                    />
                ))
            )}

            {/* 3. 新提醒按钮上方添加空白，并使按钮占一行 */}
            <div style={{ marginTop: '20px' }}>
                <Button variant="danger" style={{ width: '100%' }} onClick={() => navigate('/create-reminder')}>
                    {t('+ New Reminder')}
                </Button>
            </div>

            {/* 显示提醒详情 */}
            {showDetails && <ReminderDetails reminder={selectedReminder} onClose={handleClose} />}
        </div>
    );
};

export default TodayRemindersPage;
