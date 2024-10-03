package com.pcn.exoplanets.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OAuth2Config {
    private final Dotenv dotenv;

    public OAuth2Config() {
        this.dotenv = Dotenv.load();
    }

    @Bean
    public String getGoogleClientId() {
        return dotenv.get("GOOGLE_CLIENT_ID");
    }

    @Bean
    public String getGoogleClientSecret() {
        return dotenv.get("GOOGLE_CLIENT_SECRET");
    }

    @Bean
    public String getGoogleRedirectUri() {
        return dotenv.get("GOOGLE_REDIRECT_URI");
    }

    @Bean
    public String getJwtSecret() {
        return dotenv.get("JWT_SECRET");
    }
}
