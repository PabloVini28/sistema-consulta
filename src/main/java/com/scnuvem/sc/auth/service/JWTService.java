package com.scnuvem.sc.auth.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.scnuvem.sc.user.entity.User;

@Service
public class JWTService {
    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(User user){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                .withIssuer("auth-api")
                .withSubject(user.getUsername())
                .withClaim("role", user.getRole().toString())
                .withExpiresAt(generateExpirationDate())
                .sign(algorithm);
            return token;
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error while generating token", exception);
        }
    }

    public String validateToken(String token){
        if (token == null || token.isEmpty()) {
            throw new RuntimeException("Token is empty or null");
        }
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                .withIssuer("auth-api")
                .build()
                .verify(token)
                .getSubject();
        } catch (JWTVerificationException e) {
            
            if (e.getMessage().contains("expired")) {
                throw new RuntimeException("Token has expired");
            } else if (e.getMessage().contains("signature")) {
                throw new RuntimeException("Invalid token signature");
            }
            throw new RuntimeException("Invalid token: " + e.getMessage());
        }
    }

    private Instant generateExpirationDate(){
        return LocalDateTime.now().plusMinutes(60).toInstant(ZoneOffset.of("-03:00"));
    }
}
