import Keycloak from 'keycloak-js';

import React, { useEffect, useRef, useState } from 'react';
const KEYCLOAK_REALM_NAME = 'test-app';
const KEYCLOAK_CLIENT_ID = 'test-client';
const KEYCLOAK_URL = 'http://kjj.kjj.r-e.kr:8081/auth/';

const client = new Keycloak({
  realm: KEYCLOAK_REALM_NAME,
  clientId: KEYCLOAK_CLIENT_ID,
  url: KEYCLOAK_URL,
});
const useAuth = () => {
  const [isLogin, setLogin] = useState(false);
  const isRun = useRef(false);
  useEffect(() => {
    if (isRun.current) return;

    isRun.current = true;
    client.init({ onLoad: 'login-required' }).then((res) => setLogin(res));
  }, []);

  return isLogin;
};

export default useAuth;
