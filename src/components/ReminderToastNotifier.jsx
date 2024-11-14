import React, { useEffect, useState } from 'react';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const ReminderToastNotifier = ({ reminders, selectedDate, completedReminders }) => {
  // Get the triggered reminder ID stored in localStorage
  const getTriggeredRemindersFromStorage = () => {
    const triggeredReminders = JSON.parse(localStorage.getItem('triggeredReminders')) || [];
    return new Set(triggeredReminders);
  };

  const [triggeredReminders, setTriggeredReminders] = useState(getTriggeredRemindersFromStorage);

  useEffect(() => {
    const interval = setInterval(() => {
      const currentTime = new Date(); 

      // Convert selectedDate to a Date object so that you can compare only the date part
      const selectedDateObj = new Date(selectedDate);
      const currentDateObj = new Date(); 

      // Compare the date parts, making sure to only compare the year, month, and day
      const todayString = currentDateObj.toISOString().slice(0, 10); 
      const selectedDateString = selectedDateObj.toISOString().slice(0, 10); 

      //  If selectedDate is in the past, skip the reminder
      if (selectedDateString < todayString) {
        return; 
      }

      reminders.forEach((reminder) => {
        reminder.times.forEach((time, timeIndex) => {
          const reminderId = `${reminder.name}-${reminder.startDate}-${time}-${timeIndex}`;
          const reminderStartDate = new Date(reminder.startDate);
          const reminderEndDate = new Date(reminder.endDate);
          const reminderTime = new Date(`${selectedDate}T${time}:00`);

          // Trigger reminders only if:
// 1. Current time is greater than or equal to reminder time
// 2. Reminder is not marked as completed
// 3. Reminder time is not triggered
// 4. Reminder date range includes selectedDate
// Make sure the reminder time is within the date specified by selectedDate

          // The comparison is the year, month, and day parts, ensuring that only the date range is checked to see if it matches
          const isReminderForToday = reminderStartDate <= selectedDateObj && reminderEndDate >= selectedDateObj;

          if (
            currentTime < reminderTime ||
            completedReminders[reminderId] || 
            triggeredReminders.has(reminderId) || 
            !isReminderForToday 
          ) {
            return; // Skip this reminder
          }

          // 只在选定日期内才会触发提醒
          toast.info(`Time to take: ${reminder.name}`, {
            closeButton: true,  // 显示关闭按钮
            autoClose: false,   // 不自动关闭
          });

         // Update the status of triggeredReminders and save it to localStorage
          setTriggeredReminders((prevSet) => {
            const newSet = new Set(prevSet.add(reminderId));
            localStorage.setItem('triggeredReminders', JSON.stringify([...newSet])); 
            return newSet;
          });
        });
      });
    }, 1000); // Check once per second

    return () => clearInterval(interval); // Clear Timer
  }, [reminders, selectedDate, completedReminders, triggeredReminders]); 

  return null; 
};

export default ReminderToastNotifier;
