import React from 'react';
import ReminderForm from '../components/ReminderForm';
import { useNavigate } from 'react-router-dom';

const CreateReminderPage = ({ onAddReminder }) => {
    const navigate = useNavigate();

    const handleAddReminder = (reminder) => {
        onAddReminder(reminder);
        navigate(`/reminder/${reminder.name}`); // Navigate to the details page using the name as an identifier
    };

    return (
        <div>
            <h1>Create Reminder</h1>
            <ReminderForm onAddReminder={handleAddReminder} />
        </div>
    );
};

export default CreateReminderPage;
