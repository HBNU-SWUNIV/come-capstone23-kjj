import { createBrowserRouter } from "react-router-dom";
import App from "../App";
import Control_login from '../LoginPage/Control_login';

const router = createBrowserRouter([
    {
        path:'',
        element:<App/>,
        children:[
            {
                path:'',
                element:<Control_login/>
            },
            
        ]
    }
])

export default router;