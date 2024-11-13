import React from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import { useTranslation } from 'react-i18next';
const ReminderDetails = ({ reminder, onClose }) => {
    if (!reminder) {
        return null;
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
    const { t, i18n } = useTranslation();
    return (
        <Modal show={!!reminder} onHide={onClose} backdrop="static" keyboard={false}>
            <Modal.Header closeButton>
                <Modal.Title>{reminder.name}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <p><strong>{t('Description')}:</strong> {reminder.description}</p>
                <p><strong>{t('Dosage')}:</strong> {reminder.dosage}</p>
                <p><strong>{t('Start Date')}:</strong> {formattedStartDate}</p>
                <p><strong>{t('Frequency')}:</strong> {reminder.frequency}</p>
                <p><strong>{t('End Date')}:</strong> {formattedEndDate}</p>
                <p><strong>{t('Times')}:</strong> {sortedTimes.join(', ')}</p>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={onClose}>Close</Button>
            </Modal.Footer>
        </Modal>
    );
};

export default ReminderDetails;
