import { useSelector } from "react-redux";
import Login from "./Login";
import FirstLogin from "./FirstLogin";

function Control_login(){
    let User = useSelector(state => state.User);
    return(
        <>
        {/* { User.isLogin? <FirstLogin/> : <Login/> 
        } */}
        <Login/>
        </>
    )
}

export default Control_login;