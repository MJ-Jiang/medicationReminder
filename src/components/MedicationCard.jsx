import React from 'react';
import { Card } from 'react-bootstrap';

const MedicationCard = ({ medication }) => {
    // 合并 indications_and_usage 和 when_using 字段内容
    const description = `${medication.indications_and_usage ? medication.indications_and_usage[0] : 'N/A'}. ${medication.when_using ? medication.when_using[0] : 'N/A'}.`;
    return (
        <Card>
            <Card.Body>
                {/* 显示品牌名 */}
                <Card.Title>{medication.openfda.brand_name ? medication.openfda.brand_name[0] : 'N/A'}</Card.Title>
                
                {/* 保留 Purpose 字段 */}
                <Card.Text>
                    <strong>Purpose: </strong> 
                    {medication.purpose ? medication.purpose[0] : 'N/A'}
                </Card.Text>

                {/* 显示 Description，内容为 Indications and Usage + When Using */}
                <Card.Text>
                    <strong>Description: </strong> 
                    {description}
                </Card.Text>
            </Card.Body>
        </Card>
    );
};

export default MedicationCard;
