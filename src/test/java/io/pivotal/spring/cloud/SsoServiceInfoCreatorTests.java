package io.pivotal.spring.cloud;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SsoServiceInfoCreatorTests {

    private final String P_SSO_ID = "p-identity";
    SsoServiceInfoCreator creator = new SsoServiceInfoCreator();

    @Test
    public void acceptServiceData() {
        Map<String, Object> serviceData = new HashMap<>();
        serviceData.put("label", P_SSO_ID);
        assertTrue(creator.accept(serviceData));
    }

    @Test
    public void acceptServiceDataForTags() {
        Map<String, Object> serviceData = new HashMap<>();
        serviceData.put("tags", Arrays.asList("sso", "oauth2"));
        serviceData.put("label", "p-foo");
        assertTrue(creator.accept(serviceData));
    }

    @Test
    public void doesNotAcceptServiceData() {
        Map<String, Object> serviceData = new HashMap<>();
        serviceData.put("label", "wrong-label");
        assertFalse(creator.accept(serviceData));
    }

    @Test
    public void doesNotAcceptServiceDataForTags() {
        Map<String, Object> serviceData = new HashMap<>();
        serviceData.put("tags", Arrays.asList("oauth2"));
        serviceData.put("label", "wrong-label");
        assertFalse(creator.accept(serviceData));
    }

    @Test
    public void createServiceInfo() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("client_id", "test_client_id");
        credentials.put("client_secret", "test_client_secret");
        credentials.put("auth_domain", "test_auth_domain");

        Map<String, Object> serviceData = new HashMap<>();
        serviceData.put("credentials", credentials);
        serviceData.put("label", P_SSO_ID);

        SsoServiceInfo info = creator.createServiceInfo(serviceData);

        assertEquals("test_client_id", info.getClientId());
        assertEquals("test_client_secret", info.getClientSecret());
        assertEquals("test_auth_domain", info.getAuthDomain());
    }
}
