{/**Time picker for selecting times */}

import React from 'react';
import { useTranslation } from 'react-i18next';


const TimeInputComponent = ({ selectedTime, setSelectedTime }) => {
    const { t, i18n } = useTranslation();
    return (
        <div>
            <label htmlFor="time">{t('Reminder Time')}:</label> {/** Creates a text label that describes what the input is for */}
            <input
                id="time"
                type="time"
                value={selectedTime}
                onChange={(e) => setSelectedTime(e.target.value)}
                required //Make this <input> field required.
            />
        </div>
    );
};

export default TimeInputComponent;
