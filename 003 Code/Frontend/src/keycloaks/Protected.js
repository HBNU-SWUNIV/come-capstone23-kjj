import React from 'react';
import { useKeycloak } from '@react-keycloak/web';

const Protected = () => {
  const { keycloak, initialized } = useKeycloak();
  console.log(keycloak);
  return <div>Protected</div>;
};

export default Protected;
