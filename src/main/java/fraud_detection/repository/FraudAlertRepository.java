package fraud_detection.repository;


import fraud_detection.entity.FraudAlert;
import fraud_detection.enums.AlertSeverity;
import fraud_detection.enums.AlertStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FraudAlertRepository extends JpaRepository<FraudAlert, UUID> {

    List<FraudAlert> findByStatus(AlertStatus status);

    List<FraudAlert> findBySeverity(AlertSeverity severity);

    List<FraudAlert> findByTransactionId(UUID transactionId);

    long countByStatus(AlertStatus status);
}
