package fraud_detection.service;


import fraud_detection.dto.request.TransactionRequest;
import fraud_detection.dto.response.TransactionResponse;
import fraud_detection.entity.Transaction;
import fraud_detection.enums.TransactionStatus;
import fraud_detection.exception.ResourceNotFoundException;
import fraud_detection.kafka.producer.TransactionProducer;
import fraud_detection.repository.TransactionRepository;
import fraud_detection.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionProducer transactionProducer;

    @Override
    @Transactional
    public TransactionResponse submitTransaction(TransactionRequest request) {

        // Generate unique transaction reference
        String txRef = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Transaction transaction = Transaction.builder()
                .transactionRef(txRef)
                .senderAccount(request.getSenderAccount())
                .receiverAccount(request.getReceiverAccount())
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .transactionType(request.getTransactionType())
                .status(TransactionStatus.PENDING)
                .ipAddress(request.getIpAddress())
                .deviceId(request.getDeviceId())
                .location(request.getLocation())
                .build();

        Transaction saved = transactionRepository.save(transaction);

        // Publish to Kafka for async fraud analysis
        transactionProducer.sendTransaction(saved);
        log.info("Transaction {} submitted and sent to Kafka", txRef);

        return mapToResponse(saved);
    }

    @Override
    public TransactionResponse getTransaction(UUID id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Transaction not found with id: " + id));
        return mapToResponse(transaction);
    }

    @Override
    public List<TransactionResponse> getAllTransactions() {
        return transactionRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<TransactionResponse> getTransactionsBySender(String senderAccount) {
        return transactionRepository.findBySenderAccount(senderAccount)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private TransactionResponse mapToResponse(Transaction t) {
        return TransactionResponse.builder()
                .id(t.getId())
                .transactionRef(t.getTransactionRef())
                .senderAccount(t.getSenderAccount())
                .receiverAccount(t.getReceiverAccount())
                .amount(t.getAmount())
                .currency(t.getCurrency())
                .transactionType(t.getTransactionType())
                .status(t.getStatus())
                .ipAddress(t.getIpAddress())
                .location(t.getLocation())
                .createdAt(t.getCreatedAt())
                .build();
    }
}
