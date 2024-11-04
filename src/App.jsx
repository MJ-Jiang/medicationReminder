import React, { useState } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import TodayRemindersPage from './pages/TodayRemindersPage';
import CreateReminderPage from './pages/CreateReminderPage';
import ReminderDetailsPage from './pages/ReminderDetailsPage';

const App = () => {
    const [reminders, setReminders] = useState([]);

    const addReminder = (reminder) => {
        setReminders((prev) => [...prev, reminder]);
    };

    return (
        <Router>
            <Routes>
                <Route path="/" element={<TodayRemindersPage reminders={reminders} />} />
                <Route path="/create-reminder" element={<CreateReminderPage onAddReminder={addReminder} />} />
                <Route path="/reminder/:id" element={<ReminderDetailsPage reminders={reminders} />} />
            </Routes>
        </Router>
    );
};

export default App;
