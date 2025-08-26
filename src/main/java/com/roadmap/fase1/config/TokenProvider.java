package com.roadmap.fase1.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import com.roadmap.fase1.dto.TokenResponse;
import com.roadmap.fase1.dto.UserTokenDTO;
import com.roadmap.fase1.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
@Slf4j
@Component
public class TokenProvider {
    private static final int UNAUTHORIZED = 401;
    private final ObjectMapper objectMapper;
    @Value("${jwt.key}")
    private String jwtKey;
    @Value("${jwt.expiration-time}")
    private Integer expirationTime;

    public TokenProvider(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public TokenResponse generateToken(Authentication authentication) {
        final Date now = new Date();
        long expirationInMillis = expirationTime.longValue();
        Date expirationDate = new Date(System.currentTimeMillis() + expirationInMillis);

        final User user = getUsuario(authentication);
        Key key = Keys.hmacShaKeyFor(jwtKey.getBytes(StandardCharsets.UTF_8));

        // Criar DTO com informações essenciais do usuário
        UserTokenDTO userTokenDTO = new UserTokenDTO(
            user.getIdentifier(), 
            user.getEmail(), 
            user.getName()
        );

        try {
            String userJson = objectMapper.writeValueAsString(userTokenDTO);

            final String auth = Jwts.builder()
                    .setIssuer("WEB Token")
                    .setSubject(userJson)
                    .setIssuedAt(now)
                    .setNotBefore(now)
                    .setExpiration(expirationDate)
                    .signWith(key)
                    .compact();

            return TokenResponse.builder()
                    .token(auth)
                    .expiresIn(Long.valueOf(expirationInMillis))
                    .username(user.getUsername())
                    .build();
        } catch (JsonProcessingException e) {
            log.error("Erro ao serializar usuário para token: {}", e.getMessage());
            throw new RuntimeException("Erro interno ao gerar token", e);
        }
    }

    public boolean isValid(String jwt, ServletResponse response) throws IOException {
        try {
            jwt = extractToken(jwt);
            SignedJWT signedJWT = SignedJWT.parse(jwt);
            JWSVerifier verifier = new MACVerifier(jwtKey.getBytes(StandardCharsets.UTF_8));
            if (!signedJWT.verify(verifier)) {
                log.error("Assinatura inválida!");
                ((HttpServletResponse) response).sendError(UNAUTHORIZED);
                return false;
            }
            Jwts.parserBuilder()
                    .setSigningKey(jwtKey.getBytes(StandardCharsets.UTF_8)).build()
                    .parseClaimsJws(jwt);
            return true;
        }catch (Exception e) {
            log.error("Token inválido: {}", e.getMessage());
            ((HttpServletResponse) response).sendError(UNAUTHORIZED);
            return false;
        }
    }

    public UserTokenDTO getUserFromToken(String jwt) throws JsonProcessingException {
        jwt = extractToken(jwt);
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtKey.getBytes(StandardCharsets.UTF_8)).build()
                .parseClaimsJws(jwt).getBody();
        return objectMapper.readValue(claims.getSubject(), UserTokenDTO.class);
    }

    public User getUsuario(Authentication authentication) {
        return (User) authentication.getPrincipal();
    }

    private String extractToken(String authToken) {
        if (authToken.toLowerCase().startsWith("bearer")) {
            return authToken.substring("bearer ".length());
        }
        return authToken;
    }
}