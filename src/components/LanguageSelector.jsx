
import React from 'react';
import { Dropdown } from 'react-bootstrap';
import { changeLanguage } from 'i18next';


const LanguageSelector = () => {
    return (
        <Dropdown align="end">
            <Dropdown.Toggle
                as="img"
                src="src/assets/lng.png"
                alt="Language Selector"
                style={{ width: '24px', height: '24px', cursor: 'pointer' }}
            />
            <Dropdown.Menu style={{ minWidth: 'auto', width: 'auto', padding: '0' }}>
                <Dropdown.Item
                    eventKey="en"
                    onClick={() => changeLanguage('en')}
                    className="custom-dropdown-item"
                >
                    EN
                </Dropdown.Item>
                <Dropdown.Item
                    eventKey="fi"
                    onClick={() => changeLanguage('fi')}
                    className="custom-dropdown-item"
                >
                    FI
                </Dropdown.Item>
            </Dropdown.Menu>
        </Dropdown>
    );
};

export default LanguageSelector;
