import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';

const MedicationDescriptionPage = () => {
    const [medicationInfo, setMedicationInfo] = useState(null);
    const [error, setError] = useState(null);
    const location = useLocation();
    const query = new URLSearchParams(location.search).get('query');

    useEffect(() => {
        if (query) {
            // Fetch medication info from OpenFDA
            fetch(`https://api.fda.gov/drug/label.json?search=${query}`)
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }
                    return response.json();
                })
                .then(data => {
                    if (data.results && data.results.length > 0) {
                        setMedicationInfo(data);
                        setError(null); // Reset any previous errors
                    } else {
                        setMedicationInfo(null); // No results found
                        setError('No medication information found for your query.'); // Set no results error message
                    }
                })
                .catch(error => {
                    console.error("Error fetching medication info:", error);
                    setError('Error fetching medication information.'); // Handle network errors
                });
        } else {
            // If no query is provided, reset states
            setMedicationInfo(null);
            setError(null);
        }
    }, [query]);

    // Only show the loading message if a search has been made
    if (error) return <p>{error}</p>; // Show error message if any

    // If there is no medication info and a query was entered, prompt the user
    if (!medicationInfo && query) return <p>Please enter a medication name to see details.</p>;

    // When there is medication information to display
    return (
        <div>
            <h2>Medication Information</h2>
            <p><strong>Name:</strong> {medicationInfo?.results?.[0]?.openfda?.brand_name?.[0] || 'N/A'}</p>
            <p><strong>Purpose:</strong> {medicationInfo?.results?.[0]?.purpose?.[0] || 'N/A'}</p>
            <p><strong>Description:</strong> {medicationInfo?.results?.[0]?.description?.[0] || 'N/A'}</p>
        </div>
    );
};

export default MedicationDescriptionPage;
