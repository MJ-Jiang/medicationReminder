import React, { useEffect, useState } from 'react';
import ReminderItem from './ReminderItem';
import '../App.css'; // 确保 App.css 文件被正确导入

const ReminderList = ({ reminders, selectedDate, completedReminders, handleToggleComplete, onItemClick }) => {
    const [sortedReminderItems, setSortedReminderItems] = useState([]);
    const [isDateChanged, setIsDateChanged] = useState(false);

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

        // 设置日期更改状态以触发闪烁效果
        setIsDateChanged(true);
        const timer = setTimeout(() => setIsDateChanged(false), 200); // 闪烁效果持续200ms

        return () => clearTimeout(timer); // 清除定时器
    }, [reminders, selectedDate, completedReminders]);

    return (
        <div className={isDateChanged ? 'flash' : ''}> {/* 只有在日期更改时，应用闪烁效果 */}
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
