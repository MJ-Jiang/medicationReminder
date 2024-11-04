import React from 'react';

const ReminderItem = ({ reminder, onToggleComplete, onItemClick }) => {
    return (
        <div 
            style={{ 
                display: 'flex', 
                alignItems: 'center', 
                padding: '8px', 
                borderBottom: '1px solid #ccc', 
                backgroundColor: reminder.completed ? '#e0e0e0' : 'white', // 变为灰色表示已标记
                textDecoration: reminder.completed ? 'line-through' : 'none' // 添加删除线样式
            }}
        >
            <div
                onClick={onItemClick} // 点击条目导航到详细信息
                style={{
                    flex: 1,
                    cursor: 'pointer', // 确保光标在悬停时变为指针
                    display: 'flex',
                    justifyContent: 'space-between',
                    alignItems: 'center'
                }}
            >
                <span>{reminder.name} - Time: {reminder.time} - Dosage: {reminder.dosage}</span>
            </div>
            <input
                type="checkbox"
                checked={reminder.completed || false} // 如果未定义则默认为 false
                onChange={onToggleComplete} // 切换复选框
                style={{ marginLeft: '10px' }}
            />
        </div>
    );
};

export default ReminderItem;
