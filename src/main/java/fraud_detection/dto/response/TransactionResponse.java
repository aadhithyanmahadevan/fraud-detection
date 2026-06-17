package fraud_detection.dto.response;


import fraud_detection.enums.TransactionStatus;
import fraud_detection.enums.TransactionType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class TransactionResponse {

    private UUID id;
    private String transactionRef;
    private String senderAccount;
    private String receiverAccount;
    private BigDecimal amount;
    private String currency;
    private TransactionType transactionType;
    private TransactionStatus status;
    private String ipAddress;
    private String location;
    private LocalDateTime createdAt;
}
