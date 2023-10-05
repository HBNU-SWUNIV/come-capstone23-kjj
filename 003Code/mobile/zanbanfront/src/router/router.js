import { createBrowserRouter } from "react-router-dom";
import App from "../App";
import Login from "../LoginPage/Login";
import SignUp from "../LoginPage/SignUp";
import Home from "../HomePage/Home";
import Calendar from '../CalendarPage/Calendar';
import My from '../MyPage/My';
import MyUse from '../MyPage/MyUse'
import Graph from "../MyPage/Graph";
import Setting from "../SettingPage/Setting";
import Guide1 from "../SettingPage/Guide1";
import Guide2 from "../SettingPage/Guide2";
import Guide3 from "../SettingPage/Guide3";
import Guide4 from "../SettingPage/Guide4";
import BestMenu from "../HomePage/BestMenu";
import RootContainer from './RootContainer';
import Checkout from '../toss/Checkout';
import Success from "../toss/Success";
import Fail from "../toss/Fail";
import FirstLogin from "../SettingPage/FirtsLogin";



const router = createBrowserRouter([
    {
        path: '',
        element: <RootContainer />,
        children: [
            {
                path: '',
                element: <Login />
            },
            {
                path: '/login',
                element: <Login />
            },
            {
                path: '/signup',
                element: <SignUp />
            },
            {
                path: '',
                element: <App />,
                children: [
                    {
                        path: '/home',
                        element: <Home />
                    },
                    {
                        path: '/home/:id',
                        element: <Home />
                    },
                    {
                        path: '/home/bestmenu',
                        element: <BestMenu />
                    },
                    {
                        path: '/calendar',
                        element: <Calendar />
                    },
                    {
                        path: '/calendar/:id',
                        element: <Calendar />
                    },
                    {
                        path: '/my',
                        element: <My />
                    },
                    {
                        path: '/setting',
                        element: <Setting />
                    },
                    {
                        path: '/firstlogin',
                        element: <FirstLogin />
                    },
                    {
                        path: '/guide1',
                        element: <Guide1 />
                    },
                    {
                        path: '/guide2',
                        element: <Guide2 />
                    },
                    {
                        path: '/guide3',
                        element: <Guide3 />
                    },
                    {
                        path: '/guide4',
                        element: <Guide4 />
                    },
                    {
                        path: '/myuse',
                        element: <MyUse />
                    },
                    {
                        path: '/graph',
                        element: <Graph />
                    },
                    {
                        path: '/checkout',
                        element: <Checkout />
                    },
                    {
                        path: '/success',
                        element: <Success />
                    },
                    {
                        path: '/fail',
                        element: <Fail />
                    },
                ]
            }
        ]
    }
])

export default router;