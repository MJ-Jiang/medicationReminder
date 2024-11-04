{/*Page for creating new medication reminders*/}
import React, { useState } from 'react';
import TimeInputComponent from '../components/TimeInputComponent'; // Time input for selecting times

const CreateReminderPage = () => {
    const [reminders, setReminders] = useState([]);
    const [name, setName] = useState('');
    const [description, setDescription] = useState('');
    const [dosage, setDosage] = useState(''); // New state for dosage
    const [selectedDate, setSelectedDate] = useState(new Date().toISOString().split('T')[0]); // Default to today's date
    const [selectedTimes, setSelectedTimes] = useState(['']);
    const [endDate, setEndDate] = useState('');
    const [frequency, setFrequency] = useState('daily'); // Frequency state

    const handleAddTime = () => {
        setSelectedTimes([...selectedTimes, '']);
    };

    const handleTimeChange = (index, newTime) => {
        const updatedTimes = [...selectedTimes];
        updatedTimes[index] = newTime;
        setSelectedTimes(updatedTimes);
    };

    const handleAddReminder = () => {
        const newReminder = {
            name,
            description,
            dosage, // Include dosage in the reminder object
            date: selectedDate,
            times: selectedTimes,
            endDate,
            frequency, // Include frequency in the reminder object
        };
        setReminders([...reminders, newReminder]);
        // Reset form fields
        setName('');
        setDescription('');
        setDosage(''); // Reset dosage
        setSelectedTimes(['']);
        setEndDate('');
        setFrequency('daily'); // Reset frequency to default
    };

    return (
        <div>
            <h1>Create Reminder</h1>
            <div>
                <label>Reminder Name:</label>
                <input 
                    type="text" 
                    placeholder="Enter reminder name"
                    value={name} 
                    onChange={(e) => setName(e.target.value)}
                    required
                />
            </div>
            <div>
                <label>Description:</label>
                <input 
                    type="text" 
                    placeholder="Enter reminder description"
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                    required
                />
            </div>
            <div>
                <label>Dosage:</label>
                <input 
                    type="text" 
                    placeholder="Enter dosage (e.g., 500mg)"
                    value={dosage} 
                    onChange={(e) => setDosage(e.target.value)}
                    required
                />
            </div>
            <div>
                <label>Date:</label>
                <input 
                    type="date" 
                    value={selectedDate} 
                    onChange={(e) => setSelectedDate(e.target.value)} 
                    required
                    style={{ margin: '5px', padding: '5px', borderRadius: '4px' }} // Consistent styling
                />
            </div>
            <div>
                <label>Frequency:</label>
                <select value={frequency} onChange={(e) => setFrequency(e.target.value)}>
                    <option value="daily">Daily</option>
                    <option value="weekly">Weekly</option>
                    <option value="weekdays">Every Weekday</option>
                    <option value="weekends">Every Weekend</option>
                </select>
            </div>
            <div>
                <label>End Date:</label>
                <input 
                    type="date" 
                    value={endDate}
                    onChange={(e) => setEndDate(e.target.value)} 
                    required
                    style={{ margin: '5px', padding: '5px', borderRadius: '4px' }} // Consistent styling
                />
            </div>
            <div>
                <label style={{ display: 'block' }}>Reminder Time:</label>
                {selectedTimes.map((time, index) => (
                    <div key={index} style={{ display: 'flex', alignItems: 'center', marginTop: '5px' }}>
                        <TimeInputComponent
                            selectedTime={time}
                            setSelectedTime={(newTime) => handleTimeChange(index, newTime)}
                        />
                        {index === selectedTimes.length - 1 && (
                            <button type="button" onClick={handleAddTime}>+</button>
                        )}
                    </div>
                ))}
            </div>
            <button onClick={handleAddReminder}>Add Reminder</button>
        
            <ul>
                {reminders.map((reminder, index) => (
                    <li key={index}>
                        {reminder.name} - {reminder.description} 
                        <br />
                        Dosage: {reminder.dosage} 
                        <br />
                        Date: {reminder.date} 
                        <br />
                        Times: {reminder.times.join(', ')} 
                        <br />
                        Frequency: {reminder.frequency} 
                        <br />
                        End Date: {reminder.endDate}
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default CreateReminderPage;

