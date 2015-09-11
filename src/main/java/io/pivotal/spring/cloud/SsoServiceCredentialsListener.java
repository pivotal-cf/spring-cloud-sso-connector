package io.pivotal.spring.cloud;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudException;
import org.springframework.cloud.CloudFactory;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class SsoServiceCredentialsListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    private static final String PROPERTY_SOURCE_NAME = "vcapPivotalSso";
    private static final String SPRING_OAUTH2_CLIENT_ID = "spring.oauth2.client.clientId";
    private static final String SPRING_OAUTH2_CLIENT_SECRET = "spring.oauth2.client.clientSecret";
    private static final String SPRING_OAUTH2_AUTHORIZE_URI = "spring.oauth2.client.userAuthorizationUri";
    private static final String SPRING_OAUTH2_KEY_URI = "spring.oauth2.resource.jwt.keyUri";
    private static final String SPRING_OAUTH2_ACCESS_TOKEN_URI = "spring.oauth2.client.accessTokenUri";
    private static final String PIVOTAL_SSO_TARGET = "pivotal-sso.target";
    private static final String SPRING_OAUTH2_USER_INFO_URI = "spring.oauth2.resource.userInfoUri";
    private static final String SPRING_OAUTH2_TOKEN_INFO_URI = "spring.oauth2.resource.tokenInfoUri";

    private Cloud cloud;

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        if (cloud != null) {
            return;
        }

        try {
            cloud = new CloudFactory().getCloud();
        } catch (CloudException e) {
            // not running on a known cloud environment, so nothing to do
            return;
        }

        for (ServiceInfo serviceInfo : cloud.getServiceInfos()) {
            if (serviceInfo instanceof SsoServiceInfo) {
                Map<String, Object> map = new HashMap<String, Object>();
                SsoServiceInfo ssoServiceInfo = (SsoServiceInfo) serviceInfo;
                map.put(SPRING_OAUTH2_CLIENT_ID, ssoServiceInfo.getClientId());
                map.put(SPRING_OAUTH2_CLIENT_SECRET, ssoServiceInfo.getClientSecret());
                map.put(SPRING_OAUTH2_ACCESS_TOKEN_URI, ssoServiceInfo.getAuthDomain() + "/oauth/token");
                map.put(SPRING_OAUTH2_AUTHORIZE_URI, ssoServiceInfo.getAuthDomain() + "/oauth/authorize");
                map.put(SPRING_OAUTH2_KEY_URI, ssoServiceInfo.getAuthDomain() + "/token_key");
                map.put(PIVOTAL_SSO_TARGET, ssoServiceInfo.getAuthDomain());
                map.put(SPRING_OAUTH2_USER_INFO_URI, ssoServiceInfo.getAuthDomain() + "/userinfo");
                map.put(SPRING_OAUTH2_TOKEN_INFO_URI, ssoServiceInfo.getAuthDomain() + "/check_token");
                MapPropertySource mapPropertySource = new MapPropertySource(PROPERTY_SOURCE_NAME, map);

                event.getEnvironment().getPropertySources().addFirst(mapPropertySource);
            }
        }
    }
}
