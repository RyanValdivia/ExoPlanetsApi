package com.pcn.exoplanets.auth;

import com.pcn.exoplanets.dtos.AuthRequest;
import com.pcn.exoplanets.dtos.GoogleUser;
import com.pcn.exoplanets.enums.Role;
import com.pcn.exoplanets.models.user.User;
import com.pcn.exoplanets.repositories.UserRepository;
import com.pcn.exoplanets.auth.responses.AuthResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;

    private static final String GOOGLE_TOKEN_INFO_URL = "https://www.googleapis.com/oauth2/v3/userinfo";

    public void registerUser (GoogleUser googleUser) {
        if (!userRepository.existsByEmail(googleUser.getEmail())) {
            User newUser = User.builder()
                    .email(googleUser.getEmail())
                    .name(googleUser.getName())
                    .provider("google")
                    .providerId(googleUser.getId())
                    .imageUrl(googleUser.getPicture())
                    .role(Role.USER)
                    .build();
            userRepository.save(newUser);
        } else {
            throw new RuntimeException("User already exists");
        }

    }

    public String registerAndGetToken (HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String googleToken = authHeader.substring(7);
            GoogleUser googleUser = validateGoogleToken(googleToken);
            if (userRepository.existsByEmail(googleUser.getEmail())) {
                throw new BadCredentialsException("User already exists");
            }
            registerUser(googleUser);
            return jwtService.generateToken(googleUser.getEmail());
        }
        throw new RuntimeException("Token not found");
    }

    public String loginAndGetToken (HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String googleToken = authHeader.substring(7);
            GoogleUser googleUser = validateGoogleToken(googleToken);
            if (userRepository.existsByEmail(googleUser.getEmail())) {
                return jwtService.generateToken(googleUser.getEmail());
            } else {
                throw new BadCredentialsException("User does not exist");
            }
        }
        throw new RuntimeException("Token not found");
    }

    private GoogleUser validateGoogleToken(String token) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<GoogleUser> response = restTemplate.exchange(GOOGLE_TOKEN_INFO_URL, HttpMethod.GET, entity, GoogleUser.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to fetch user info from Google");
        }
    }
}
