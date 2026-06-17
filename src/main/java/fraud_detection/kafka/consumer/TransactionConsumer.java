package fraud_detection.kafka.consumer;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fraud_detection.entity.FraudAlert;
import fraud_detection.entity.Transaction;
import fraud_detection.enums.AlertSeverity;
import fraud_detection.enums.AlertStatus;
import fraud_detection.enums.TransactionStatus;
import fraud_detection.repository.FraudAlertRepository;
import fraud_detection.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TransactionConsumer {

    private final TransactionRepository transactionRepository;
    private final FraudAlertRepository fraudAlertRepository;
    private final ObjectMapper objectMapper;

    // Fraud rule thresholds
    private static final BigDecimal HIGH_AMOUNT_THRESHOLD = new BigDecimal("100000");
    private static final int VELOCITY_LIMIT = 5;
    private static final int VELOCITY_WINDOW_MINUTES = 10;

    public TransactionConsumer(TransactionRepository transactionRepository,
                               FraudAlertRepository fraudAlertRepository) {
        this.transactionRepository = transactionRepository;
        this.fraudAlertRepository = fraudAlertRepository;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @KafkaListener(
            topics = "${kafka.topic.transactions:transactions.raw}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    @Transactional
    public void consumeTransaction(String message) {
        try {
            Transaction transaction = objectMapper.readValue(message, Transaction.class);
            log.info("Consumed transaction: {}", transaction.getTransactionRef());

            // Reload from DB to get managed entity
            Transaction managed = transactionRepository
                    .findById(transaction.getId())
                    .orElse(null);

            if (managed == null) {
                log.warn("Transaction not found in DB: {}", transaction.getId());
                return;
            }

            // Run all fraud rules
            List<FraudAlert> alerts = runFraudRules(managed);

            if (!alerts.isEmpty()) {
                fraudAlertRepository.saveAll(alerts);
                managed.setStatus(TransactionStatus.FLAGGED);
                log.warn("Transaction {} FLAGGED — {} alert(s) raised",
                        managed.getTransactionRef(), alerts.size());
            } else {
                managed.setStatus(TransactionStatus.APPROVED);
                log.info("Transaction {} APPROVED", managed.getTransactionRef());
            }

            transactionRepository.save(managed);

        } catch (Exception e) {
            log.error("Error processing transaction from Kafka: {}", e.getMessage());
        }
    }

    private List<FraudAlert> runFraudRules(Transaction transaction) {
        List<FraudAlert> alerts = new ArrayList<>();

        // Rule 1 — High amount check
        if (transaction.getAmount().compareTo(HIGH_AMOUNT_THRESHOLD) > 0) {
            alerts.add(FraudAlert.builder()
                    .transaction(transaction)
                    .alertType("HIGH_AMOUNT")
                    .severity(AlertSeverity.HIGH)
                    .description(String.format(
                            "Transaction amount ₹%s exceeds threshold ₹%s",
                            transaction.getAmount(), HIGH_AMOUNT_THRESHOLD))
                    .status(AlertStatus.OPEN)
                    .build());
            log.warn("Rule triggered: HIGH_AMOUNT for {}", transaction.getTransactionRef());
        }

        // Rule 2 — Velocity check (too many transactions in short window)
        LocalDateTime since = LocalDateTime.now().minusMinutes(VELOCITY_WINDOW_MINUTES);
        long recentCount = transactionRepository.countRecentTransactions(
                transaction.getSenderAccount(), since);

        if (recentCount > VELOCITY_LIMIT) {
            alerts.add(FraudAlert.builder()
                    .transaction(transaction)
                    .alertType("VELOCITY_BREACH")
                    .severity(AlertSeverity.CRITICAL)
                    .description(String.format(
                            "Sender %s made %d transactions in last %d minutes",
                            transaction.getSenderAccount(), recentCount, VELOCITY_WINDOW_MINUTES))
                    .status(AlertStatus.OPEN)
                    .build());
            log.warn("Rule triggered: VELOCITY_BREACH for {}", transaction.getTransactionRef());
        }

        // Rule 3 — Same sender and receiver
        if (transaction.getSenderAccount().equals(transaction.getReceiverAccount())) {
            alerts.add(FraudAlert.builder()
                    .transaction(transaction)
                    .alertType("SELF_TRANSFER")
                    .severity(AlertSeverity.MEDIUM)
                    .description("Sender and receiver accounts are identical")
                    .status(AlertStatus.OPEN)
                    .build());
            log.warn("Rule triggered: SELF_TRANSFER for {}", transaction.getTransactionRef());
        }

        return alerts;
    }
}
