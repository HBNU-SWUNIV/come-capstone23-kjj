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



const router = createBrowserRouter([
    {
        path:'',
        element:<App/>,
        children:[
            {
                path:'',
                element:<Login/>
            },
            {
                path:'/login',
                element:<Login/>
            },
            {
                path:'/signup',
                element:<SignUp/>
            },
            {
                path:'/home',
                element:<Home/>
            },
            {
                path:'/calendar',
                element:<Calendar/>
            },
            {
                path:'/my',
                element:<My/>
            },
            {
                path:'/setting',
                element:<Setting/>
            },
            {
                path:'/guide1',
                element:<Guide1/>
            },
            {
                path:'/guide2',
                element:<Guide2/>
            },
            {
                path:'/myuse',
                element:<MyUse/>
            },
            {
                path:'/graph',
                element:<Graph/>
            },
        ]
    }
])

export default router;