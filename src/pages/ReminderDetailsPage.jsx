import React from 'react';
import { useParams } from 'react-router-dom';

const ReminderDetailsPage = ({ reminders }) => {
    const { id } = useParams(); // id will be the name in this case
    const reminder = reminders.find((r) => r.name === id);

    if (!reminder) {
        return <div>Reminder not found</div>;
    }

    return (
        <div>
            <h1>{reminder.name}</h1>
            <p>Description: {reminder.description}</p>
            <p>Dosage: {reminder.dosage}</p>
            <p>Start Date: {reminder.startDate}</p>
            <p>Frequency: {reminder.frequency}</p>
            <p>End Date: {reminder.endDate}</p>
            <p>Times: {reminder.times.join(', ')}</p>
        </div>
    );
};

export default ReminderDetailsPage;
