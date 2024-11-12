import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.jsx'
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css'
import './i18n';
import "react-datepicker/dist/react-datepicker.css";


createRoot(document.getElementById('root')).render(
  <StrictMode>
    <App />
  </StrictMode>,
)
