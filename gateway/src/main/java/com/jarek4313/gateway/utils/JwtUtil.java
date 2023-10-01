package com.jarek4313.gateway.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtUtil {

    public final String SECRET;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        SECRET = secret;
    }

    public void validateToken(final String token) {
        Jwts.parserBuilder().setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token);
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
