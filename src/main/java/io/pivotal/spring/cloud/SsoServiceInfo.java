package io.pivotal.spring.cloud;

import org.springframework.cloud.service.BaseServiceInfo;

public class SsoServiceInfo extends BaseServiceInfo {

    public static final String P_SSO_ID = "p-identity";
    private String clientId;
    private String clientSecret;
    private String authDomain;

    public SsoServiceInfo(String clientId, String clientSecret, String authDomain) {
        this(P_SSO_ID, clientId, clientSecret, authDomain);
    }

    public SsoServiceInfo(String id, String clientId, String clientSecret, String authDomain) {
        super(id);
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.authDomain = authDomain;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getAuthDomain() {
        return authDomain;
    }
}
