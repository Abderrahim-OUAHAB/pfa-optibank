package com.bank_transactions.auth.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtService {

    // Use a stronger secret key (at least 256 bits/32 characters)
    private final String secret = "ThisIsASecretKeyWithAtLeast256BitsLength1234567890";
    private final TokenBlacklistService tokenBlacklistService = new TokenBlacklistService();
    // Create a proper secret key
    private final SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractEmail(String token) {
         if (tokenBlacklistService.isBlacklisted(token)) {
        throw new JwtException("Token is invalidated");
    }
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}