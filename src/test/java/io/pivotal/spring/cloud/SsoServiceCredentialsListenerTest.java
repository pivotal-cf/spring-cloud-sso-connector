package io.pivotal.spring.cloud;

import io.pivotal.spring.cloud.mocks.MockCloudConnector;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SsoServiceCredentialsListenerTest.TestConfig.class)
public class SsoServiceCredentialsListenerTest {

    private static SsoServiceInfo info = Mockito.mock(SsoServiceInfo.class);

    @Autowired
    private Environment environment;

    @BeforeClass
    public static void beforeClass() {
        when(MockCloudConnector.instance.isInMatchingCloud()).thenReturn(true);
        when(MockCloudConnector.instance.getServiceInfos()).thenReturn(Collections.singletonList(
            info));
        when(info.getClientId()).thenReturn("test-client-id");
        when(info.getClientSecret()).thenReturn("test-client-secret");
        when(info.getAuthDomain()).thenReturn("test-auth-domain");
    }

    @AfterClass
    public static void afterClass() {
        MockCloudConnector.reset();
    }

    @Test
    public void addClientCredentials() {
        assertEquals("test-client-id", environment.getProperty("security.oauth2.client.clientId"));
        assertEquals("test-client-secret", environment.getProperty("security.oauth2.client.clientSecret"));
        assertEquals("test-auth-domain/oauth/token", environment.getProperty("security.oauth2.client.accessTokenUri"));
        assertEquals("test-auth-domain/oauth/authorize", environment.getProperty("security.oauth2.client.userAuthorizationUri"));
        assertEquals("test-auth-domain/token_key", environment.getProperty("security.oauth2.resource.jwt.keyUri"));
        assertEquals("test-auth-domain/userinfo", environment.getProperty("security.oauth2.resource.userInfoUri"));
        assertEquals("test-auth-domain/check_token", environment.getProperty("security.oauth2.resource.tokenInfoUri"));
        assertEquals("test-auth-domain", environment.getProperty("ssoServiceUrl"));
    }

    public static class TestConfig {
    }
}
