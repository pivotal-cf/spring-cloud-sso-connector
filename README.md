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
security.oauth2.resource.jwt.keyUri  |  {ssoServiceUrl}/token_key

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
