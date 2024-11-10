import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const SearchBar = ({ onSearch }) => {
    const [query, setQuery] = useState('');
    const navigate = useNavigate();

    const handleSearch = () => {
        if (query) {
            navigate(`/medication-description?query=${encodeURIComponent(query)}`);
        }
    };
    return (
        <div>
            <input
                type="text"
                placeholder="Search for medication..."
                value={query}
                onChange={(e) => setQuery(e.target.value)}
            />
            <button onClick={handleSearch}>Search</button>
        </div>
    );
};

export default SearchBar;
{/**encodeURIComponent(query) is used to encode the user's input to ensure that special characters (such as space, &, ?, etc.) do not cause problems in the URL.
    onChange calls the (e) => setQuery(e.target.value) function every time the user types or deletes a character. 
    This function updates the value of query and keeps the content of the input box synchronized with the query state.
    onClick={handleSearch} means that when the user clicks the "Search" button, the handleSearch function will be called to perform the search operation. */}