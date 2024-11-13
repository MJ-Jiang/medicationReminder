import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom'; // 导入 useNavigate
import BackButton from '../components/BackButton';
import { useTranslation } from 'react-i18next';
import Spinner from 'react-bootstrap/Spinner';
import Card from 'react-bootstrap/Card';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';

const MedicationDescriptionPage = () => {
    const [medicationInfo, setMedicationInfo] = useState(null);
    const [isLoading, setIsLoading] = useState(false);  // 控制 loading 状态
    const location = useLocation();
    const query = new URLSearchParams(location.search).get('query');
    const { t, i18n } = useTranslation();
    const navigate = useNavigate(); // 获取 navigate 方法用于页面跳转

    useEffect(() => {
        if (!query) {
            return;
        }

        // 设置加载状态为 true
        setIsLoading(true);

        // 请求数据
        fetch(`https://api.fda.gov/drug/label.json?search=openfda.brand_name:"${query}"`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                // 请求成功且返回数据
                if (data.results && data.results.length > 0) {
                    setMedicationInfo(data);
                } else {
                    setMedicationInfo(null); // 没有数据时处理
                }
            })
            .catch(error => {
                console.error("Error fetching medication info:", error);
            })
            .finally(() => {
                setIsLoading(false); // 请求结束，更新 loading 状态
            });

    }, [query, navigate]);

    // 查询过程中显示 loading 动画
    if (isLoading) {
        return (
            <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100px' }}>
                <Spinner animation="border" role="status">
                    <span className="visually-hidden">Loading...</span>
                </Spinner>
            </div>
        );
    }

    // 查询完成后，根据数据展示结果
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
                {medicationInfo ? (
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
                ) : (
                    // 如果没有药物信息，显示没有找到的提示
                    query && (
                        <div style={{ textAlign: 'center' }}>
                            <p>{t('No medication information found for your query.')}</p>
                        </div>
                    )
                )}
            </Card.Body>
        </Card>
    );
};

export default MedicationDescriptionPage;
