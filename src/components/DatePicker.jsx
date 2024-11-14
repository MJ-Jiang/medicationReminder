import React, { useState, useEffect } from 'react';

const DatePicker = ({ onDateChange, initialDate, style = {} }) => {
    const [selectedDate, setSelectedDate] = useState(initialDate || new Date().toISOString().slice(0, 10));

    // Use useEffect to monitor changes in initialDate and update selectedDate synchronously
    useEffect(() => {
        if (initialDate) {
            setSelectedDate(initialDate);
        }
    }, [initialDate]);

    const handleChange = (event) => {
        const newDate = event.target.value;
        setSelectedDate(newDate);
        onDateChange(newDate); // Calling the parent component's callback
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
                appearance: 'none', // Remove native styling
                WebkitAppearance: 'none', // Compatibility for Safari
                MozAppearance: 'none', // Compatibility for Firefox
                transition: 'all 0.2s ease',
                width: '100%',
                ...style, // The passed-in style will override default styles

            }}
        />
    );
};

export default DatePicker;
