import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import './index.css';
import App from './App.jsx';
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import './i18n';
import "react-datepicker/dist/react-datepicker.css";
import 'react-toastify/dist/ReactToastify.css'; // 
import { toast, ToastContainer } from 'react-toastify'; // 


createRoot(document.getElementById('root')).render(
  <StrictMode>
    <App />
    <ToastContainer
      position="top-center"
      autoClose={5000}  
      hideProgressBar={false} 
      newestOnTop={true} 
      closeOnClick={true} 
      pauseOnHover={true} 
      draggable={false} 
      pauseOnFocusLoss={false}
    />
  </StrictMode>,
);
