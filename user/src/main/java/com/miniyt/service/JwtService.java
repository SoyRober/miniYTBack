package com.miniyt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.miniyt.component.TokenProperties;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    private final TokenProperties tokenProperties;
    private final Algorithm algorithm;
    private final JWTVerifier verifier;

    public JwtService(TokenProperties tokenProperties) {
        this.tokenProperties = tokenProperties;
        this.algorithm = Algorithm.HMAC256(tokenProperties.getSecret());
        this.verifier = JWT.require(algorithm).build();
    }

    public String extractUsername(String token) {
        return decodeToken(token).getSubject();
    }

    public String generateToken(String username, String role, String email) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date expiry = new Date(nowMillis + tokenProperties.getExpiration());

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
