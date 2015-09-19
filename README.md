#Spring Cloud Single Sign-On Connector

Spring Cloud Connector for using Single Sign-On service on Cloud Foundry

### Spring Applications

Spring Application can use this connector to auto-configure its OAuth 2.0 client
which enables the application to authenticate via Single Sign-On with the UAA in
Cloud Foundry.

The SSO Service provides the following properties to your spring application:

Property Name  |  Value
-------------- | ------
ssoServiceUrl  |  e.g. https://uaa.run.pivotal.io
spring.oauth2.client.clientId  | client_id
spring.oauth2.client.clientSecret | client_secret
spring.oauth2.client.userAuthorizationUri  |  {ssoServiceUrl}/oauth/authorize
spring.oauth2.client.accessTokenUri  |  {ssoServiceUrl}/oauth/token
spring.oauth2.resource.userInfoUri  |  {ssoServiceUrl}/userinfo
spring.oauth2.resource.tokenInfoUri  |  {ssoServiceUrl}/check_token
spring.oauth2.resource.jwt.keyUri  |  {ssoServiceUrl}/token_key

Obs.: The `ssoServiceUrl` is the UAA base location (e.g. `https://uaa.run.pivotal.io`)

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

Sample apps are at https://github.com/pivotal-cf/identity-sample-apps