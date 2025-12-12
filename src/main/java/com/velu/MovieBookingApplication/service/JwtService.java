package com.velu.MovieBookingApplication.service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

import java.util.Map;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long jwtexpiration;

    public String extractUsername(String jwtToken){
        return extractClaim(jwtToken, Claims::getSubject);
    }

    private <T> T extractClaim(String jwtToken, Function<Claims,T> claimResolver){

       final Claims claims = extractAllCliams(jwtToken);
       return claimResolver.apply(claims);
    }

    private Claims extractAllCliams(String jwtToken) {

        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();

    }

    public SecretKey getSignInKey() {

        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

    }


    public String generateToken(UserDetails userDetails){
         return generateToken(new HashMap<>(),userDetails);
    }

    public String generateToken(Map<String,Object> extractClaims,UserDetails userDetails){

        return Jwts.builder()
                .claims(extractClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtexpiration))
                .signWith(getSignInKey())
                .compact();

    }

    public boolean isTokenValid(String token , UserDetails userDetails){

        final String username = extractUsername(token);

        return (userDetails.getUsername().equals(username) && !isTokenExpired(token));
        
    }

    private boolean isTokenExpired(String token) {

        return extractExpiration(token).before(new Date());

    }

    private Date extractExpiration(String token) {

        return extractClaim(token,Claims::getExpiration);

    }
}
