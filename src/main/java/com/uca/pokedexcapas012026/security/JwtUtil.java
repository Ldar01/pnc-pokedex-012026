package com.uca.pokedexcapas012026.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private String expirationTime;


    public Key getKey() {
       return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + Long.parseLong(expirationTime));

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getKey())
                .compact();
        return token;
    }

    public String getUsernameFromToken(String token) {
        String username = Jwts.parser()
                .setSigningKey(getKey())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        return username;
    }

    public boolean validateToken(String token) {
        Jwts.parser()
                .setSigningKey(getKey())
                .parse(token);
        return true;
    }
}
