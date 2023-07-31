import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import reportWebVitals from './reportWebVitals';
import { RouterProvider } from 'react-router-dom';
import router from './router';
import { ReactKeycloakProvider } from '@react-keycloak/web';
import { CookiesProvider } from 'react-cookie';
import keycloak, { initOptions } from './auth/Keycloak';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <ReactKeycloakProvider initOptions={initOptions} authClient={keycloak}>
    <CookiesProvider>
      <RouterProvider router={router} />
    </CookiesProvider>
  </ReactKeycloakProvider>
);

reportWebVitals();
// onEvent={onKeycloakEvent}
