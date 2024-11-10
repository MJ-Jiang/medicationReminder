{/**Time picker for selecting times */}

import React from 'react';

const TimeInputComponent = ({ selectedTime, setSelectedTime }) => {
    return (
        <div>
            <label htmlFor="time">Reminder Time:</label> {/** Creates a text label that describes what the input is for */}
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
