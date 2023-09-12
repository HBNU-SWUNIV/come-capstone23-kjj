import React from 'react';
import { isloginAtom } from '../atom/loginAtom';
import { Navigate } from 'react-router-dom';
import { useRecoilValue } from 'recoil';
import { useKeycloak } from '@react-keycloak/web';
import Swal from "sweetalert2";

const Auth = (props) => {
  const loginauth = useRecoilValue(isloginAtom);
  const { keycloak } = useKeycloak();

  if (!loginauth && !keycloak.authenticated) {
    return <Navigate to="/login" replace />;
  }

  return props.children;
};

export default Auth;