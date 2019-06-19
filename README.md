### **WARNING: This library is only compatible with Spring Boot 1.5 and Spring Security 2. If you are looking for a library that supports Spring Boot 2.1+ and Spring Security 5+, please refer to [Java CFEnv](https://github.com/pivotal-cf/java-cfenv) and the updated [identity-sample-apps](https://github.com/pivotal-cf/identity-sample-apps)**

#Spring Cloud Single Sign-On Connector

Spring Cloud Connector for use with the Pivotal Single Sign-On Service on Cloud Foundry

### Spring Applications

Spring Applications can use this connector to auto-configure its OAuth 2.0 client
which enables the application with the Pivotal Single Sign-On Service

This service provides the following properties to your spring application:

Property Name  |  Value
-------------- | ------
ssoServiceUrl  |  e.g. https://login.run.pivotal.io
security.oauth2.client.clientId  | client_id
security.oauth2.client.clientSecret | client_secret
security.oauth2.client.userAuthorizationUri  |  {ssoServiceUrl}/oauth/authorize
security.oauth2.client.accessTokenUri  |  {ssoServiceUrl}/oauth/token
security.oauth2.resource.userInfoUri  |  {ssoServiceUrl}/userinfo
security.oauth2.resource.tokenInfoUri  |  {ssoServiceUrl}/check_token
security.oauth2.resource.jwk.key-set-uri  |  {ssoServiceUrl}/token_keys

Note: ssoServiceUrl refers to the service uri corresponding to a Pivotal Single Sign-On service plan. For more information on configuring a service plan please refer to http://docs.pivotal.io/p-identity/index.html#create-plan

### Java Applications

Applications can use this connector to access the information in `VCAP_SERVICES`
environment variable, necessary to configure single sign-on.

```
CloudFactory cloudFactory = new CloudFactory();
Cloud cloud = cloudFactory.getCloud();
SsoServiceInfo ssoService = (SsoServiceInfo) cloud.getServiceInfo("p-identity");
ssoService.getClientId()
ssoService.getClientSecret();
ssoService.getAuthDomain();
```

### Sample Apps

Sample apps using this connector are available at https://github.com/pivotal-cf/identity-sample-apps
