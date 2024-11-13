import React, { useState } from 'react';

const DatePicker = ({ onDateChange, initialDate, style = {} }) => {  // 给style设置默认值为空对象
    const [selectedDate, setSelectedDate] = useState(initialDate || new Date().toISOString().slice(0, 10));

    const handleChange = (event) => {
        const newDate = event.target.value;
        setSelectedDate(newDate);
        onDateChange(newDate); // 调用父组件的回调
    };

    return (
        <input
            type="date"
            value={selectedDate}
            onChange={handleChange}
            style={{
                fontSize: '1rem',
                padding: '5px 5px',
                borderRadius: '8px',
                border: '1px solid #ced4da',
                backgroundColor: 'rgb(255, 255, 255)',
                boxSizing: 'border-box',
                appearance: 'none', // 取消原生样式
                WebkitAppearance: 'none', // 兼容Safari
                MozAppearance: 'none', // 兼容Firefox
                transition: 'all 0.2s ease',
                width: '100%',
                ...style, // 这里确保了传递给组件的style会覆盖默认的样式
            }}
        />
    );
};

export default DatePicker;
