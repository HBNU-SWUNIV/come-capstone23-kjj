package com.hanbat.zanbanzero.auth.jwt;

public class JwtTemplate {
    public String getSecret() { return "capston2023kjj";}

    public int getExpiration() {
        return 30;
    }

    public String getTokenPrefix() {
        return "Bearer ";
    }

    public String getHeaderString() {
        return "Authorization";
    }

    public String getRefreshHeaderString() {
        return "Refresh_Token";
    }

    public String getRefreshType() { return "Refresh"; }
}
