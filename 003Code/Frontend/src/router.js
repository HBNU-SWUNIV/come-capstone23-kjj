import { Navigate, createBrowserRouter } from 'react-router-dom';
import InitialLogin from './domains/accounts/InitialLogin';
import Login, { action as loginAction } from './domains/accounts/Login';
import Dashboard from './domains/pages/Dashboard';
import Dayoff from './domains/pages/Dayoff';
import Menus from './domains/pages/Menus';
import TodayMenu from './domains/pages/TodayMenu';
import App from './router/App';
import ErrorBoundary from './router/ErrorBoundary';
import RootContainer from './router/RootContainer';

const router = createBrowserRouter([
  {
    path: '/',
    element: <RootContainer />,
    errorElement: <ErrorBoundary />,
    children: [
      {
        path: '/login',
        element: <Login />,
        action: loginAction,
      },
      {
        path: '/initialLogin',
        element: <InitialLogin />,
      },
      {
        path: '/',
        element: <App />,
        children: [
          {
            path: '/',
            element: <Navigate to="/login" />,
          },
          {
            path: '/home',
            element: <Dashboard />,
            index: true,
          },
          {
            path: '/menu',
            element: <Menus />,
          },
          {
            path: '/dailymenu',
            element: <TodayMenu />,
          },
          {
            path: '/dayoff',
            element: <Dayoff />,
          },
        ],
      },
    ],
  },
]);

export default router;
