import React from 'react';
import { Card } from 'react-bootstrap';
import { useTranslation } from 'react-i18next';
const MedicationCard = ({ medication }) => {
    
    const description = `${medication.indications_and_usage ? medication.indications_and_usage[0] : 'N/A'}. ${medication.when_using ? medication.when_using[0] : 'N/A'}.`;
    const { t, i18n } = useTranslation();
    return (
        <Card>
            <Card.Body>
                <Card.Title>{medication.openfda.brand_name ? medication.openfda.brand_name[0] : 'N/A'}</Card.Title>
                <Card.Text>
                    <strong>{t('Purpose')}: </strong> 
                    {medication.purpose ? medication.purpose[0] : 'N/A'}
                </Card.Text>
                <Card.Text>
                    <strong>{t('Description')}: </strong> 
                    {description}
                </Card.Text>
            </Card.Body>
        </Card>
    );
};

export default MedicationCard;
