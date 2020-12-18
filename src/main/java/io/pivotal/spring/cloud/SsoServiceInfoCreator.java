package io.pivotal.spring.cloud;

import org.springframework.cloud.cloudfoundry.CloudFoundryServiceInfoCreator;
import org.springframework.cloud.cloudfoundry.Tags;

import java.util.Map;

public class SsoServiceInfoCreator extends CloudFoundryServiceInfoCreator<SsoServiceInfo> {

    public SsoServiceInfoCreator() {
        super(new Tags("sso"));
    }

    @Override
    public boolean accept(Map<String, Object> serviceData) {
        return serviceData.get("label").equals(SsoServiceInfo.P_SSO_ID) || tagsMatch(serviceData);
    }

    @Override
    public SsoServiceInfo createServiceInfo(Map<String, Object> serviceData) {
        Map<String, Object> credentials = getCredentials(serviceData);
        String clientId = (String) credentials.get("client_id");
        String clientSecret = (String) credentials.get("client_secret");
        String authDomain = (String) credentials.get("auth_domain");
        if (serviceData.get("label").equals(SsoServiceInfo.P_SSO_ID)) {
            // to keep backward compatibility
            return new SsoServiceInfo(clientId, clientSecret, authDomain);
        } else {
            String id = (String) serviceData.get("name");
            return new SsoServiceInfo(id, clientId, clientSecret, authDomain);
        }
    }
}
