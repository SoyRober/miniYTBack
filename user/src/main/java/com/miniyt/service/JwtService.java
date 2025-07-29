package com.miniyt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.miniyt.component.TokenProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {
    private final TokenProperties tokenProperties;
    private final Algorithm algorithm;
    private final JWTVerifier verifier;

    public String extractUsername(String token) {
        return decodeToken(token).getSubject();
    }

    public String generateToken(String username, String role, String email) {
        log.info("Generating JWT token for user: {}", username);
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date expiry = new Date(nowMillis + tokenProperties.getExpiration());
        log.info("Token will expire at: {}", expiry);

        return JWT.create()
                .withSubject(username)
                .withClaim("email", email)
                .withClaim("role", role)
                .withIssuedAt(now)
                .withExpiresAt(expiry)
                .sign(algorithm);
    }

    public boolean isTokenValid(String token, String username) {
        try {
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getSubject().equals(username) && jwt.getExpiresAt().after(new Date());
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    private DecodedJWT decodeToken(String token) {
        return verifier.verify(token);
    }
}
