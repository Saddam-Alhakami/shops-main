package com.shops.dreamshops.security.jwt;

import com.shops.dreamshops.security.ShopUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoder;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    @Value("${auth.token.jwtSecret}")
    private String jwtSecret;
    @Value("${auth.token.expirationInMils}")
    private int expirationTime;

    public String generateTokenForUser(Authentication authentication){
        ShopUserDetails userPrincipal = (ShopUserDetails) authentication.getPrincipal();

        // Extract the roles assigned to the user
        List<String> roles = userPrincipal.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        // Create the JWT token with the user details and roles
        return Jwts.builder()
                .setSubject(userPrincipal.getEmail()) // Set the email as the subject
                .claim("id", userPrincipal.getId()) // Add user ID as a claim
                .claim("roles", roles) // Add roles as a claim
                .setIssuedAt(new Date()) // Add the current date as the issue time
                .setExpiration(new Date((new Date()).getTime() + expirationTime)) // Set expiration time for the token
                .signWith(key(), SignatureAlgorithm.HS256) // Sign the token with the secret key
                .compact(); // Generate the JWT token
    }

    public String getUserNameFromToken(String token){
        return  Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    public boolean validToken(String token){
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException  | UnsupportedJwtException  | MalformedJwtException  | SignatureException | IllegalArgumentException e) {
            throw new JwtException(e.getMessage());
        }

    }

    private Key key(){
      return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
}
