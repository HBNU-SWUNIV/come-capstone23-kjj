package com.hanbat.zanbanzero.external;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties("my.keycloak")
public class KeycloakProperties {
    private final String host;
    private final String realmName;
    private final String clientId;

    public KeycloakProperties(String host, String realmName, String clientId) {
        this.host = host;
        this.realmName = realmName;
        this.clientId = clientId;
    }
}