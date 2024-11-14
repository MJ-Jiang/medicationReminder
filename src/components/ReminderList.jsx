import React, { useEffect, useState, useRef } from 'react';
import ReminderItem from './ReminderItem';
import '../App.css';
import { useTranslation } from 'react-i18next';

const ReminderList = ({ reminders, selectedDate, completedReminders, handleToggleComplete, onItemClick }) => {
  const [sortedReminderItems, setSortedReminderItems] = useState([]);
  const [isDateChanged, setIsDateChanged] = useState(false);
  
  // Use useRef to save the previous selectedDate
  const prevSelectedDateRef = useRef(selectedDate);
  const { t } = useTranslation();
  
  useEffect(() => {
    // Filter reminders for the day
    const filteredReminders = reminders.filter((reminder) => {
      const currentDate = new Date(selectedDate);
      const reminderStartDate = new Date(reminder.startDate);
      const reminderEndDate = new Date(reminder.endDate);
      return reminderStartDate <= currentDate && reminderEndDate >= currentDate;
    });

    // Extract all reminders and sort them by time
    const allReminderItems = filteredReminders.flatMap((reminder) =>
      reminder.times.map((time, timeIndex) => {
        const reminderId = `${reminder.name}-${reminder.startDate}-${time}-${timeIndex}`;
        return {
          ...reminder,
          time,
          reminderId,
          completed: completedReminders[reminderId], // Add completed status
        };
      })
    );

    // Sort by time
    setSortedReminderItems(
      allReminderItems.sort((a, b) => {
        const timeA = new Date(`${selectedDate}T${a.time}:00`);
        const timeB = new Date(`${selectedDate}T${b.time}:00`);
        return timeA - timeB;
      })
    );

    // Check if the date has changed
    const prevDate = new Date(prevSelectedDateRef.current);
    const currentDate = new Date(selectedDate);
    
    if (currentDate.getTime() !== prevDate.getTime()) {
      setIsDateChanged(true);
      // Use setTimeout to clear the flickering effect
      const timer = setTimeout(() => setIsDateChanged(false), 200);
      prevSelectedDateRef.current = selectedDate; // update ref
      return () => clearTimeout(timer);
    }
  }, [reminders, selectedDate, completedReminders]);

  return (
    <div key={selectedDate}> {/* Use key to force re-rendering of component */}
      {sortedReminderItems.length === 0 ? (
        <p>{t('No reminders for')} {selectedDate}.</p>
      ) : (
        sortedReminderItems.map((reminder) => (
          <ReminderItem
            key={reminder.reminderId}
            reminder={reminder}
            isDateChanged={isDateChanged}
            onToggleComplete={() => handleToggleComplete(reminder.reminderId)}
            onItemClick={() => onItemClick(reminder)}
          />
        ))
      )}
    </div>
  );
};

export default ReminderList;
