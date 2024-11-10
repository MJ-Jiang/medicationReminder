import React, { useState } from 'react';
import TimeInputComponent from './TimeInputComponent';
import DatePicker from './DatePicker';  // 你自定义的 DatePicker 组件

const ReminderForm = ({ onAddReminder }) => {
    const [name, setName] = useState('');
    const [description, setDescription] = useState('');
    const [dosage, setDosage] = useState('');
    const [startDate, setStartDate] = useState(new Date()); // 默认今天
    const [endDate, setEndDate] = useState(new Date()); // 默认今天
    const [frequency, setFrequency] = useState('');
    const [times, setTimes] = useState(['']); // 初始只有一个时间输入框

    const handleAddTime = () => {
        setTimes([...times, '']);
    };

    const handleTimeChange = (index, newTime) => {
        const updatedTimes = [...times];
        updatedTimes[index] = newTime;
        setTimes(updatedTimes);
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        
        const newReminder = { name, description, dosage, startDate, frequency, endDate, times };
        onAddReminder(newReminder);

        // 重置表单
        setName('');
        setDescription('');
        setDosage('');
        setStartDate(new Date()); 
        setFrequency('');
        setEndDate(new Date()); 
        setTimes(['']); 
    };

    return (
        <form onSubmit={handleSubmit}>
            <div>
                <label>Name:</label>
                <input type="text" value={name} onChange={(e) => setName(e.target.value)} required />
            </div>
            <div>
                <label>Description:</label>
                <input type="text" value={description} onChange={(e) => setDescription(e.target.value)} />
            </div>
            <div>
                <label>Dosage:</label>
                <input type="text" value={dosage} onChange={(e) => setDosage(e.target.value)} />
            </div>
            <div>
                <label>Start Date:</label>
                <DatePicker 
                    selectedDate={startDate} 
                    onDateChange={(date) => setStartDate(date)} 
                />
            </div>
            <div>
                <label>Frequency:</label>
                <select value={frequency} onChange={(e) => setFrequency(e.target.value)} required>
                    <option value="">Select Frequency</option>
                    <option value="daily">Daily</option>
                    <option value="weekly">Weekly</option>
                </select>
            </div>
            <div>
                <label>End Date:</label>
                <DatePicker 
                    selectedDate={endDate} 
                    onDateChange={(date) => setEndDate(date)} 
                />
            </div>
            <div>
                {times.map((time, index) => (
                    <div key={index} style={{ display: 'flex', alignItems: 'center' }}>
                        <TimeInputComponent 
                            selectedTime={time} 
                            setSelectedTime={(newTime) => handleTimeChange(index, newTime)} 
                        />
                        {index === times.length - 1 && (
                            <button type="button" onClick={handleAddTime}>+</button>
                        )}
                        {times.length > 1 && (
                            <button type="button" onClick={() => {
                                const updatedTimes = times.filter((_, i) => i !== index);
                                setTimes(updatedTimes);
                            }}>-</button>
                        )}
                    </div>
                ))}
            </div>
            <button type="submit">Add Reminder</button>
        </form>
    );
};

export default ReminderForm;
