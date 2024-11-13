import React, { useState } from 'react';

const DatePicker = ({ onDateChange, initialDate }) => {
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
                border: '1px solid rgb(204, 204, 204)',
                backgroundColor: 'rgb(255, 255, 255)',
                width: '100%',
                boxSizing: 'border-box',
                appearance: 'none', // 取消原生样式
                WebkitAppearance: 'none', // 兼容Safari
                MozAppearance: 'none', // 兼容Firefox
                transition: 'all 0.2s ease',
            }}
        />
    );
};

export default DatePicker;
