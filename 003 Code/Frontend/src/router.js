import { createBrowserRouter } from "react-router-dom";
import App from "./App";
import Dashboard from "./dashboard/Dashboard";
import LoginFirst from "./login/LoginFirst";
import Album from "./dashboard/Album";
import DailyMenu from "./dailyMenu/DailyMenu";
import Dayoff from "./dayoff/Dayoff";
import Login1 from "./login/Login1";

const router = createBrowserRouter([
    {
        path:'',
        element:<App/>,
        children:[
            {
                path:'/',
                element:<Login1/>,
            },
            {
                path:'/loginfirst',
                element:<LoginFirst/>
            },
            {
                path:'/home',
                element:<Dashboard/>
            },
            {
                path:'/menu',
                element:<Album/>
            },
            {
                path:'/dailymenu',
                element:<DailyMenu/>
            },
            {
                path:'/dayoff',
                element:<Dayoff/>
            }
        ]
    }
])

export default router;