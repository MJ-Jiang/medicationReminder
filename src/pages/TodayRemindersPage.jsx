// TodayRemindersPage.jsx
import React, { useState, useEffect } from 'react';
import ReminderList from '../components/ReminderList'; // 引入新的 ReminderList 组件
import ReminderDetails from '../components/ReminderDetails';
import { useNavigate } from 'react-router-dom';
import SearchBar from '../components/SearchBar';
import DatePicker from '../components/DatePicker';
import { useTranslation } from 'react-i18next';
import Button from 'react-bootstrap/Button';
import LanguageSelector from '../components/LanguageSelector'; // 引入新的语言选择组件
import ReminderToastNotifier from '../components/ReminderToastNotifier'; // 引入 ReminderToastNotifier 组件
import { ToastContainer } from 'react-toastify';  // 引入 ToastContainer 组件
import '../App.css';

const TodayRemindersPage = () => {
  const navigate = useNavigate();
  const { t } = useTranslation();
  const [reminders, setReminders] = useState([]); // 所有提醒
  const [completedReminders, setCompletedReminders] = useState({}); // 记录提醒是否完成
  const [selectedDate, setSelectedDate] = useState(new Date().toISOString().slice(0, 10)); // 默认当天
  const [selectedReminder, setSelectedReminder] = useState(null); // 当前选中的提醒
  const [query, setQuery] = useState('');

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

  // 切换提醒的完成状态
  const handleToggleComplete = (reminderId) => {
    setCompletedReminders((prev) => {
      const newCompleted = { ...prev };
      newCompleted[reminderId] = !newCompleted[reminderId];
      return newCompleted;
    });
  };

  // 显示选中的提醒详情
  const handleItemClick = (reminder) => {
    setSelectedReminder(reminder);
  };

  return (
    <div className="centered-container">
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
        <LanguageSelector /> {/* 引入新的 LanguageSelector 组件 */}
      </div>

      <SearchBar onSearch={setQuery} style={{ marginBottom: '20px' }} />

      <div style={{ display: 'flex', justifyContent: 'flex-start', marginBottom: '20px' }}>
        <DatePicker
          onDateChange={(newDate) => setSelectedDate(newDate)}
          initialDate={selectedDate}
          style={{ width: '35%' }} // 在这里传递了新的样式
        />
      </div>

      {/* 使用 ReminderList 组件显示提醒条目 */}
      <ReminderList
        reminders={reminders}
        selectedDate={selectedDate}
        completedReminders={completedReminders}
        handleToggleComplete={handleToggleComplete}
        onItemClick={handleItemClick} // 将 onItemClick 传递给 ReminderList
      />

      <div style={{ marginTop: '20px' }}>
        <Button variant="danger" style={{ width: '100%' }} onClick={() => navigate('/create-reminder')}>
          {t('+ New Reminder')}
        </Button>
      </div>

      {/* 显示提醒详情 */}
      {selectedReminder && <ReminderDetails reminder={selectedReminder} onClose={() => setSelectedReminder(null)} />}

      {/* 触发提醒的 toast 通知 */}
      <ReminderToastNotifier
        reminders={reminders}
        selectedDate={selectedDate}
        completedReminders={completedReminders}
      />

      <ToastContainer />
    </div>
  );
};

export default TodayRemindersPage;
