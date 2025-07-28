package com.miniyt.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.miniyt.component.TokenProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class JwtConfig {

    private final TokenProperties tokenProperties;

    @Bean
    public Algorithm jwtAlgorithm() {
        String secret = tokenProperties.getSecret();
        if (secret == null || secret.isEmpty()) {
            throw new IllegalArgumentException("JWT secret must not be null or empty. Please configure 'token.secret' in your application properties.");
        }
        return Algorithm.HMAC256(secret);
    }

    @Bean
    public JWTVerifier jwtVerifier(Algorithm algorithm) {
        return JWT.require(algorithm)
                        .build();
    }
}