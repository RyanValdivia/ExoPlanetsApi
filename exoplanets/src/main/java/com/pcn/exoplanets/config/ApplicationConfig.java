package com.pcn.exoplanets.config;

import com.pcn.exoplanets.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
@Configuration
public class ApplicationConfig {
    private final UserRepository userRepository;

    @Bean
    public AuthenticationManager authenticationManager (
            AuthenticationConfiguration config
    ) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider () {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(getUserDetailsService());
        return provider;
    }

    @Bean
    public UserDetailsService getUserDetailsService () {
        return email ->
                userRepository.findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException(email));
    }
}
