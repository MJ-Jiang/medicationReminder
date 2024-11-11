import React from 'react';

const ReminderDetails = ({ reminder, onClose }) => {
    console.log(reminder); 
    if (!reminder) {
        return null; // Return null if there's no reminder data
    }

    const formattedStartDate = reminder.originalStartDate 
        ? new Date(reminder.originalStartDate).toLocaleDateString() 
        : new Date(reminder.startDate).toLocaleDateString();

    const formattedEndDate = reminder.originalEndDate 
        ? new Date(reminder.originalEndDate).toLocaleDateString() 
        : new Date(reminder.endDate).toLocaleDateString();

    // Sort times before displaying
    const sortedTimes = reminder.times.slice().sort((a, b) => {
        const [aHour, aMinute] = a.split(':').map(Number);
        const [bHour, bMinute] = b.split(':').map(Number);
        return aHour - bHour || aMinute - bMinute;
    });

    return (
        <div style={overlayStyle}>
            <div style={modalStyle}>
                <button onClick={onClose} style={closeButtonStyle}>x</button>
                <h1>{reminder.name}</h1>
                <p>Description: {reminder.description}</p>
                <p>Dosage: {reminder.dosage}</p>
                <p>Start Date: {formattedStartDate}</p>
                <p>Frequency: {reminder.frequency}</p>
                <p>End Date: {formattedEndDate}</p>
                <p>Times: {sortedTimes.join(', ')}</p> {/* Display sorted times */}
            </div>
        </div>
    );
};

// Styles
const overlayStyle = {
    position: 'fixed',
    top: 0,
    left: 0,
    right: 0,
    bottom: 0,
    backgroundColor: 'rgba(0, 0, 0, 0.5)',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    zIndex: 1000
};

const modalStyle = {
    backgroundColor: 'white',
    padding: '20px',
    borderRadius: '8px',
    position: 'relative',
    minWidth: '300px'
};

const closeButtonStyle = {
    position: 'absolute',
    top: '10px',
    right: '10px',
    background: 'none',
    border: 'none',
    cursor: 'pointer',
    fontSize: '20px',
};

export default ReminderDetails;
