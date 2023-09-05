package com.hanbat.zanbanzero.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.hanbat.zanbanzero.auth.login.userDetails.UserDetailsInterface;
import com.hanbat.zanbanzero.exception.exceptions.JwtTokenException;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    
    private final JwtTemplate jwtTemplate = new JwtTemplate();
    private static final String USERNAME = "username";

    public String createToken(UserDetailsInterface userDetails) {
        return JWT.create()
                .withSubject(jwtTemplate.getTokenPrefix())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtTemplate.getExpiration()))
                .withClaim("id", userDetails.getMemberId())
                .withClaim(USERNAME, userDetails.getUsername())
                .withClaim("roles", userDetails.getMemberRoles())
                .sign(Algorithm.HMAC256(jwtTemplate.getSecret()));
    }

    public String createRefreshToken(UserDetailsInterface userDetails) {
        return JWT.create()
                .withSubject(jwtTemplate.getTokenPrefix())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtTemplate.getRefreshExpiration()))
                .withClaim(USERNAME, userDetails.getUsername())
                .withClaim("type", jwtTemplate.getRefreshType())
                .sign(Algorithm.HMAC256(jwtTemplate.getSecret()));
    }

    public String getUsernameFromToken(String token) {
        token = token.replace(jwtTemplate.getTokenPrefix(), "");

        return JWT.require(Algorithm.HMAC256(jwtTemplate.getSecret())).build().verify(token).getClaim(USERNAME).asString();
    }

    public String getTypeFromRefreshToken(String token){
        token = token.replace(jwtTemplate.getTokenPrefix(), "");

        return JWT.require(Algorithm.HMAC256(jwtTemplate.getSecret())).build().verify(token).getClaim("type").asString();
    }

    public boolean isTokenExpired(String token) {
        token = token.replace(jwtTemplate.getTokenPrefix(), "");

        Date exp = JWT.require(Algorithm.HMAC256(jwtTemplate.getSecret())).build().verify(token).getClaim("exp").asDate();
        return exp.before(new Date());
    }

    public String getUsernameFromRefreshToken(String token) {
        String data = getUsernameFromToken(token);
        if (data == null) throw new JwtTokenException("username can not be null");
        return data;
    }
}