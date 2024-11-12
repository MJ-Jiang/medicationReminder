import React, { useState } from 'react';
import TimeInputComponent from './TimeInputComponent';
import DatePicker from './DatePicker';  // 你自定义的 DatePicker 组件
import { useTranslation } from 'react-i18next';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';

const ReminderForm = ({ onAddReminder }) => {
    const [name, setName] = useState('');
    const [description, setDescription] = useState('');
    const [dosage, setDosage] = useState('');
    const [startDate, setStartDate] = useState(new Date()); // 默认今天
    const [endDate, setEndDate] = useState(new Date()); // 默认今天
    const [frequency, setFrequency] = useState('');
    const [times, setTimes] = useState(['']); // 初始只有一个时间输入框
    const [error, setError] = useState('');  // 存储错误信息
    const { t, i18n } = useTranslation();

    // 更新结束日期并检查是否晚于开始日期
    const handleEndDateChange = (date) => {
        const endDateObj = new Date(date);
        if (endDateObj <= startDate) {
            // 如果结束日期早于或等于开始日期，设置错误消息
            setError('结束日期必须晚于开始日期');
        } else {
            // 如果结束日期有效，更新结束日期并清除错误消息
            setError('');
            setEndDate(date);
        }
    };

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
        onAddReminder(newReminder);

        // 重置表单
        setName('');
        setDescription('');
        setDosage('');
        setStartDate(new Date()); 
        setFrequency('');
        setEndDate(new Date()); 
        setTimes(['']); 
    };

    return (
        <Form onSubmit={handleSubmit}>
        {/* Name Field */}
        <Form.Group className="mb-3" controlId="formName">
            <Form.Label>{t('Name')}</Form.Label>
            <Form.Control
                type="text"
                value={name}
                onChange={(e) => setName(e.target.value)}
                required
            />
        </Form.Group>

        {/* Description Field */}
        <Form.Group className="mb-3" controlId="formDescription">
            <Form.Label>{t('Description')}</Form.Label>
            <Form.Control
                type="text"
                value={description}
                onChange={(e) => setDescription(e.target.value)}
            />
        </Form.Group>

        {/* Dosage Field */}
        <Form.Group className="mb-3" controlId="formDosage">
            <Form.Label>{t('Dosage')}</Form.Label>
            <Form.Control
                type="text"
                value={dosage}
                onChange={(e) => setDosage(e.target.value)}
            />
        </Form.Group>

        {/* Start Date Field */}
        <Form.Group className="mb-3" controlId="formStartDate">
            <Form.Label>{t('Start Date')}</Form.Label>
            <DatePicker
                selectedDate={startDate}
                onDateChange={(date) => setStartDate(date)}
            />
        </Form.Group>

        {/* Frequency Field */}
        <Form.Group className="mb-3" controlId="formFrequency">
            <Form.Label>{t('Frequency')}</Form.Label>
            <Form.Select
                value={frequency}
                onChange={(e) => setFrequency(e.target.value)}
                required
            >
                <option value="">{t('Select Frequency')}</option>
                <option value="daily">Daily</option>
                <option value="weekly">Weekly</option>
            </Form.Select>
        </Form.Group>

        {/* End Date Field */}
        <Form.Group className="mb-3" controlId="formEndDate">
            <Form.Label>{t('End Date')}</Form.Label>
            <DatePicker
                selectedDate={endDate}
                onDateChange={handleEndDateChange}
            />
            {error && <div style={{ color: 'red', marginTop: '10px' }}>{error}</div>}
        </Form.Group>

        {/* Times Field */}
        <Form.Group className="mb-3" controlId="formTimes">
    {times.map((time, index) => (
        <div key={index} style={{ display: 'flex', alignItems: 'center', marginBottom: '5px' }}>
            <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'flex-start' }}>
                {/* Label and TimePicker inside the column */}
                <TimeInputComponent
                    selectedTime={time}
                    setSelectedTime={(newTime) => handleTimeChange(index, newTime)}
                />
            </div>
            {index === times.length - 1 && (
                <button
                    type="button"
                    onClick={handleAddTime}
                    style={{
                        background: 'none',     // No background
                        border: 'none',         // No border
                        padding: '0',           // Remove padding
                        marginLeft: '20px',     // Space between the TimeInput and the button
                        cursor: 'pointer',     // Pointer cursor on hover
                        marginTop: '25px',       // Only adjust this to move the button vertically
                    }}
                >
                    <img src="src/assets/add.png" alt="Add" style={{ width: '20px', height: '20px' }} />
                </button>
            )}
            {times.length > 1 && (
                <button
                    type="button"
                    onClick={() => {
                        const updatedTimes = times.filter((_, i) => i !== index);
                        setTimes(updatedTimes);
                    }}
                    style={{
                        background: 'none',  // No background
                        border: 'none',      // No border
                        padding: '0',        // Remove padding
                        marginLeft: '20px',  // Space between the buttons
                        cursor: 'pointer',  // Pointer cursor on hover
                        marginTop: '25px',    // Adjust this to control vertical space between the button and TimeInput
                    }}
                >
                    <img src="src/assets/minus.png" alt="minus" style={{ width: '20px', height: '20px' }} />
                </button>
            )}
        </div>
    ))}
</Form.Group>





        {/* Submit Button */}
        <Button variant="danger" type="submit" disabled={!!error}>{t('Add Reminder')}</Button>
    </Form>
    );
};

export default ReminderForm;
