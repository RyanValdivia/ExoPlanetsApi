package com.pcn.exoplanets.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {
    private final Dotenv dotenv = Dotenv.load();
    private final String SECRET_KEY = dotenv.get("SECRET_KEY");

    public String generateToken (String email) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

        Date now = new Date(System.currentTimeMillis());
        Date expiration = new Date(now.getTime() + 1000 * 60 * 60 * 24);

        return JWT.create()
                .withIssuer("issuer-app")
                .withSubject(email)
                .withIssuedAt(now)
                .withExpiresAt(expiration)
                .sign(algorithm);
    }

    public String validateAndGetSubject (String token) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        return JWT.require(algorithm)
                .withIssuer("issuer-app")
                .build()
                .verify(token)
                .getSubject();
    }
}
