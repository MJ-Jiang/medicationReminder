import React, { useState } from 'react';
import ReminderForm from '../components/ReminderForm';
import ReminderDetails from '../components/ReminderDetails';
import SearchBar from '../components/SearchBar';
import BackButton from '../components/BackButton';
import { useTranslation } from 'react-i18next';
import { Row, Col } from 'react-bootstrap';
import '../App.css';

const CreateReminderPage = ({ onAddReminder }) => {
    const [showDetails, setShowDetails] = useState(false);
    const [reminder, setReminder] = useState(null);
    const [query, setQuery] = useState('');
    const { t, i18n } = useTranslation();

    const handleAddReminder = (newReminder) => {
        const { startDate, endDate, frequency, times } = newReminder;
        const remindersToAdd = [];
        
        // Keep original start and end dates
        const originalStartDate = new Date(startDate).toISOString().slice(0, 10);
        const originalEndDate = new Date(endDate).toISOString().slice(0, 10);
    
        const reminderData = JSON.parse(localStorage.getItem('reminderData')) || [];
        
        // Generate reminders based on frequency
        if (frequency === 'daily') {
            let currentDate = new Date(startDate);
            const endDateObj = new Date(endDate);
    
            while (currentDate <= endDateObj) {
                remindersToAdd.push({
                    ...newReminder,
                    id: `${newReminder.name}-${currentDate.toISOString().slice(0, 10)}-${times.join('-')}`,  // Generate a unique id
                    startDate: currentDate.toISOString().slice(0, 10),
                    endDate: currentDate.toISOString().slice(0, 10),
                    times: times,
                    originalStartDate, 
                    originalEndDate, 
                });
                currentDate.setDate(currentDate.getDate() + 1); // add everyday
            }
        } else if (frequency === 'weekly') {
            let currentDate = new Date(startDate);
            const endDateObj = new Date(endDate);
    
            while (currentDate <= endDateObj) {
                remindersToAdd.push({
                    ...newReminder,
                    id: `${newReminder.name}-${currentDate.toISOString().slice(0, 10)}-${times.join('-')}`,  // 生成唯一 id
                    startDate: currentDate.toISOString().slice(0, 10),
                    endDate: currentDate.toISOString().slice(0, 10),
                    times: times,
                    originalStartDate, 
                    originalEndDate, 
                });
                currentDate.setDate(currentDate.getDate() + 7); // add 7 days
            }
        }

        // Add all reminders to the main app state
        if (remindersToAdd.length > 0) {
            // Update localStorage and parent component state
            const updatedReminderData = [...reminderData, ...remindersToAdd];
            localStorage.setItem('reminderData', JSON.stringify(updatedReminderData));

            remindersToAdd.forEach((reminder) => onAddReminder(reminder));
    
            // Keep reminder information for the entire cycle and pass it to details
            setReminder({
                ...newReminder,
                startDate: originalStartDate,
                endDate: originalEndDate
            });
            setShowDetails(true);
        } else {
            console.log("No reminders were created.");
        }
    };

    const handleClose = () => {
        setShowDetails(false); setReminder(null); // Clear reminders when closed
    };

    return (
        <div className="centered-container">
            <Row className="align-items-center mb-3">
                <Col xs="auto">
                    <BackButton />
                </Col>
                <Col className="text-center">
                    <h3 style={{ margin: 0 }}>{t('New Pill Reminder')}</h3>
                </Col>
                <Col xs="auto"></Col>
            </Row>
            
            <SearchBar onSearch={setQuery} />
            <ReminderForm onAddReminder={handleAddReminder} />
            {showDetails && <ReminderDetails reminder={reminder} onClose={handleClose} />}
        </div>
    );
};

export default CreateReminderPage;
