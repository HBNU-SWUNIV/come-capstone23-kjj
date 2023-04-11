import { useSelector } from "react-redux";
import Login from "./Login";
import FirstLogin from "./FirstLogin";



function Control_login(){
    let User = useSelector(state => state.User);
    console.log(User);
    return(
        <>
        { User.isLogin? <FirstLogin/> : <Login/> 
        }
        </>
    )
}

export default Control_login;