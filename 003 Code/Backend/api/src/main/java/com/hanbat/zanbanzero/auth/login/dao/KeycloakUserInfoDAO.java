package com.hanbat.zanbanzero.auth.login.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class KeycloakUserInfoDAO {
    private String sub;
    private boolean email_verified;
    private String[] roles;
    private String name;
    private String preferred_username;
    private String given_name;
    private String family_name;
}
