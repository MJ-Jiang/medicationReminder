import React, { useState } from 'react';

const ReminderForm = ({ onAddReminder }) => {
    const [name, setName] = useState('');
    const [description, setDescription] = useState('');
    const [dosage, setDosage] = useState('');
    const [startDate, setStartDate] = useState('');
    const [frequency, setFrequency] = useState('');
    const [endDate, setEndDate] = useState('');
    const [times, setTimes] = useState(['']); // Start with one time input

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
        onAddReminder(newReminder); // Pass the new reminder to the parent
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
                <input type="date" value={startDate} onChange={(e) => setStartDate(e.target.value)} required />
            </div>
            <div>
                <label>Frequency:</label>
                <select value={frequency} onChange={(e) => setFrequency(e.target.value)} required>
                    <option value="">Select Frequency</option>
                    <option value="daily">Daily</option>
                    <option value="weekly">Weekly</option>
                    <option value="weekdays">Every Weekday</option>
                    <option value="weekends">Every Weekend</option>
                </select>
            </div>
            <div>
                <label>End Date:</label>
                <input type="date" value={endDate} onChange={(e) => setEndDate(e.target.value)} required />
            </div>
            <div>
                <label>Times:</label>
                {times.map((time, index) => (
                    <div key={index} style={{ display: 'flex', alignItems: 'center' }}>
                        <input
                            type="time"
                            value={time}
                            onChange={(e) => handleTimeChange(index, e.target.value)}
                            required
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
