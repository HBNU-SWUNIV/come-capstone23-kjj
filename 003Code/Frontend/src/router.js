import { Navigate, createBrowserRouter } from 'react-router-dom';
import App from './router/App';
import RootContainer from './router/RootContainer';
import Login, { action as loginAction } from './domains/accounts/Login';
import InitialLogin from './domains/accounts/InitialLogin';
import TodayMenu from './domains/pages/TodayMenu';
import Dashboard from './domains/pages/Dashboard';
import Menus from './domains/pages/Menus';
import Dayoff from './domains/pages/Dayoff';

const router = createBrowserRouter([
  {
    path: '/',
    element: <RootContainer />,
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
