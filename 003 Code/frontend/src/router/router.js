import { createBrowserRouter } from "react-router-dom";
import App from "../App";
import Control_login from '../LoginPage/Control_login';
import Dashboard from "../HomePage/Dashboard";
import Menu from '../MenuPage/Menu';
import Backban from "../BackbanPage/Backban";
import Setting from "../SettingPage/Setting";
import NotSetting from "../LoginPage/NotSetting";



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
                path:'/notSetting',
                element:<NotSetting/>
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
                path:'/menu/:deleteId',
                element:<Menu/>
            },
            {
                path:'/menu/update/:updateId',
                element:<Menu/>
            },
            {
              path:'/menu/sick/:sickId',
              element:<Menu/>  
            },
            {
                path:'/backban',
                element:<Backban/>
            },
            {
                path:'/backban/:id',
                element:<Backban/>
            },
            {
                path:'/setting',
                element:<Setting/>
            },
            {
                path:'/setting/:id',
                element:<Setting/>
            }
        ]
    }
])

export default router;