package fraud_detection.controller;

import fraud_detection.dto.request.TransactionRequest;
import fraud_detection.dto.response.ApiResponse;
import fraud_detection.dto.response.TransactionResponse;
import fraud_detection.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "Submit and query transactions")
@SecurityRequirement(name = "bearerAuth")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    @Operation(summary = "Submit a new transaction for fraud analysis")
    public ResponseEntity<ApiResponse<TransactionResponse>> submit(
            @Valid @RequestBody TransactionRequest request) {

        TransactionResponse response = transactionService.submitTransaction(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Transaction submitted successfully", response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get transaction by ID")
    public ResponseEntity<ApiResponse<TransactionResponse>> getById(
            @PathVariable UUID id) {

        TransactionResponse response = transactionService.getTransaction(id);
        return ResponseEntity
                .ok(ApiResponse.success("Transaction fetched", response));
    }

    @GetMapping
    @Operation(summary = "Get all transactions")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getAll() {

        List<TransactionResponse> response = transactionService.getAllTransactions();
        return ResponseEntity
                .ok(ApiResponse.success("Transactions fetched", response));
    }

    @GetMapping("/sender/{senderAccount}")
    @Operation(summary = "Get transactions by sender account")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getBySender(
            @PathVariable String senderAccount) {

        List<TransactionResponse> response =
                transactionService.getTransactionsBySender(senderAccount);
        return ResponseEntity
                .ok(ApiResponse.success("Transactions fetched", response));
    }
}
