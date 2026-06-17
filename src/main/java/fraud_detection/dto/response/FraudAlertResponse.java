package fraud_detection.dto.response;


import fraud_detection.enums.AlertSeverity;
import fraud_detection.enums.AlertStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class FraudAlertResponse {

    private UUID id;
    private UUID transactionId;
    private String transactionRef;
    private String alertType;
    private AlertSeverity severity;
    private String description;
    private AlertStatus status;
    private String reviewedBy;
    private LocalDateTime reviewedAt;
    private LocalDateTime createdAt;
}
