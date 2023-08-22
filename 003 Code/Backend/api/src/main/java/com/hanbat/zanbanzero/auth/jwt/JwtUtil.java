package com.hanbat.zanbanzero.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.hanbat.zanbanzero.auth.login.userDetails.UserDetailsInterface;
import com.hanbat.zanbanzero.exception.exceptions.JwtTokenException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

public class JwtUtil {

    public static String createToken(UserDetailsInterface userDetails) {
        return JWT.create()
                .withSubject(JwtTemplate.TOKEN_PREFIX)
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtTemplate.EXPIRATION_TIME))
                .withClaim("id", userDetails.getMemberId())
                .withClaim("username", userDetails.getUsername())
                .withClaim("roles", userDetails.getMemberRoles())
                .sign(Algorithm.HMAC256(JwtTemplate.SECRET));
    }

    public static String createRefreshToken(UserDetailsInterface userDetails) {
        return JWT.create()
                .withSubject(JwtTemplate.TOKEN_PREFIX)
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtTemplate.EXPIRATION_TIME_REFRESH))
                .withClaim("username", userDetails.getUsername())
                .withClaim("type", JwtTemplate.REFRESH_TYPE)
                .sign(Algorithm.HMAC256(JwtTemplate.SECRET));
    }

    public static String getUsernameFromToken(String token) {
        token = token.replace(JwtTemplate.TOKEN_PREFIX, "");

        String username = JWT.require(Algorithm.HMAC256(JwtTemplate.SECRET)).build().verify(token).getClaim("username").asString();
        return username;
    }

    public static String getTypeFromRefreshToken(String token){
        token = token.replace(JwtTemplate.TOKEN_PREFIX, "");

        String username = JWT.require(Algorithm.HMAC256(JwtTemplate.SECRET)).build().verify(token).getClaim("type").asString();
        return username;
    }

    public static boolean isTokenExpired(String token) {
        token = token.replace(JwtTemplate.TOKEN_PREFIX, "");

        Date exp = JWT.require(Algorithm.HMAC256(JwtTemplate.SECRET)).build().verify(token).getClaim("exp").asDate();
        return exp.before(new Date());
    }

    public static String getUsernameFromRefreshToken(String token) {
        String username = getUsernameFromToken(token);
        if (username == null) throw new JwtTokenException("username can not be null");
        return username;
    }
}