import Keycloak from 'keycloak-js';

const KEYCLOAK_REALM_NAME = 'test-app';
const KEYCLOAK_CLIENT_ID = 'test-client';
const KEYCLOAK_URL = 'http://kjj.kjj.r-e.kr:8081/auth';

const keycloak = new Keycloak({
  realm: KEYCLOAK_REALM_NAME,
  clientId: KEYCLOAK_CLIENT_ID,
  url: KEYCLOAK_URL,
});

//키를락 로그인 안됐을 경우 키클락 로그인 페이지로 이동
// export const initOptions = {
//   onLoad: 'login-required',
//   checkLoginIframe: false,
// };

// export const initOptions = {
//   onLoad: 'check-sso',
//   checkLoginIframe: false,
// };


export default keycloak;
