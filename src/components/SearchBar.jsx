import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { InputGroup, FormControl } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';

const SearchBar = ({ onSearch }) => {
    const [query, setQuery] = useState('');
    const navigate = useNavigate();
    const { t, i18n } = useTranslation();

    const handleSearch = () => {
        if (query) {
            navigate(`/medication-description?query=${encodeURIComponent(query)}`);
        }
    };

    const handleKeyPress = (event) => {
        if (event.key === 'Enter') {
            handleSearch();
        }
    };

    const clearSearch = () => {
        setQuery('');
    };

    return (
        <InputGroup className="mb-3">
            {/* Left icon for search */}
            <InputGroup.Text>
                <img src="/src/assets/search.png" alt="Search" width="16" height="16" />
            </InputGroup.Text>

            {/* Search input field */}
            <FormControl
                type="text"
                placeholder={t('Search for medication')}
                value={query}
                onChange={(e) => setQuery(e.target.value)}
                onKeyPress={handleKeyPress}
            />

            {/* Right icon for clear, always visible */}
            <InputGroup.Text onClick={clearSearch} style={{ cursor: 'pointer' }}>
                <img src="/src/assets/delete.png" alt="Clear" width="16" height="16" />
            </InputGroup.Text>
        </InputGroup>
    );
};

export default SearchBar;
