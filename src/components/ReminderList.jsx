import React, { useEffect, useState, useRef } from 'react';
import ReminderItem from './ReminderItem';
import '../App.css';

const ReminderList = ({ reminders, selectedDate, completedReminders, handleToggleComplete, onItemClick }) => {
  const [sortedReminderItems, setSortedReminderItems] = useState([]);
  const [isDateChanged, setIsDateChanged] = useState(false);
  
  // 使用 useRef 保存之前的 selectedDate
  const prevSelectedDateRef = useRef(selectedDate);

  useEffect(() => {
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
          completed: completedReminders[reminderId], // 添加 completed 状态
        };
      })
    );

    // 按时间排序
    setSortedReminderItems(
      allReminderItems.sort((a, b) => {
        const timeA = new Date(`${selectedDate}T${a.time}:00`);
        const timeB = new Date(`${selectedDate}T${b.time}:00`);
        return timeA - timeB;
      })
    );

    // 检查日期是否发生变化
    const prevDate = new Date(prevSelectedDateRef.current);
    const currentDate = new Date(selectedDate);
    
    if (currentDate.getTime() !== prevDate.getTime()) {
      setIsDateChanged(true);
      // 使用 setTimeout 清除闪烁效果
      const timer = setTimeout(() => setIsDateChanged(false), 200);
      prevSelectedDateRef.current = selectedDate; // 更新 ref
      return () => clearTimeout(timer);
    }
  }, [reminders, selectedDate, completedReminders]);

  return (
    <div key={selectedDate}> {/* 使用 key 强制重新渲染组件 */}
      {sortedReminderItems.length === 0 ? (
        <p>No reminders for {selectedDate}.</p>
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
