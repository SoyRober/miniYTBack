package com.miniyt.component;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class TokenProperties {

    @Value("${token.secret}")
    private String secret;

    @Value("${token.expiration}")
    private long expiration;

    @Value("${app.jwt.refresh-expiration-minutes}")
    private long refreshTokenExpirationMinutes;
}
