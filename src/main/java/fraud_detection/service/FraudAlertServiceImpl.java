package fraud_detection.service;

import fraud_detection.dto.response.FraudAlertResponse;
import fraud_detection.entity.FraudAlert;
import fraud_detection.enums.AlertStatus;
import fraud_detection.exception.ResourceNotFoundException;
import fraud_detection.repository.FraudAlertRepository;
import fraud_detection.service.FraudAlertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FraudAlertServiceImpl implements FraudAlertService {

    private final FraudAlertRepository fraudAlertRepository;

    @Override
    @Transactional(readOnly = true)
    public List<FraudAlertResponse> getAllAlerts() {
        return fraudAlertRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FraudAlertResponse> getAlertsByStatus(AlertStatus status) {
        return fraudAlertRepository.findByStatus(status)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FraudAlertResponse> getAlertsByTransaction(UUID transactionId) {
        return fraudAlertRepository.findByTransactionId(transactionId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public FraudAlertResponse updateAlertStatus(UUID alertId,
                                                AlertStatus status,
                                                String reviewedBy) {
        FraudAlert alert = fraudAlertRepository.findById(alertId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Alert not found with id: " + alertId));

        alert.setStatus(status);
        alert.setReviewedBy(reviewedBy);
        alert.setReviewedAt(LocalDateTime.now());

        FraudAlert updated = fraudAlertRepository.save(alert);
        log.info("Alert {} updated to status {} by {}", alertId, status, reviewedBy);

        return mapToResponse(updated);
    }

    private FraudAlertResponse mapToResponse(FraudAlert a) {
        return FraudAlertResponse.builder()
                .id(a.getId())
                .transactionId(a.getTransaction().getId())
                .transactionRef(a.getTransaction().getTransactionRef())
                .alertType(a.getAlertType())
                .severity(a.getSeverity())
                .description(a.getDescription())
                .status(a.getStatus())
                .reviewedBy(a.getReviewedBy())
                .reviewedAt(a.getReviewedAt())
                .createdAt(a.getCreatedAt())
                .build();
    }
}