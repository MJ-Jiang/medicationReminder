import React from 'react';

const ReminderItem = ({ reminder, onToggleComplete, onItemClick }) => {
    return (
        <div 
            style={{ 
                display: 'flex', 
                alignItems: 'center', 
                padding: '12px', 
                borderRadius: '8px',  // 圆角
                border: '1px solid #ddd',  // 边框颜色更柔和
                boxShadow: '0 2px 4px rgba(0, 0, 0, 0.1)',  // 添加阴影
                backgroundColor: reminder.completed ? '#f0f0f0' : '#fff', // 更柔和的已完成背景色
                textDecoration: reminder.completed ? 'line-through' : 'none', // 删除线样式
                marginBottom: '10px', // 每个条目之间有间距
                transition: 'all 0.3s ease', // 平滑过渡
            }}
        >
            <div
                onClick={onItemClick} // 点击条目导航到详细信息
                style={{
                    flex: 1,
                    cursor: 'pointer', // 确保光标在悬停时变为指针
                    display: 'flex',
                    justifyContent: 'space-between',
                    alignItems: 'center',
                    fontSize: '16px', // 提高字体可读性
                    color: reminder.completed ? '#888' : '#333', // 文字颜色根据状态调整
                    paddingRight: '10px', // 右边留一些空间
                }}
            >
                <span>{reminder.name} - Time: {reminder.time} - Dosage: {reminder.dosage}</span>
            </div>
            <input
                type="checkbox"
                checked={reminder.completed || false} // 如果未定义则默认为 false
                onChange={() => onToggleComplete(reminder.id)} // 切换复选框
                style={{
                    marginLeft: '10px',
                    width: '16px',  // 调整复选框大小
                    height: '16px',  // 调整复选框大小
                    cursor: 'pointer', // 光标样式
                    accentColor: reminder.completed ? '#888' : '#007bff', // 打勾后的颜色为深灰色
                    transition: 'all 0.3s ease', // 平滑过渡
                }}
            />
        </div>
    );
    
    
};

export default ReminderItem;
