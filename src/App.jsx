// App.jsx
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import CreateReminderPage from './pages/CreateReminderPage';

const App = () => {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<CreateReminderPage />} />
            </Routes>
        </Router>
    );
};

export default App;
