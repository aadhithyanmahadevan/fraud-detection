package fraud_detection.service;

import fraud_detection.dto.request.LoginRequest;
import fraud_detection.dto.request.RegisterRequest;
import fraud_detection.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}