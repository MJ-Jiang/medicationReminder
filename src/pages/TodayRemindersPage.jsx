
import React, { useState, useEffect } from 'react';
import ReminderList from '../components/ReminderList'; 
import ReminderDetails from '../components/ReminderDetails';
import { useNavigate } from 'react-router-dom';
import SearchBar from '../components/SearchBar';
import DatePicker from '../components/DatePicker';
import { useTranslation } from 'react-i18next';
import Button from 'react-bootstrap/Button';
import LanguageSelector from '../components/LanguageSelector'; 
import ReminderToastNotifier from '../components/ReminderToastNotifier'; 
import { ToastContainer } from 'react-toastify';  
import '../App.css';

const TodayRemindersPage = () => {
  const navigate = useNavigate();
  const { t } = useTranslation();
  const [reminders, setReminders] = useState([]); 
  const [completedReminders, setCompletedReminders] = useState({}); 
  const [selectedDate, setSelectedDate] = useState(new Date().toISOString().slice(0, 10)); 
  const [selectedReminder, setSelectedReminder] = useState(null); 
  const [query, setQuery] = useState('');

  // Read reminder data from localStorage
  useEffect(() => {
    const reminderData = JSON.parse(localStorage.getItem('reminderData')) || [];
    setReminders(reminderData);

    // Initialize the completedReminders state
    const initialCompleted = reminderData.reduce((acc, reminder) => {
      reminder.times.forEach((time, timeIndex) => {
        const reminderId = `${reminder.name}-${reminder.startDate}-${time}-${timeIndex}`;
        acc[reminderId] = false; // Default is unfinished
      });
      return acc;
    }, {});
    setCompletedReminders(initialCompleted);
  }, []);

  // Toggle the completion state of a reminder
  const handleToggleComplete = (reminderId) => {
    setCompletedReminders((prev) => {
      const newCompleted = { ...prev };
      newCompleted[reminderId] = !newCompleted[reminderId];
      return newCompleted;
    });
  };

  // Show selected reminder details
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
        <LanguageSelector /> 
      </div>

      <SearchBar onSearch={setQuery} style={{ marginBottom: '20px' }} />

      <div style={{ display: 'flex', justifyContent: 'flex-start', marginBottom: '20px' }}>
        <DatePicker
          onDateChange={(newDate) => setSelectedDate(newDate)}
          initialDate={selectedDate}
          style={{ width: '35%' }} 
        />
      </div>

      <ReminderList
        reminders={reminders}
        selectedDate={selectedDate}
        completedReminders={completedReminders}
        handleToggleComplete={handleToggleComplete}
        onItemClick={handleItemClick} // Pass onItemClick to ReminderList
      />

      <div style={{ marginTop: '20px' }}>
        <Button variant="danger" style={{ width: '100%' }} onClick={() => navigate('/create-reminder')}>
          {t('+ New Reminder')}
        </Button>
      </div>


      {selectedReminder && <ReminderDetails reminder={selectedReminder} onClose={() => setSelectedReminder(null)} />}


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
