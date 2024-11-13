import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import './index.css';
import App from './App.jsx';
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import './i18n';
import "react-datepicker/dist/react-datepicker.css";
import 'react-toastify/dist/ReactToastify.css'; // 导入 react-toastify 样式
import { toast, ToastContainer } from 'react-toastify'; // 导入 ToastContainer

// 渲染整个应用
createRoot(document.getElementById('root')).render(
  <StrictMode>
    <App />
    {/* ToastContainer 放在这里，确保全局都能使用 */}
    <ToastContainer
      position="top-center"
      autoClose={5000}  // 自动关闭时间
      hideProgressBar={false} // 显示进度条
      newestOnTop={true} // 新的通知会显示在最上面
      closeOnClick={true} // 点击关闭通知
      pauseOnHover={true} // 悬停时暂停
      draggable={false}  // 禁用拖动
      pauseOnFocusLoss={false} // 页面失去焦点时暂停
    />
  </StrictMode>,
);
