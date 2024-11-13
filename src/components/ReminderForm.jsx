import React, { useState } from 'react';
import { ToastContainer, toast } from 'react-toastify';  // 修正：确保只导入一次
import 'react-toastify/dist/ReactToastify.css';
import TimeInputComponent from './TimeInputComponent';
import DatePicker from './DatePicker';  // 自定义的 DatePicker 组件
import { useTranslation } from 'react-i18next';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';

const ReminderForm = ({ onAddReminder }) => {
    const { t } = useTranslation();
    const [name, setName] = useState('');
    const [description, setDescription] = useState('');
    const [dosage, setDosage] = useState('');
    const [startDate, setStartDate] = useState(new Date().toISOString().slice(0, 10));
    const [endDate, setEndDate] = useState(new Date().toISOString().slice(0, 10));
    const [frequency, setFrequency] = useState('');
    const [times, setTimes] = useState(['']); // 初始只有一个时间输入框

    // Toast helper function for displaying error
    const showToast = (message) => {
        toast.error(message, {
            position: "top-right",
            autoClose: false,
            hideProgressBar: false,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
        });
    };

    const handleStartDateChange = (date) => {
        setStartDate(date);
    };

    const handleEndDateChange = (date) => {
        setEndDate(date);
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

        const startDateObj = new Date(startDate);
        const endDateObj = new Date(endDate);

        // 检查结束日期是否晚于开始日期
        if (endDateObj < startDateObj) {
            showToast(t('End date must be after start date'));  // Toast 提示
            return;  // 不提交表单，避免创建 reminder
        }

        // 如果日期有效，创建新的提醒
        const newReminder = { name, description, dosage, startDate, frequency, endDate, times };
        onAddReminder(newReminder);

        // 重置表单
        setName('');
        setDescription('');
        setDosage('');
        setFrequency('');
        setTimes(['']);

        // 重置日期到今天
        const today = new Date().toISOString().slice(0, 10);
        setStartDate(today);
        setEndDate(today);
    };

    return (
        <>
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
                        initialDate={startDate}
                        onDateChange={handleStartDateChange}
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
                        initialDate={endDate}
                        onDateChange={handleEndDateChange}
                    />
                </Form.Group>

                {/* Times Field */}
                <Form.Group className="mb-3" controlId="formTimes">
                    {times.map((time, index) => (
                        <div key={index} style={{ display: 'flex', alignItems: 'center', marginBottom: '5px' }}>
                            <TimeInputComponent
                                selectedTime={time}
                                setSelectedTime={(newTime) => handleTimeChange(index, newTime)}
                            />
                            {index === times.length - 1 && (
                                <button
                                    type="button"
                                    onClick={handleAddTime}
                                    style={{
                                        background: 'none',
                                        border: 'none',
                                        padding: '0',
                                        marginLeft: '20px',
                                        cursor: 'pointer',
                                        marginTop: '25px',
                                        paddingTop: '3px',
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
                                        background: 'none',
                                        border: 'none',
                                        padding: '0',
                                        marginLeft: '20px',
                                        cursor: 'pointer',
                                        marginTop: '25px',
                                        paddingTop: '3px',
                                    }}
                                >
                                    <img src="src/assets/minus.png" alt="minus" style={{ width: '20px', height: '20px' }} />
                                </button>
                            )}
                        </div>
                    ))}
                </Form.Group>

                {/* Submit Button */}
                <Button variant="danger" style={{ width: '100%' }} type="submit">
                    {t('Add Reminder')}
                </Button>
            </Form>

            {/* Toast container for displaying notifications */}
            <ToastContainer />
        </>
    );
};

export default ReminderForm;