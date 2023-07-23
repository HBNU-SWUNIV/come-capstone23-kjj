package com.hanbat.zanbanzero.external;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties("my.keycloak")
public class KeycloakProperties {
    private String host;
    private String realmName;
    private String clientId;
    private String clientSecret;
    private String redirectUri;

    public KeycloakProperties(String host, String realmName, String clientId, String clientSecret, String redirectUri) {
        this.host = host;
        this.realmName = realmName;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
    }
}
