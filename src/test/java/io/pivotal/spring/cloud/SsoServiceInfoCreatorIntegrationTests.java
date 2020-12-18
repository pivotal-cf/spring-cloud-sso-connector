package io.pivotal.spring.cloud;

import org.junit.Test;
import org.springframework.cloud.cloudfoundry.AbstractCloudFoundryConnectorTest;
import org.springframework.cloud.service.ServiceInfo;

import java.util.List;

import static org.mockito.Mockito.when;


public class SsoServiceInfoCreatorIntegrationTests extends AbstractCloudFoundryConnectorTest {
    private static final String VCAP_SERVICES_ENV_KEY = "VCAP_SERVICES";

    @Test
    public void ssoServiceCreation() {
        when(mockEnvironment.getEnvValue(VCAP_SERVICES_ENV_KEY))
            .thenReturn(getServicesPayload(getSsoServicePayload()));

        List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();
        assertServiceFoundOfType(serviceInfos, "p-identity", SsoServiceInfo.class);
    }

    @Test
    public void ssoServiceCreationWithSsoTag() {
        when(mockEnvironment.getEnvValue(VCAP_SERVICES_ENV_KEY))
                .thenReturn(getServicesPayload(getSsoServicePayloadWithSsoTag()));

        List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();
        assertServiceFoundOfType(serviceInfos, "test-sso", SsoServiceInfo.class);
    }

    private String getSsoServicePayload() {
        String payload = "{\n" +
            "    \"name\": \"test-sso\",\n" +
            "    \"label\": \"p-identity\",\n" +
            "    \"plan\": \"standard\",\n" +
            "    \"tags\": [\n" +
            "        \"configuration\"\n" +
            "    ],\n" +
            "    \"credentials\": {\n" +
            "        \"auth_domain\": \"http://test.com\",\n" +
            "        \"client_id\": \"config_client_id\",\n" +
            "        \"client_secret\": \"its_a_secret_dont_tell\"\n" +
            "    }\n" +
            "}";

        return payload;
    }

    private String getSsoServicePayloadWithSsoTag() {
        String payload = "{\n" +
                "    \"name\": \"test-sso\",\n" +
                "    \"label\": \"p-foo\",\n" +
                "    \"plan\": \"standard\",\n" +
                "    \"tags\": [\n" +
                "        \"sso\", \"oauth2\"\n" +
                "    ],\n" +
                "    \"credentials\": {\n" +
                "        \"auth_domain\": \"http://test.com\",\n" +
                "        \"client_id\": \"config_client_id\",\n" +
                "        \"client_secret\": \"its_a_secret_dont_tell\"\n" +
                "    }\n" +
                "}";

        return payload;
    }

}
