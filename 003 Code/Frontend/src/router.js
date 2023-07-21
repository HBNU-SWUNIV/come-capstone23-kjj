import { createBrowserRouter } from 'react-router-dom';
import App from './App';
import Dashboard from './dashboard/Dashboard';
import LoginFirst from './login/LoginFirst';
import Album from './dashboard/Album';
import DailyMenu from './dailyMenu/DailyMenu';
import Dayoff from './dayoff/Dayoff';
import Login from './login/Login';

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
        element: <Album />,
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
