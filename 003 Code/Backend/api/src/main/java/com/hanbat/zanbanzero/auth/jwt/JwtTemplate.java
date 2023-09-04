package com.hanbat.zanbanzero.auth.jwt;

public interface JwtTemplate {
    String SECRET = "capston2023kjj";
    int EXPIRATION_TIME = 30;
    long EXPIRATION_TIME_REFRESH = 30;
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
    String REFRESH_HEADER_STRING = "Refresh_Token";
    String REFRESH_TYPE = "Refresh";
}
