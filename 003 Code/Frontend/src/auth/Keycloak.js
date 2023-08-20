import Keycloak from 'keycloak-js';

const KEYCLOAK_REALM_NAME = 'test-app';
const KEYCLOAK_CLIENT_ID = 'test-client';
const KEYCLOAK_URL = 'http://kjj.kjj.r-e.kr:8081/auth';

const keycloak = new Keycloak({
  realm: KEYCLOAK_REALM_NAME,
  clientId: KEYCLOAK_CLIENT_ID,
  url: KEYCLOAK_URL,
});

export default keycloak;
