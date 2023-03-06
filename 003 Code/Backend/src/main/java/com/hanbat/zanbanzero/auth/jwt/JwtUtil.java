package com.hanbat.zanbanzero.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.hanbat.zanbanzero.auth.Login.UserDetails.ManagerPrincipalDetails;
import com.hanbat.zanbanzero.auth.Login.UserDetails.UserPrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.hibernate.engine.spi.ManagedEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    public static String createToken(UserDetails userDetails, String path) {
        if (path.equals("/login/user")) {
            return JWT.create()
                    .withSubject(JwtTemplate.TOKEN_PREFIX)
                    .withExpiresAt(new Date(System.currentTimeMillis() + JwtTemplate.EXPIRATION_TIME))
                    .withClaim("id", ((UserPrincipalDetails) userDetails).getUser().getId())
                    .withClaim("username", userDetails.getUsername())
                    .withClaim("roles", "ROLE_USER")
                    .sign(Algorithm.HMAC256(JwtTemplate.SECRET));
        }
        else if (path.equals("/login/manager")) {
            return JWT.create()
                    .withSubject(JwtTemplate.TOKEN_PREFIX)
                    .withExpiresAt(new Date(System.currentTimeMillis() + JwtTemplate.EXPIRATION_TIME))
                    .withClaim("id", ((ManagerPrincipalDetails) userDetails).getManager().getId())
                    .withClaim("username", userDetails.getUsername())
                    .withClaim("roles", "ROLE_MANAGER")
                    .sign(Algorithm.HMAC256(JwtTemplate.SECRET));
        }
        else {
            throw new RuntimeException("잘못된 PATH 입니다.");
        }
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