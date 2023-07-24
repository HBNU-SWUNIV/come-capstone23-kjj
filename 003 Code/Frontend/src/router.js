import { createBrowserRouter } from 'react-router-dom';
import App from './App';
import Dashboard from './screens/Dashboard';
import DailyMenu from './screens/DailyMenu';
import Dayoff from './screens/Dayoff';
import Menus from './screens/Menus';
import Login from './login/Login';
import LoginFirst from './login/LoginFirst';

const router = createBrowserRouter([
  {
    path: '',
    element: <App />,
    children: [
      {
        path: '/',
        element: <Login />,
      },
      {
        path: '/loginfirst',
        element: <LoginFirst />,
      },
      {
        path: '/home',
        element: <Dashboard />,
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
]);

export default router;
