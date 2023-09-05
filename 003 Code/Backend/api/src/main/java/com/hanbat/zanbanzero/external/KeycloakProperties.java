package com.hanbat.zanbanzero.external;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties("my.keycloak")
public class KeycloakProperties {
    private final String host;
    private final String realmName;
    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;

    public KeycloakProperties(String host, String realmName, String clientId, String clientSecret, String redirectUri) {
        this.host = host;
        this.realmName = realmName;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
    }
}
