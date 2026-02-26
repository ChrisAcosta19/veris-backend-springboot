package com.veris.services;

import com.veris.dto.LoginRequest;
import com.veris.dto.LoginResponse;
import com.veris.security.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthService {

    @Autowired
    private JwtUtils jwtUtils;

    // Hardcoded credentials as per requirements
    private static final String VALID_USERNAME = "VERIS";
    private static final String VALID_PASSWORD = "PRUEBAS123";

    public LoginResponse login(LoginRequest request) throws Exception {
        if (request.getUsername() == null || request.getPassword() == null) {
            throw new IllegalArgumentException("Username and password are required");
        }

        if (!VALID_USERNAME.equals(request.getUsername()) || !VALID_PASSWORD.equals(request.getPassword())) {
            throw new Exception("Authentication failed. Invalid username or password.");
        }

        String token = jwtUtils.generateToken(request.getUsername());

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setType("Bearer");
        response.setExpiresIn((int) jwtUtils.getExpirationTime());
        response.setUsername(request.getUsername());

        return response;
    }
}
