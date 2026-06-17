package fraud_detection.service;


import fraud_detection.dto.response.FraudAlertResponse;
import fraud_detection.enums.AlertStatus;

import java.util.List;
import java.util.UUID;

public interface FraudAlertService {
    List<FraudAlertResponse> getAllAlerts();
    List<FraudAlertResponse> getAlertsByStatus(AlertStatus status);
    List<FraudAlertResponse> getAlertsByTransaction(UUID transactionId);
    FraudAlertResponse updateAlertStatus(UUID alertId, AlertStatus status, String reviewedBy);
}
