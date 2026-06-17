package fraud_detection.repository;

import fraud_detection.entity.Transaction;
import fraud_detection.enums.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    Optional<Transaction> findByTransactionRef(String transactionRef);

    List<Transaction> findBySenderAccount(String senderAccount);

    List<Transaction> findByStatus(TransactionStatus status);

    // Count transactions from same sender within a time window
    // Used for velocity check fraud rule
    @Query("SELECT COUNT(t) FROM Transaction t " +
            "WHERE t.senderAccount = :senderAccount " +
            "AND t.createdAt >= :since")
    long countRecentTransactions(
            @Param("senderAccount") String senderAccount,
            @Param("since") LocalDateTime since
    );

    // Find high value transactions above a threshold
    @Query("SELECT t FROM Transaction t " +
            "WHERE t.amount >= :threshold " +
            "AND t.status = :status")
    List<Transaction> findHighValueTransactions(
            @Param("threshold") BigDecimal threshold,
            @Param("status") TransactionStatus status
    );
}
