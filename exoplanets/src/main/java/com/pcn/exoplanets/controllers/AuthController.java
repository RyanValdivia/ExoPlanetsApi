package com.pcn.exoplanets.controllers;

import com.pcn.exoplanets.auth.AuthService;
import com.pcn.exoplanets.auth.responses.AuthResponse;
import com.pcn.exoplanets.dtos.AuthRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping ("/auth/google")
public class AuthController {
    private final AuthService authService;

    @GetMapping ("/register")
    public ResponseEntity<?> registerUser (HttpServletRequest request) {
        String token = authService.registerAndGetToken(request);
        AuthResponse response = AuthResponse.builder()
                .token(token)
                .message("User registered successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping ("/login")
    public ResponseEntity<?> loginUser (HttpServletRequest request) {
        String token = authService.loginAndGetToken(request);
        AuthResponse response = AuthResponse.builder()
                .token(token)
                .message("User logged in successfully")
                .build();
        return ResponseEntity.ok(response);
    }
}
