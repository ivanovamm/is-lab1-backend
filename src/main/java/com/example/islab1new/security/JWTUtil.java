package com.example.islab1new.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import jakarta.enterprise.context.ApplicationScoped;
import java.security.Key;
import java.util.Date;
import java.util.Map;

@ApplicationScoped
public class JWTUtil {

    private final Key secretKey = Keys.hmacShaKeyFor("9W6BJ9ITw20rjZHciqS4QfTP9G0bl05U1J77T5Grkp0".getBytes());
    private final long expirationTime = 3600000;

    public String generateToken(String username, String password) {
        return Jwts.builder()
                .setSubject(username) // Задаем username как subject
                .claim("password", password) // Добавляем роль как claim
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(SignatureAlgorithm.HS256, secretKey)
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
