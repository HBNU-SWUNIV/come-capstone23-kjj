import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter } from "react-router-dom";
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import router from '../src/router/router';
import { RouterProvider } from 'react-router-dom';
import { store, persistor } from './store';
import { PersistGate } from 'redux-persist/integration/react';
import { Provider } from 'react-redux';
import { ReactKeycloakProvider } from '@react-keycloak/web';
import { CookiesProvider } from 'react-cookie';
import keycloak from './auth/Keycloak';

const root = ReactDOM.createRoot(document.getElementById('root'));
// root.render(
//   <React.StrictMode>
//     <Provider store={store}>
//       <PersistGate loading={null} persistor={persistor}>
//         <RouterProvider router={router} />
//       </PersistGate>
//     </Provider>
//   </React.StrictMode>,
// );

root.render(
  <Provider store={store}>
    <ReactKeycloakProvider authClient={keycloak}>
      <CookiesProvider>
        <PersistGate loading={null} persistor={persistor}>
          <RouterProvider router={router} />
        </PersistGate>
      </CookiesProvider>
    </ReactKeycloakProvider>
  </Provider>,
);


reportWebVitals();
