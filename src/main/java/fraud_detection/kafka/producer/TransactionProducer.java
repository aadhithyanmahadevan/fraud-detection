package fraud_detection.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fraud_detection.entity.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TransactionProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${kafka.topic.transactions:transactions.raw}")
    private String transactionTopic;

    public TransactionProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public void sendTransaction(Transaction transaction) {
        try {
            String payload = objectMapper.writeValueAsString(transaction);

            kafkaTemplate.send(transactionTopic,
                            transaction.getId().toString(),
                            payload)
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            log.info("Transaction {} sent to Kafka topic [{}] partition [{}]",
                                    transaction.getTransactionRef(),
                                    result.getRecordMetadata().topic(),
                                    result.getRecordMetadata().partition());
                        } else {
                            log.error("Failed to send transaction {} to Kafka: {}",
                                    transaction.getTransactionRef(), ex.getMessage());
                        }
                    });

        } catch (JsonProcessingException e) {
            log.error("Failed to serialize transaction: {}", e.getMessage());
        }
    }
}
