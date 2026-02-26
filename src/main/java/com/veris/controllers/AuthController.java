package com.veris.controllers;

import com.veris.dto.ApiResponse;
import com.veris.dto.LoginRequest;
import com.veris.dto.LoginResponse;
import com.veris.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@Slf4j
@RestController
@RequestMapping("/autenticacion")
@Tag(name = "Autenticación", description = "Endpoints para autenticación")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Login con credenciales básicas", description = "Obtiene un token JWT usando Basic Auth")
    @SecurityRequirement(name = "basicAuth")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            LoginRequest request = null;

            if (authHeader != null && authHeader.startsWith("Basic ")) {
                // Extract credentials from Basic Auth header
                String credentials = authHeader.substring(6);
                String decodedCredentials = new String(Base64.getDecoder().decode(credentials));
                String[] parts = decodedCredentials.split(":");

                request = new LoginRequest();
                request.setUsername(parts.length > 0 ? parts[0] : null);
                request.setPassword(parts.length > 1 ? parts[1] : null);
            }

            if (request == null) {
                return ResponseEntity.status(401).body(
                        ApiResponse.<LoginResponse>builder()
                                .code(401)
                                .success(false)
                                .message("Authentication failed. Invalid username or password.")
                                .errorData(new Object())
                                .build());
            }

            LoginResponse response = authService.login(request);

            return ResponseEntity.ok(
                    ApiResponse.<LoginResponse>builder()
                            .code(200)
                            .success(true)
                            .message("Authentication successful")
                            .data(response)
                            .build());
        } catch (Exception e) {
            log.error("Login error: {}", e.getMessage());
            return ResponseEntity.status(401).body(
                    ApiResponse.<LoginResponse>builder()
                            .code(401)
                            .success(false)
                            .message("Authentication failed. Invalid username or password.")
                            .errorData(new Object())
                            .build());
        }
    }
}
