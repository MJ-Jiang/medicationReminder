import React from 'react';
import { useNavigate } from 'react-router-dom';

const BackButton = () => {
    const navigate = useNavigate();

    const handleClick = () => {
        navigate(-1); // 返回上一页
    };

    return (
        <button
            onClick={handleClick}
            style={{
                background: 'none',
                border: 'none',
                padding: '0',
                cursor: 'pointer',
                fontSize: '16px',
                display: 'flex',
                alignItems: 'center',
            }}
        >
            ←
        </button>
    );
};

export default BackButton;