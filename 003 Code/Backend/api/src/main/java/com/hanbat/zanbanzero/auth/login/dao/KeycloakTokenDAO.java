package com.hanbat.zanbanzero.auth.login.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class KeycloakTokenDAO {
    private String access_token;
    private int expires_in;
    private int refresh_expires_in;
    private String refresh_token;
    private String token_type;
    private String id_token;
    private int notBeforePolicy;
    private String session_state;
    private String scope;
}
