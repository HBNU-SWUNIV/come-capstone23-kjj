import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import reportWebVitals from './reportWebVitals';
import router from '../src/router/router';
import { RouterProvider } from 'react-router-dom';
import { store, persistor } from './store';
import { PersistGate } from 'redux-persist/integration/react';
import { Provider } from 'react-redux';
import { ReactKeycloakProvider } from '@react-keycloak/web';
import { CookiesProvider } from 'react-cookie';
import keycloak from './auth/Keycloak';
import { RecoilRoot } from 'recoil';

const root = ReactDOM.createRoot(document.getElementById('root'));

root.render(
  <Provider store={store}>
    <ReactKeycloakProvider authClient={keycloak}>
      <CookiesProvider>
        <RecoilRoot>
          <PersistGate loading={null} persistor={persistor}>
            <RouterProvider router={router} />
          </PersistGate>
        </RecoilRoot>
      </CookiesProvider>
    </ReactKeycloakProvider>
  </Provider>,
);


reportWebVitals();
