// ReminderList.jsx
import React, { useEffect, useState } from 'react';
import ReminderItem from './ReminderItem';

const ReminderList = ({ reminders, selectedDate, completedReminders, handleToggleComplete, onItemClick }) => {
    const [sortedReminderItems, setSortedReminderItems] = useState([]);

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
                    completed: completedReminders[reminderId], // 添加completed状态
                };
            })
        );

        // 按时间排序
        setSortedReminderItems(allReminderItems.sort((a, b) => {
            const timeA = new Date(`${selectedDate}T${a.time}:00`);
            const timeB = new Date(`${selectedDate}T${b.time}:00`);
            return timeA - timeB;
        }));
    }, [reminders, selectedDate, completedReminders]);

    return (
        <div>
            {/* 显示提醒条目 */}
            {sortedReminderItems.length === 0 ? (
                <p>No reminders for {selectedDate}.</p>
            ) : (
                sortedReminderItems.map((reminder) => (
                    <ReminderItem
                        key={reminder.reminderId}
                        reminder={reminder}
                        onToggleComplete={() => handleToggleComplete(reminder.reminderId)}
                        onItemClick={() => onItemClick(reminder)} // 传递当前 reminder 给 onItemClick
                    />
                ))
            )}
        </div>
    );
};

export default ReminderList;
