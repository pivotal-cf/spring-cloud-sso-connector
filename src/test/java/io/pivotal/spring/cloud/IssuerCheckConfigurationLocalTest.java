package io.pivotal.spring.cloud;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = IssuerCheckConfiguration.class)
public class IssuerCheckConfigurationLocalTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void testJwkTokenStoreNotFoundInContext() {
        thrown.expect(NoSuchBeanDefinitionException.class);
        thrown.expectMessage("No qualifying bean of type 'org.springframework.security.oauth2.provider.token.TokenStore' available");

        applicationContext.getBean( TokenStore.class );
    }
}
