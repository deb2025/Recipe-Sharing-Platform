package com.recipe.token;

import io.jsonwebtoken.*;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "your secret key"; // Use a more secure key

    // Method to generate the JWT token
    public String generateToken(String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles",role); 
        return createToken(claims, username);
    }

    // Method to create a JWT token with claims and username
    private String createToken(Map<String, Object> claims, String username) {
        Key key = new SecretKeySpec(SECRET_KEY.getBytes(), SignatureAlgorithm.HS256.getJcaName()); // Create a Key from the secret
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour expiration
                .signWith(key, SignatureAlgorithm.HS256) // Sign the token using the Key
                .compact();
    }

    // Extract username (subject) from the JWT token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract roles from the JWT token
    public String extractRoles(String token) {
        return extractClaim(token, claims -> claims.get("roles", String.class)); 
    }

    // Check if the token is valid (not expired)
    public boolean isTokenValid(String token) {
        return !isTokenExpired(token);
    }

    // Check if the token has expired
    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    // Extract a claim from the JWT token
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extract all claims from the JWT token
    private Claims extractAllClaims(String token) {
        Key key = new SecretKeySpec(SECRET_KEY.getBytes(), SignatureAlgorithm.HS256.getJcaName()); // Create a Key for verification
        return Jwts.parserBuilder() // Use parserBuilder() instead of parser()
                .setSigningKey(key) // Set the signing key for verification
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
   

    

    

    
}

