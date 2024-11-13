// ReminderToastNotifier.jsx
import React, { useEffect, useState } from 'react';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const ReminderToastNotifier = ({ reminders, selectedDate, completedReminders }) => {
  // 获取存储在 localStorage 中的已触发提醒 ID
  const getTriggeredRemindersFromStorage = () => {
    const triggeredReminders = JSON.parse(localStorage.getItem('triggeredReminders')) || [];
    return new Set(triggeredReminders);
  };

  const [triggeredReminders, setTriggeredReminders] = useState(getTriggeredRemindersFromStorage);

  useEffect(() => {
    const interval = setInterval(() => {
      const currentTime = new Date(); // 当前时间

      reminders.forEach((reminder) => {
        reminder.times.forEach((time, timeIndex) => {
          const reminderId = `${reminder.name}-${reminder.startDate}-${time}-${timeIndex}`;
          const reminderTime = new Date(`${selectedDate}T${time}:00`); // 设定的提醒时间

          // 只在以下条件下触发提醒：
          // 1. 当前时间大于等于提醒时间
          // 2. 提醒没有被标记为完成
          // 3. 提醒时间未触发
          if (
            currentTime < reminderTime || // 当前时间小于提醒时间，跳过
            completedReminders[reminderId] || // 提醒已完成，跳过
            triggeredReminders.has(reminderId) // 提醒已触发过，跳过
          ) {
            return; // 跳过该提醒
          }

          // 在此时间点触发 toast
          toast.info(`Time to take: ${reminder.name}`, {
            closeButton: true,  // 显示关闭按钮
            autoClose: false,   // 不自动关闭
          });

          // 更新 triggeredReminders 的状态，并保存到 localStorage
          setTriggeredReminders((prevSet) => {
            const newSet = new Set(prevSet.add(reminderId));
            localStorage.setItem('triggeredReminders', JSON.stringify([...newSet])); // 将状态存储到 localStorage
            return newSet;
          });
        });
      });
    }, 1000); // 每秒检查一次

    return () => clearInterval(interval); // 清除定时器
  }, [reminders, selectedDate, completedReminders, triggeredReminders]); // 添加 triggeredReminders 作为依赖

  return null; // 该组件不渲染任何内容，只处理逻辑
};

export default ReminderToastNotifier;
