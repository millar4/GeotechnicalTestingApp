package com.example.accessingdatamysql.JWT;

import java.util.Base64;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey; 

    @Value("${jwt.expiration}")
    private long validityInMilliseconds = 3600000;

    // Record the backend startup time to invalidate tokens issued before a restart.
    private final long startupTime = System.currentTimeMillis();

    // Generates a JWT token including the backend startup time as a claim.
    public String generateToken(UserDetails userDetails) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(now)
                .setExpiration(validity)
                // Add the backend startup time to the token claims.
                .claim("startup", startupTime)
                .signWith(SignatureAlgorithm.HS256, keyBytes)
                .compact();
    }

    // Extracts the username from the token.
    public String getUsernameFromToken(String token) {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        JwtParser parser = Jwts.parser().setSigningKey(keyBytes).build();
        Claims claims = parser.parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    // Validates the token by checking its signature, expiration, and startup time.
    // If the token's "startup" claim does not match the current backend startup time,
    // the token is considered invalid.
    public boolean validateToken(String token) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(secretKey);
            JwtParser parser = Jwts.parser().setSigningKey(keyBytes).build();
            Claims claims = parser.parseClaimsJws(token).getBody();
            Long tokenStartup = claims.get("startup", Long.class);
            if (tokenStartup == null || tokenStartup.longValue() != startupTime) {
                return false;
            }
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
