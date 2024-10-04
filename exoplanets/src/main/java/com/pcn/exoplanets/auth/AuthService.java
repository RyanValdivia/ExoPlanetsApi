package com.pcn.exoplanets.auth;

import com.pcn.exoplanets.dtos.GoogleUser;
import com.pcn.exoplanets.enums.Role;
import com.pcn.exoplanets.models.user.User;
import com.pcn.exoplanets.repositories.UserRepository;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;

    private static final Dotenv dotenv = Dotenv.load();

    private static final String GOOGLE_TOKEN_INFO_URL = "https://www.googleapis.com/oauth2/v3/userinfo";
    private static final String CODE_URL = "https://oauth2.googleapis.com/token";

    public String registerAndGetToken (GoogleUser googleUser) {
        User newUser = User.builder()
                .email(googleUser.getEmail())
                .name(googleUser.getName())
                .provider("google")
                .providerId(googleUser.getId())
                .imageUrl(googleUser.getPicture())
                .role(Role.USER)
                .build();
        userRepository.save(newUser);
        return jwtService.generateToken(googleUser.getEmail());
    }

    public String loginAndGetToken (GoogleUser googleUser) {
        return jwtService.generateToken(googleUser.getEmail());
    }


    public String exchangeCodeForToken (String code) throws Exception  {
        code = URLDecoder.decode(code, StandardCharsets.UTF_8);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("code", code);
        params.add("client_id", dotenv.get("GOOGLE_CLIENT_ID"));
        params.add("client_secret", dotenv.get("GOOGLE_CLIENT_SECRET"));
        params.add("redirect_uri", dotenv.get("GOOGLE_REDIRECT_URI"));
        params.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<Map> response = restTemplate.exchange(CODE_URL, HttpMethod.POST, request, Map.class);

        if (response.getBody() != null && response.getBody().containsKey("access_token")) {
            return response.getBody().get("access_token").toString();
        } else {
            throw new RuntimeException("Failed to exchange code for access token");
        }
    }

    public String handleGoogleToken (String token) {
        GoogleUser googleUser = validateGoogleToken(token);
        if (userExists(googleUser.getEmail())) {
            return loginAndGetToken(googleUser);
        } else {
            return registerAndGetToken(googleUser);
        }
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

    public boolean userExists (String email) {
        return userRepository.existsByEmail(email);
    }


}
