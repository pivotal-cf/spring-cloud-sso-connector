package io.pivotal.spring.cloud;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudException;
import org.springframework.cloud.CloudFactory;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

public class SsoServiceCredentialsListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {
    private Cloud cloud;

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        if (cloud != null) return;
        try {
            cloud = new CloudFactory().getCloud();
        } catch (CloudException e) {
            return; // not running on a known cloud environment, so nothing to do
        }

        for (ServiceInfo serviceInfo : cloud.getServiceInfos()) {
            if (serviceInfo instanceof SsoServiceInfo) {
                Map<String, Object> map = new HashMap<>();
                SsoServiceInfo ssoServiceInfo = (SsoServiceInfo) serviceInfo;
                map.put("security.oauth2.client.clientId", ssoServiceInfo.getClientId());
                map.put("security.oauth2.client.clientSecret", ssoServiceInfo.getClientSecret());
                map.put("security.oauth2.client.accessTokenUri", ssoServiceInfo.getAuthDomain() + "/oauth/token");
                map.put("security.oauth2.client.userAuthorizationUri", ssoServiceInfo.getAuthDomain() + "/oauth/authorize");
                map.put("ssoServiceUrl", ssoServiceInfo.getAuthDomain());
                map.put("security.oauth2.resource.userInfoUri", ssoServiceInfo.getAuthDomain() + "/userinfo");
                map.put("security.oauth2.resource.tokenInfoUri", ssoServiceInfo.getAuthDomain() + "/check_token");
                map.put("security.oauth2.resource.jwk.key-set-uri", ssoServiceInfo.getAuthDomain() + "/token_keys");
                map.put("sso.connector.cloud.available", "success");
                MapPropertySource mapPropertySource = new MapPropertySource("vcapPivotalSso", map);

                event.getEnvironment().getPropertySources().addFirst(mapPropertySource);
            }
        }
    }
}
