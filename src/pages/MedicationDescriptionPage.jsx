import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom'; // 导入 useNavigate
import BackButton from '../components/BackButton';
import { useTranslation } from 'react-i18next';
import Spinner from 'react-bootstrap/Spinner';
import Card from 'react-bootstrap/Card';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import { toast } from 'react-toastify'; // 导入 toast

const MedicationDescriptionPage = () => {
    const [medicationInfo, setMedicationInfo] = useState(null);
    const [error, setError] = useState(null);
    const location = useLocation();
    const query = new URLSearchParams(location.search).get('query');
    const { t, i18n } = useTranslation();
    const navigate = useNavigate(); // 获取 navigate 方法用于页面跳转

    useEffect(() => {
        if (query) {
            // Fetch medication info from OpenFDA
            fetch(`https://api.fda.gov/drug/label.json?search=openfda.brand_name:"${query}"`)
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

    useEffect(() => {
        if (error) {
            toast.error(error, {
                onClose: () => {
                    // 自动返回上一页
                    navigate(-1);  // 使用 react-router 的 navigate 方法返回上一页
                }
            }); // Show error notification and handle onClose
        }
    }, [error, navigate]); // 监听错误和 navigate 变化

    // Only show the loading message if a search has been made
    if (!medicationInfo && query) {
        return (
            <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100px' }}>
                <Spinner animation="border" role="status">
                    <span className="visually-hidden">Loading...</span>
                </Spinner>
            </div>
        );
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
                <div style={{ textAlign: 'center' }}>
                    <Card.Text>
                        <strong>{t('Name')}:</strong> {medicationInfo?.results?.[0]?.openfda?.brand_name?.[0] || 'N/A'}
                    </Card.Text>
                    <Card.Text>
                        <strong>{t('Purpose')}:</strong> {medicationInfo?.results?.[0]?.purpose?.[0] || 'N/A'}
                    </Card.Text>
                    <Card.Text>
                        <strong>{t('Description')}:</strong> {medicationInfo?.results?.[0]?.description?.[0] || 'N/A'}
                    </Card.Text>
                </div>
            </Card.Body>
        </Card>
    );
};

export default MedicationDescriptionPage;
