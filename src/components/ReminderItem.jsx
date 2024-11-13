import React from 'react';

const ReminderItem = ({ reminder, onToggleComplete, onItemClick }) => {
    return (
        <div 
            style={{ 
                display: 'flex', 
                flexDirection: 'column', // 垂直布局
                alignItems: 'flex-start', // 左对齐
                padding: '12px', 
                borderRadius: '8px',  // 圆角
                border: '1px solid #ddd',  // 边框颜色更柔和
                boxShadow: '0 2px 4px rgba(0, 0, 0, 0.1)',  // 添加阴影
                backgroundColor: reminder.completed ? '#f0f0f0' : '#fff', // 更柔和的已完成背景色
                textDecoration: reminder.completed ? 'line-through' : 'none', // 删除线样式
                marginBottom: '10px', // 每个条目之间有间距
                transition: 'all 0.3s ease', // 平滑过渡
                position: 'relative', // 使复选框能够固定在右侧
            }}
        >
            <div
                onClick={onItemClick} // 点击条目导航到详细信息
                style={{
                    display: 'flex',
                    alignItems: 'flex-start', // 确保文本和图片垂直对齐
                    cursor: 'pointer', // 确保光标在悬停时变为指针
                    paddingBottom: '5px', // 为了让时间和剂量有点间距
                }}
            >
                {/* 图片部分 */}
                <img 
                    src="src/assets/pill.png" 
                    alt="Pill"
                    style={{
                        width: '24px', 
                        height: '24px', 
                        marginRight: '10px', // 图片和文本之间有点间距
                    }}
                />
                {/* 内容部分 */}
                <div style={{ display: 'flex', flexDirection: 'column' }}>
                    {/* 名称部分 */}
                    <span style={{
                        fontSize: '16px', // 提高字体可读性
                        color: reminder.completed ? '#888' : '#333', // 文字颜色根据状态调整
                    }}>
                        {reminder.name}
                    </span>
                    {/* 时间和剂量显示在一行 */}
                    <div style={{
                        display: 'flex', 
                        alignItems: 'center',
                        marginTop: '5px', // 为了让时间和剂量之间有间距
                        fontSize: '12px', // 更小的字体
                        color: '#888', // 使用浅灰色
                    }}>
                        {/* 时间 */}
                        <span style={{ marginRight: '15px' }}>{reminder.time}</span>
                        {/* 剂量 */}
                        <span>{reminder.dosage}</span>
                    </div>
                </div>
            </div>
            {/* 复选框部分，固定在卡片右侧 */}
            <input
                type="checkbox"
                checked={reminder.completed || false} // 如果未定义则默认为 false
                onChange={() => onToggleComplete(reminder.id)} // 切换复选框
                style={{
                    position: 'absolute',
                    top: '50%', // 使复选框垂直居中
                    right: '10px', // 距离右侧有点间距
                    transform: 'translateY(-50%)', // 完全居中
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
