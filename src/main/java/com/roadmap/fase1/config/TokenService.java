package com.roadmap.fase1.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.roadmap.fase1.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TokenService {
    @Value("${token.secret}")
    private String secret;
    @Value("${token.expiration}")
    private Integer expirationToken;
    public  String generateToken(User user) {

        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("rest-jwt-oauth-filas")
                    .withSubject(user.getUsername())
                    .withExpiresAt(getExpiration()).sign(algorithm);
        } catch (JWTCreationException exception){
            throw new RuntimeException("Could not generate token", exception);
        }
    }

    public String getUsernameFromToken(String token) {
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("rest-jwt-oauth-filas").build()
                    .verify(token).getSubject();
        } catch (JWTVerificationException exception){
            return "";
        }
    }

    public Instant getExpiration() {
        return Instant.now().plusSeconds(expirationToken);
    }
}
