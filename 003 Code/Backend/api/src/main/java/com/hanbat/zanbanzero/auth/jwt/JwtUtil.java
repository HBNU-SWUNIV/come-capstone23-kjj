package com.hanbat.zanbanzero.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.hanbat.zanbanzero.auth.login.userDetails.UserDetailsInterface;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
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

    public static String createRefreshToken(UserDetails userDetails) {
        return JWT.create()
                .withSubject(JwtTemplate.TOKEN_PREFIX)
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtTemplate.EXPIRATION_TIME_REFRESH))
                .withClaim("hash", userDetails.getUsername().hashCode())
                .sign(Algorithm.HMAC256(JwtTemplate.SECRET));
    }

    public String getUsernameFromToken(String token) {
        token = token.replace(JwtTemplate.TOKEN_PREFIX, "");

        String username = JWT.require(Algorithm.HMAC256(JwtTemplate.SECRET)).build().verify(token).getClaim("username").asString();
        return username;
    }

    public Boolean checkJwt(String username, String token) {
        if (!username.equals(getUsernameFromToken(token))) {
            return false;
        }
        return true;
    }

    // 만료되었는지
    private boolean isTokenExpired(String token) {
        Date exp = JWT.require(Algorithm.HMAC256(JwtTemplate.SECRET)).build().verify(token).getClaim("exp").asDate();
        return exp.before(new Date());
    }
}