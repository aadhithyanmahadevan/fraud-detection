package fraud_detection.controller;

import fraud_detection.dto.response.ApiResponse;
import fraud_detection.dto.response.FraudAlertResponse;
import fraud_detection.enums.AlertStatus;
import fraud_detection.service.FraudAlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
@Tag(name = "Fraud Alerts", description = "View and manage fraud alerts")
@SecurityRequirement(name = "bearerAuth")
public class FraudAlertController {

    private final FraudAlertService fraudAlertService;

    @GetMapping
    @Operation(summary = "Get all fraud alerts")
    public ResponseEntity<ApiResponse<List<FraudAlertResponse>>> getAll() {

        List<FraudAlertResponse> response = fraudAlertService.getAllAlerts();
        return ResponseEntity
                .ok(ApiResponse.success("Alerts fetched", response));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get alerts by status")
    public ResponseEntity<ApiResponse<List<FraudAlertResponse>>> getByStatus(
            @PathVariable AlertStatus status) {

        List<FraudAlertResponse> response = fraudAlertService.getAlertsByStatus(status);
        return ResponseEntity
                .ok(ApiResponse.success("Alerts fetched", response));
    }

    @GetMapping("/transaction/{transactionId}")
    @Operation(summary = "Get alerts by transaction ID")
    public ResponseEntity<ApiResponse<List<FraudAlertResponse>>> getByTransaction(
            @PathVariable UUID transactionId) {

        List<FraudAlertResponse> response =
                fraudAlertService.getAlertsByTransaction(transactionId);
        return ResponseEntity
                .ok(ApiResponse.success("Alerts fetched", response));
    }

    @PatchMapping("/{alertId}/status")
    @Operation(summary = "Update alert status (Analyst/Admin only)")
    public ResponseEntity<ApiResponse<FraudAlertResponse>> updateStatus(
            @PathVariable UUID alertId,
            @RequestParam AlertStatus status,
            @AuthenticationPrincipal String username) {

        FraudAlertResponse response =
                fraudAlertService.updateAlertStatus(alertId, status, username);
        return ResponseEntity
                .ok(ApiResponse.success("Alert status updated", response));
    }
}
