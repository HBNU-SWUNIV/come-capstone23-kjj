import { useDispatch } from "react-redux";
import styled from "styled-components";
import { IoMdLogOut } from "react-icons/io";
import { R_logout } from '../store';

const Wrapper = styled.div`
display:flex;
flex-direction:column;
width:85vw;
height:100vh;
`;

const Nav = styled.div`
display:flex;
width:85vw;
height:15vh;
justify-content:space-between;
align-items:center;
span{
    margin-left:30px;
    font-size:30px;
    font-weight:600;
    font-family:'Alegreya';
    color:#0A376E;
}
div{
    margin-right:30px;
    font-size:30px;
    font-weight:600;
    font-family:'Alegreya';
}
`;



function Setting(){
    const dispatch = useDispatch();
    return(
        <Wrapper>
        <Nav>
            <span>설정</span>
            <div><IoMdLogOut onClick={() => dispatch(R_logout())}/></div>
        </Nav>
        </Wrapper>
    )
}
export default Setting;