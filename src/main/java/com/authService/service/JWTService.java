package com.authService.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class JWTService {

    private static final String SECRET_KEY = "my-super-secret-key";
    private static final long EXPIRATION_TIME = 86400000;


    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    Date now = new Date();

    public String generateToken(String username, String role){

        // CSCIES
        return JWT.create()
                .withSubject(username)
                .withClaim("role", role)
                .withClaim("issuedAt", formatter.format(now))
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(SECRET_KEY));
    }

    public String validateTokenAndRetriveSubject(String token){
        return JWT.require(Algorithm.HMAC256(SECRET_KEY))
                .build()
                .verify(token)
                .getSubject();
    }
}
