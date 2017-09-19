package io.pivotal.spring.cloud;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.discovery.ProviderConfiguration;
import org.springframework.security.oauth2.client.discovery.ProviderDiscoveryClient;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.IssuerClaimVerifier;
import org.springframework.security.oauth2.provider.token.store.jwk.JwkTokenStore;

import java.net.MalformedURLException;

@Configuration
@ConditionalOnProperty({"ssoServiceUrl", "security.oauth2.resource.jwk.key-set-uri"})
public class IssuerCheckConfiguration {
    @Value("${ssoServiceUrl}")
    private String ssoServiceUrl;

    @Value("${security.oauth2.resource.jwk.key-set-uri}")
    private String keySetUri;

    @Bean
    public TokenStore jwkTokenStore() throws MalformedURLException {
        ProviderDiscoveryClient discoveryClient = new ProviderDiscoveryClient(ssoServiceUrl);
        ProviderConfiguration providerConfiguration = discoveryClient.discover();

        IssuerClaimVerifier issuerClaimVerifier = new IssuerClaimVerifier(providerConfiguration.getIssuer());

        return new JwkTokenStore(
            keySetUri,
            issuerClaimVerifier
        );
    }
}
