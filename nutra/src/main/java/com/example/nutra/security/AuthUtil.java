package com.example.nutra.security;

import com.example.nutra.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class AuthUtil {
    @Value("${jwt.secretKey}")
    private String secretkey;

    private SecretKey secretKey() {
        return Keys.hmacShaKeyFor(secretkey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(User user) {
        return Jwts.builder().setSubject(user.getUsername()).signWith(secretKey())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)).setIssuedAt(new Date())
                .claim("UserId", user.getId().toString()).compact();
    }

    public String getUsername(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(secretKey()).build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}
