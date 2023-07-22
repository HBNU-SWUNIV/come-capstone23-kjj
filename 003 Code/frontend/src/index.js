import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import reportWebVitals from './reportWebVitals';
import { RouterProvider } from 'react-router-dom';
import router from './router';
import { ReactKeycloakProvider } from '@react-keycloak/web';
import keycloak, { initOptions } from './Keycloak';
import { CookiesProvider } from 'react-cookie';

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
