package io.pivotal.spring.cloud;

import io.specto.hoverfly.junit.rule.HoverflyRule;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@EnableAutoConfiguration
@TestPropertySource(properties = {
    "ssoServiceUrl=https://cf-identity-eng-test1.login.run.pivotal.io",
    "security.oauth2.resource.jwk.key-set-uri=https://cf-identity-eng-test1.login.run.pivotal.io/token_keys",
    "sso.connector.cloud.available=success"
})
public class IssuerCheckConfigurationTest {
    @ClassRule
    public static HoverflyRule hoverflyRule = HoverflyRule.inCaptureOrSimulationMode("IssuerCheckConfigurationTest.json");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    TokenStore jwkTokenStore;

    @Test
    public void permitsAValidAccessTokenWithTheCorrectIssuer() {
        String accessTokenForZone1 = "eyJhbGciOiJSUzI1NiIsImtpZCI6InNoYTItMjAxNy0wMS0yMC1rZXkiLCJ0eXAiOiJKV1QifQ.eyJqdGkiOiI5MThjZjc0Yzk5NTM0MDhhYjczZTI3YjI4ZGM3NzFmNCIsInN1YiI6ImNmLWlkZW50aXR5LWVuZy10ZXN0MS1jbGllbnQiLCJhdXRob3JpdGllcyI6WyJ1YWEubm9uZSJdLCJzY29wZSI6WyJ1YWEubm9uZSJdLCJjbGllbnRfaWQiOiJjZi1pZGVudGl0eS1lbmctdGVzdDEtY2xpZW50IiwiY2lkIjoiY2YtaWRlbnRpdHktZW5nLXRlc3QxLWNsaWVudCIsImF6cCI6ImNmLWlkZW50aXR5LWVuZy10ZXN0MS1jbGllbnQiLCJncmFudF90eXBlIjoiY2xpZW50X2NyZWRlbnRpYWxzIiwicmV2X3NpZyI6IjVhNzk3NmFlIiwiaWF0IjoxNTAxMTkyODMzLCJleHAiOjE1MDEyMzYwMzMsImlzcyI6Imh0dHBzOi8vY2YtaWRlbnRpdHktZW5nLXRlc3QxLnVhYS5ydW4ucGl2b3RhbC5pby9vYXV0aC90b2tlbiIsInppZCI6ImZiYjYxNjk3LWMwZDctNDg4Zi1hZTFlLTEwMmRiMjk5YjgzMCIsImF1ZCI6WyJjZi1pZGVudGl0eS1lbmctdGVzdDEtY2xpZW50Il19.N6kEA4xXGm9--HAaCA40kaEymH9ON3JSeARU1ZvdvpX_P7E_grq-RFZquAmGvk0_UWBTBlz61pM7HClz7UxKnTJVCLkcCzaokDFjDVhFwRxAiAQ2tdUtEPxxOW7xUoyzIBk6RhdAckOX4kgAj1GXKc4SBHdJed-FXao3VIW5Z1YbpwUTHKnX9q8W5hE_dSIzd6X61r3oIi1WrqHCjkABQys9Osu8VBIjLtYIVut_PR14chZu0swPrVU2L0ncdS28lpYvrJWX_2LbJsoqix2mLcsQ26NBz3skusdJF0buq4Wnb3oz7w-NtWCSLOrhfKDUaIoK3-7f8AXdqWaND7wF0A";

        OAuth2AccessToken oAuth2AccessToken = jwkTokenStore.readAccessToken(accessTokenForZone1);
        Assert.assertEquals(oAuth2AccessToken.getAdditionalInformation().get("iss"), "https://cf-identity-eng-test1.uaa.run.pivotal.io/oauth/token");
    }

    @Test
    public void rejectsAValidAccessTokenWithADifferentIssuer() {
        thrown.expect(InvalidTokenException.class);
        thrown.expectMessage("Invalid Issuer (iss) claim: https://cf-identity-eng-test2.uaa.run.pivotal.io/oauth/token");

        String accessTokenForZone2 = "eyJhbGciOiJSUzI1NiIsImtpZCI6InNoYTItMjAxNy0wMS0yMC1rZXkiLCJ0eXAiOiJKV1QifQ.eyJqdGkiOiI5ZDQzZGI3ZmE2MGU0MmRmOTA4OWYxYTBkZmZlMGYxMSIsInN1YiI6ImNmLWlkZW50aXR5LWVuZy10ZXN0Mi1jbGllbnQiLCJhdXRob3JpdGllcyI6WyJ1YWEubm9uZSJdLCJzY29wZSI6WyJ1YWEubm9uZSJdLCJjbGllbnRfaWQiOiJjZi1pZGVudGl0eS1lbmctdGVzdDItY2xpZW50IiwiY2lkIjoiY2YtaWRlbnRpdHktZW5nLXRlc3QyLWNsaWVudCIsImF6cCI6ImNmLWlkZW50aXR5LWVuZy10ZXN0Mi1jbGllbnQiLCJncmFudF90eXBlIjoiY2xpZW50X2NyZWRlbnRpYWxzIiwicmV2X3NpZyI6IjFmNTI0ZDAzIiwiaWF0IjoxNTAxMTkyODk1LCJleHAiOjE1MDEyMzYwOTUsImlzcyI6Imh0dHBzOi8vY2YtaWRlbnRpdHktZW5nLXRlc3QyLnVhYS5ydW4ucGl2b3RhbC5pby9vYXV0aC90b2tlbiIsInppZCI6Ijg4NDIxNmJiLWQyNjYtNDA3Mi1iZGIyLWNjZTEyZDc1NDkxNCIsImF1ZCI6WyJjZi1pZGVudGl0eS1lbmctdGVzdDItY2xpZW50Il19.Phmg3vtQOdzhtq3A_G6DOsckwp7u-ANLrXq_DktSyETV-0nmPLrjbAEjxzPROJKuwNrFFMjLCr5ZsbEMa-gV2zXvEKl3B5Wf4S2FZaXnEZmmzkYlQ5PRAYKYzQKJJC6dSicLRAXJdMloktqN94y27BJ8loi9qOGtpSTMZ95Z5M3fjU3fjExN0I8TQwFFZ3a2f_3zmaotb2HEtJtNmdV-wdROMcvO_kmbNVTNCUi-TW9y4gp3VQ8B84c65yXw3tvsmmiF9s8nvcPqyXuSmjkx5XaFRXlctk1D3Aztj2Yj3_tJIPZDIEUicBXCo1UoSSdOQ7iRsiGChmK6vWjdtf7vkg";
        jwkTokenStore.readAccessToken(accessTokenForZone2);
    }
}
