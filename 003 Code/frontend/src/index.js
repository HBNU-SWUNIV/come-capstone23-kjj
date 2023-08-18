import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import './Font/Font.css';
import reportWebVitals from './reportWebVitals';
import { RouterProvider } from 'react-router-dom';
import router from './router';
import { ReactKeycloakProvider } from '@react-keycloak/web';
import { CookiesProvider } from 'react-cookie';
import keycloak, { initOptions } from './auth/Keycloak';
import { RecoilRoot } from 'recoil';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <ReactKeycloakProvider authClient={keycloak}>
    <CookiesProvider>
      <RecoilRoot>
        <RouterProvider router={router} />
      </RecoilRoot>
    </CookiesProvider>
  </ReactKeycloakProvider>
);

reportWebVitals();
