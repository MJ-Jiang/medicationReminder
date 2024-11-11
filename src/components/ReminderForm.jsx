import React, { useState } from 'react';
import TimeInputComponent from './TimeInputComponent';
import DatePicker from './DatePicker';  // 你自定义的 DatePicker 组件
import { useTranslation } from 'react-i18next';

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
        <form onSubmit={handleSubmit}>
            <div>
                <label>{t('Name')}:</label>
                <input type="text" value={name} onChange={(e) => setName(e.target.value)} required />
            </div>
            <div>
                <label>{t('Description')}:</label>
                <input type="text" value={description} onChange={(e) => setDescription(e.target.value)} />
            </div>
            <div>
                <label>{t('Dosage')}:</label>
                <input type="text" value={dosage} onChange={(e) => setDosage(e.target.value)} />
            </div>
            <div>
                <label>{t('Start Date')}:</label>
                <DatePicker 
                    selectedDate={startDate} 
                    onDateChange={(date) => setStartDate(date)} 
                />
            </div>
            <div>
                <label>{t('Frequency')}:</label>
                <select value={frequency} onChange={(e) => setFrequency(e.target.value)} required>
                    <option value="">{t('Select Frequency')}</option>
                    <option value="daily">Daily</option>
                    <option value="weekly">Weekly</option>
                </select>
            </div>
            <div>
                <label>{t('End Date')}:</label>
                <DatePicker 
                    selectedDate={endDate} 
                    onDateChange={handleEndDateChange} 
                />
                {error && <div style={{ color: 'red', marginTop: '10px' }}>{error}</div>}  {/* 错误消息显示 */}
            </div>
            <div>
                {times.map((time, index) => (
                    <div key={index} style={{ display: 'flex', alignItems: 'center' }}>
                        <TimeInputComponent 
                            selectedTime={time} 
                            setSelectedTime={(newTime) => handleTimeChange(index, newTime)} 
                        />
                        {index === times.length - 1 && (
                            <button type="button" onClick={handleAddTime}>+</button>
                        )}
                        {times.length > 1 && (
                            <button type="button" onClick={() => {
                                const updatedTimes = times.filter((_, i) => i !== index);
                                setTimes(updatedTimes);
                            }}>-</button>
                        )}
                    </div>
                ))}
            </div>
            <button type="submit" disabled={!!error}>{t('Add Reminder')}</button>  {/* 禁用提交按钮直到没有错误 */}
        </form>
    );
};

export default ReminderForm;
