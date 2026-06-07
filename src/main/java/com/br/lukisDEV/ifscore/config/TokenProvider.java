package com.br.lukisDEV.ifscore.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class TokenProvider {

    @Value("${jwt.expiration}")
    private long expirationTime;

    @Value("${jwt.key}")
    private String key;

    public String gerarToken(Authentication auth) {
        UserDetails user = (UserDetails) auth.getPrincipal();
        return buildToken(user);
    }

    private String buildToken(UserDetails user) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationTime);

        var roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .subject(user.getUsername())
                .claim("roles", roles)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(getSigningKey())
                .compact();
    }
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(key.getBytes());
    }
    public boolean isTokenValid(String token) {
        try {
            getClaims(token);
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    public String generatedEmailVerify(String email){
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationTime);
        return Jwts.builder()
                .subject(email)
                .claim("type", "Verificação de Email")
                .issuedAt(now)
                .expiration(expiration)
                .signWith(getSigningKey())
                .compact();

    }
    public boolean isEmailVerificationToken(String token) {
        Claims claims = getClaims(token);

        return "Verificação de Email".equals(
                claims.get("type")
        );
    }
}

