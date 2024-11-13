import React, { useState, useEffect } from 'react';

const DatePicker = ({ onDateChange, initialDate, style = {} }) => {
    const [selectedDate, setSelectedDate] = useState(initialDate || new Date().toISOString().slice(0, 10));

    // 使用 useEffect 监听 initialDate 的变化并同步更新 selectedDate
    useEffect(() => {
        if (initialDate) {
            setSelectedDate(initialDate);
        }
    }, [initialDate]);

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
                ...style, // 传递的style会覆盖默认样式
            }}
        />
    );
};

export default DatePicker;
