package fraud_detection.service;

import fraud_detection.dto.request.TransactionRequest;
import fraud_detection.dto.response.TransactionResponse;

import java.util.List;
import java.util.UUID;

public interface TransactionService {
    TransactionResponse submitTransaction(TransactionRequest request);
    TransactionResponse getTransaction(UUID id);
    List<TransactionResponse> getAllTransactions();
    List<TransactionResponse> getTransactionsBySender(String senderAccount);
}
