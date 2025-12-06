package com.velu.MovieBookingApplication.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    private Long jwtexpiration;

    public String extractUsername(String jwtToken){
        return extractClaim(jwtToken,Claims);
    }

}
