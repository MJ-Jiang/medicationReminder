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
            
        />
    );
};

export default DatePicker;
