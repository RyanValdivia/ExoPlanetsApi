package com.pcn.exoplanets.controllers;

import com.pcn.exoplanets.auth.AuthService;
import com.pcn.exoplanets.auth.responses.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping ("/auth/google")
public class AuthController {
    private final AuthService authService;

    @GetMapping ("/code")
    public ResponseEntity<?> handleGoogleCode (@RequestParam ("code") String code) throws Exception {
        String googleToken = authService.exchangeCodeForToken(code);
        String token = authService.handleGoogleToken(googleToken);
        AuthResponse response = AuthResponse.builder()
                .message("User authenticated successfully")
                .token(token)
                .build();
        return ResponseEntity.ok(response);
    }
}
