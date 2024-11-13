import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom'; 
import { useTranslation } from 'react-i18next';
import { Card, Row, Col, Spinner } from 'react-bootstrap';  // 引入 Spinner
import BackButton from '../components/BackButton';  // 引入 BackButton
import MedicationCard from '../components/MedicationCard'; // 引入 MedicationCard 组件

const MedicationDescriptionPage = () => {
    const [medicationInfo, setMedicationInfo] = useState([]);  
    const [isLoading, setIsLoading] = useState(false);  
    const location = useLocation();
    const query = new URLSearchParams(location.search).get('query');
    const { t } = useTranslation();

    useEffect(() => {
        if (!query) {
            return;
        }

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
        ); // 显示加载状态
    }

    return (
        <Card className="centered-container">
            <Card.Body>
                {/* BackButton 和标题在同一行 */}
                <Row className="align-items-center mb-3">
                    <Col xs="auto">
                        <BackButton /> {/* 保持原有样式的 BackButton */}
                    </Col>
                    <Col>
                        <h3 style={{ margin: 0, textAlign: 'center' }}>{t('Medication Details')}</h3>
                    </Col>
                </Row>

                {/* 药物信息内容居中显示 */}
                {medicationInfo.length > 0 ? (
                    <div>
                        {medicationInfo.map((medication, index) => (
                            <Row key={index} className="mb-4">
                                <Col xs={12}> {/* 每行只显示一个 card */}
                                    <MedicationCard medication={medication} /> {/* 渲染每个卡片 */}
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
