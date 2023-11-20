import { ReactKeycloakProvider } from '@react-keycloak/web';
import React from 'react';
import { CookiesProvider } from 'react-cookie';
import ReactDOM from 'react-dom/client';
import { QueryClient, QueryClientProvider } from 'react-query';
import { RouterProvider } from 'react-router-dom';
import { RecoilRoot } from 'recoil';
import { ThemeProvider } from 'styled-components';
import keycloak, { initOptions } from './auth/Keycloak';
import reportWebVitals from './reportWebVitals';
import router from './router';
import theme from './styles/theme';

const root = ReactDOM.createRoot(document.getElementById('root'));

const queryClient = new QueryClient();

root.render(
  <ThemeProvider theme={theme}>
    <QueryClientProvider client={queryClient}>
      <ReactKeycloakProvider authClient={keycloak} initOptions={initOptions}>
        <CookiesProvider>
          <RecoilRoot>
            <RouterProvider router={router} />
          </RecoilRoot>
        </CookiesProvider>
      </ReactKeycloakProvider>
    </QueryClientProvider>
  </ThemeProvider>
);

reportWebVitals();
