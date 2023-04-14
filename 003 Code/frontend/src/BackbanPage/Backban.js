import { useDispatch } from "react-redux";
import styled from "styled-components";
import { IoMdLogOut } from "react-icons/io";
import { R_logout } from '../store';
import Calander from "./Calander";

const Wrapper = styled.div`
display:flex;
flex-direction:column;
width:85vw;
height:100vh;
margin-top:30px;
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

const Tip = styled.ul`
    display:flex;
    flex-direction:column;
    align-items:space-evenly;
    justify-content:center;
    width:25vw;
    height:10vh;
    position:absolute;
    right:0;
    margin-top:3vh;
    margin-right:17vw;
    li{
        font-weight:500;
        font-size:18px;
        color:#C63333;
    }
`;


function Backban(){
    const dispatch = useDispatch();
    return(
        <Wrapper>
            <Nav>
                <span>백반 관리</span>
                <div><IoMdLogOut onClick={() => dispatch(R_logout())}/></div>
            </Nav>
            <span style={{position:'relative',fontWeight:'600',fontSize:'20px',marginLeft:'40px',color:'#7BE457'}}>요일을 클릭해서 식단표를 작성해주세요!</span>
            <Tip>
                <li>휴일은 달력에 빨간색으로 표시됩니다.</li>
                <li>휴일지정은 설정에서 할 수 있습니다.</li>
                <li>식단을 등록하면 소비자가 확인할 수 있어요!</li>
            </Tip>
            <Calander/>
        </Wrapper>
    )
}

export default Backban;