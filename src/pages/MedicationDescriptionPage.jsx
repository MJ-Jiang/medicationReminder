import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom'; 
import { useTranslation } from 'react-i18next';
import { Card, Row, Col, Spinner } from 'react-bootstrap';  
import BackButton from '../components/BackButton';  
import MedicationCard from '../components/MedicationCard'; 

const MedicationDescriptionPage = () => {
    const [medicationInfo, setMedicationInfo] = useState([]);  
    const [isLoading, setIsLoading] = useState(false);  
    const location = useLocation();
    const query = new URLSearchParams(location.search).get('query');
    const { t } = useTranslation();

    useEffect(() => {
        if (!query) {return;}
        setIsLoading(true);

        fetch(`https://api.fda.gov/drug/label.json?search=openfda.brand_name:"${query}"&limit=10`)
            .then(response => response.json())
            .then(data => {
                if (data.results && data.results.length > 0) {
                    setMedicationInfo(data.results); 
                } else {
                    setMedicationInfo([]); 
                }
            })
            .catch(error => console.error("Error fetching medication info:", error))
            .finally(() => setIsLoading(false));

    }, [query]);

    if (isLoading) {
        return (
            <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100px' }}>
                <Spinner animation="border" role="status">
                    <span className="visually-hidden">Loading...</span>
                </Spinner>
            </div>
        ); // Display loading status
    }

    return (
        <Card className="centered-container">
            <Card.Body>
                <Row className="align-items-center mb-3">
                    <Col xs="auto">
                        <BackButton /> 
                    </Col>
                    <Col>
                        <h3 style={{ margin: 0, textAlign: 'center' }}>{t('Medication Details')}</h3>
                    </Col>
                </Row>
                {medicationInfo.length > 0 ? (
                    <div>
                        {medicationInfo.map((medication, index) => (
                            <Row key={index} className="mb-4">
                                <Col xs={12}> 
                                    <MedicationCard medication={medication} /> 
                                </Col>
                            </Row>
                        ))}
                    </div>
                ) : (
                    <div style={{ textAlign: 'center' }}>
                        <p>{t('No medication information found for your query.')}</p>
                    </div>
                )}
            </Card.Body>
        </Card>
    );
};

export default MedicationDescriptionPage;
