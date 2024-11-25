package com.example.islab1new.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import jakarta.enterprise.context.ApplicationScoped;
import java.security.Key;
import java.util.Date;
import java.util.Map;

@ApplicationScoped
public class JWTUtil {

    private final Key secretKey = Keys.hmacShaKeyFor("r3g5d8T$!v8Y7sJ@k9cLq2zZpO&1Ws#tG".getBytes());
    private final long expirationTime = 3600000;

    public String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
