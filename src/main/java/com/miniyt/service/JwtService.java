package com.miniyt.service;

import com.miniyt.component.TokenProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private final com.miniyt.component.TokenProperties tokenProperties;
    private final SecretKey key;

    public JwtService(TokenProperties tokenProperties) {
        this.tokenProperties = tokenProperties;
        this.key = Keys.hmacShaKeyFor(tokenProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(key).build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String generateToken(String username, String role, String email) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", role);
        extraClaims.put("email", email);
        return createToken(extraClaims, username);
    }

    private String createToken(Map<String, Object> extraClaims, String subject) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date expiry = new Date(nowMillis + tokenProperties.getExpiration());

        return Jwts.builder()
                .claims()
                .add(extraClaims)
                .subject(subject)
                .issuedAt(now)
                .expiration(expiry)
                .and()
                .signWith(key, SIG.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, String username) {
        try {
            final String extractedUsername = extractUsername(token);
            return extractedUsername.equals(username) && !isTokenExpired(token);
        } catch (JwtException e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
