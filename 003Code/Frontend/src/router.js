import { Navigate, createBrowserRouter } from 'react-router-dom';
import App from './App';
import Dashboard from './screens/Dashboard';
import DailyMenu from './screens/DailyMenu';
import Dayoff from './screens/Dayoff';
import Menus from './screens/Menus';
import Login, { action as loginAction } from './login/Login';
import LoginFirst from './login/LoginFirst';
import RootContainer from './router/RootContainer';

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
        path: '/',
        element: <App />,
        children: [
          {
            path: '/',
            element: <Navigate to="/home" />,
          },
          {
            path: '/home',
            element: <Dashboard />,
            index: true,
          },
          {
            path: '/loginfirst',
            element: <LoginFirst />,
          },
          {
            path: '/menu',
            element: <Menus />,
          },
          {
            path: '/dailymenu',
            element: <DailyMenu />,
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
