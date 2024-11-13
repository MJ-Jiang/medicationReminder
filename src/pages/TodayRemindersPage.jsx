// TodayRemindersPage.jsx
import React, { useRef, useState, useEffect } from 'react';
import ReminderItem from '../components/ReminderItem';
import ReminderDetails from '../components/ReminderDetails';
import { useNavigate } from 'react-router-dom';
import SearchBar from '../components/SearchBar';
import DatePicker from '../components/DatePicker';
import { useTranslation } from 'react-i18next';
import Button from 'react-bootstrap/Button';
import LanguageSelector from '../components/LanguageSelector';  // 导入新的语言选择组件
import { toast, ToastContainer } from 'react-toastify';  // 导入 toast 和 ToastContainer
import 'react-toastify/dist/ReactToastify.css';  // 引入样式
import '../App.css';

const TodayRemindersPage = () => {
    const navigate = useNavigate();
    const [showDetails, setShowDetails] = useState(false);
    const [selectedReminder, setSelectedReminder] = useState(null);
    const [query, setQuery] = useState('');
    const [selectedDate, setSelectedDate] = useState(new Date().toISOString().slice(0, 10)); // 默认当天
    const { t, i18n } = useTranslation();
    const [reminders, setReminders] = useState([]); // 所有提醒
    const [completedReminders, setCompletedReminders] = useState({}); // 记录提醒是否完成
    const triggeredRemindersRef = useRef(new Set()); // 使用ref来避免每次重新赋值

    // 从 localStorage 读取提醒数据
    useEffect(() => {
        const reminderData = JSON.parse(localStorage.getItem('reminderData')) || [];
        setReminders(reminderData);

        // 初始化 completedReminders 状态
        const initialCompleted = reminderData.reduce((acc, reminder) => {
            reminder.times.forEach((time, timeIndex) => {
                const reminderId = `${reminder.name}-${reminder.startDate}-${time}-${timeIndex}`;
                acc[reminderId] = false; // 默认为未完成
            });
            return acc;
        }, {});
        setCompletedReminders(initialCompleted);
    }, []);

    // 监听 localStorage 变化，确保新提醒添加后页面更新
    useEffect(() => {
        const handleStorageChange = () => {
            const reminderData = JSON.parse(localStorage.getItem('reminderData')) || [];
            setReminders(reminderData);
        };

        window.addEventListener('storage', handleStorageChange);

        return () => {
            window.removeEventListener('storage', handleStorageChange);
        };
    }, []);

    // 过滤当天的提醒
    const filteredReminders = reminders.filter((reminder) => {
        const currentDate = new Date(selectedDate);
        const reminderStartDate = new Date(reminder.startDate);
        const reminderEndDate = new Date(reminder.endDate);

        return reminderStartDate <= currentDate && reminderEndDate >= currentDate;
    });

    // 提取所有提醒项，并按时间排序
    const allReminderItems = filteredReminders.flatMap((reminder) =>
        reminder.times.map((time, timeIndex) => {
            const reminderId = `${reminder.name}-${reminder.startDate}-${time}-${timeIndex}`;
            return {
                ...reminder,
                time,
                reminderId,
                completed: completedReminders[reminderId], // 添加completed状态
            };
        })
    );

    // 按时间排序
    const sortedReminderItems = allReminderItems.sort((a, b) => {
        const timeA = new Date(`${selectedDate}T${a.time}:00`);
        const timeB = new Date(`${selectedDate}T${b.time}:00`);
        return timeA - timeB;
    });

    // 每秒检查提醒
    useEffect(() => {
        const interval = setInterval(() => {
            const currentTime = new Date();
            
            sortedReminderItems.forEach((reminder) => {
                const reminderTime = new Date(`${selectedDate}T${reminder.time}:00`);

                // 检查是否已经触发过提醒，并且提醒未被标记为已完成
                if (currentTime >= reminderTime && !triggeredRemindersRef.current.has(reminder.reminderId) && !reminder.completed) {
                    triggeredRemindersRef.current.add(reminder.reminderId);  // 使用ref中的Set避免重新赋值
                    toast.info(`Time to take: ${reminder.name}`, {
                        autoClose: false,  // 不自动关闭
                    });
                }
            });
        }, 1000); // 每秒检查一次

        return () => clearInterval(interval); // 清除定时器
    }, [sortedReminderItems, selectedDate, completedReminders]);

    // 切换提醒的完成状态
    const handleToggleComplete = (reminderId) => {
        setCompletedReminders((prev) => {
            const newCompleted = { ...prev };
            newCompleted[reminderId] = !newCompleted[reminderId];
            return newCompleted;
        });
    };

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

                <LanguageSelector />  {/* 引入新的 LanguageSelector 组件 */}
            </div>

            <SearchBar onSearch={setQuery} style={{ marginBottom: '20px' }} />

            <div style={{ display: 'flex', justifyContent: 'flex-start', marginBottom: '20px' }}>
                <DatePicker 
                    onDateChange={(newDate) => setSelectedDate(newDate)} 
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
                        onItemClick={() => setSelectedReminder(reminder)} // 显示提醒详情
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
            {showDetails && <ReminderDetails reminder={selectedReminder} onClose={() => setShowDetails(false)} />}

            {/* ToastContainer 显示 Toast */}
            <ToastContainer />
        </div>
    );
};

export default TodayRemindersPage;
