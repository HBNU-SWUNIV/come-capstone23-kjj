import { createBrowserRouter } from "react-router-dom";
import App from "../App";
import Control_login from '../LoginPage/Control_login';
import Dashboard from "../HomePage/Dashboard";
import Menu from '../MenuPage/Menu';
import Backban from "../BackbanPage/Backban";
import Setting from "../SettingPage/Setting";



const router = createBrowserRouter([
    {
        path:'',
        element:<App/>,
        children:[
            {
                path:'',
                element:<Control_login/>
            },
            {
                path:'/home',
                element:<Dashboard/>
            },
            {
                path:'/menu',
                element:<Menu/>
            },
            {
                path:'/backban',
                element:<Backban/>
            },
            {
                path:'/setting',
                element:<Setting/>
            }
        ]
    }
])

export default router;