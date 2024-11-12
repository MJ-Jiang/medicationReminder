import React from 'react';
import { useTranslation } from 'react-i18next';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { TimePicker } from '@mui/x-date-pickers/TimePicker';
import dayjs from 'dayjs';

const TimeInputComponent = ({ selectedTime, setSelectedTime }) => {
    const { t } = useTranslation();

    return (
        <div style={{ margin: '10px 0' }}>
            <label htmlFor="time" style={{ display: 'block',  marginBottom: '8px' }}>
                {t('Reminder Time')}
            </label>
            <LocalizationProvider dateAdapter={AdapterDayjs}>
                <TimePicker
                    value={selectedTime ? dayjs(selectedTime, 'HH:mm') : null}
                    onChange={(newValue) => {
                        setSelectedTime(newValue ? newValue.format('HH:mm') : '');
                    }}
                    placeholder={selectedTime ? '' : t('Select Time')}  // Show placeholder only when no time is selected
                    sx={{
                        '& .MuiInputBase-root': {
                            height: 40,
                            display: 'flex',
                            alignItems: 'center', // Ensure the content is vertically centered
                            padding: '0 10px',    // Control horizontal padding
                            paddingTop: '0px',    // Remove any padding that may push the text down
                        },
                        '& .MuiInputBase-input': {
                            padding: 0,             // Remove extra padding
                            lineHeight: 'normal',   // Set line-height to normal
                            verticalAlign: 'middle', // Ensures the text is vertically centered
                        },
                    }}
                    renderInput={(params) => <input {...params} required />}
                />
            </LocalizationProvider>
        </div>
    );
};

export default TimeInputComponent;
