import styled from "styled-components";
import Calander from "./Calander";
import Navtop from "../Components/Navtop";
import Overlay from "../Components/Overlay";
import { useLocation } from "react-router-dom";

const Wrapper = styled.div`
display:flex;
flex-direction:column;
width:85vw;
height:100vh;
font-family:'DeliveryFont';
margin-top:30px;
`;

const Tip = styled.ul`
    display:flex;
    flex-direction:column;
    align-items:space-evenly;
    justify-content:center;
    width:30vw;
    height:10vh;
    position:absolute;
    right:0;
    margin-top:1vh;
    margin-right:20vw;
    li{
        font-weight:500;
        font-size:15px;
        color:#C63333;
    }
`;

function Backban(){
    const location = useLocation();
    
    return(
        <>
        <Wrapper>
            <Navtop pages={'오늘의 메뉴'}/>
            <span style={{position:'relative',fontWeight:'400',fontSize:'22px',marginLeft:'30px',color:'#7BE457'}}>요일을 클릭해서 식단표를 작성해주세요!</span>
            <Tip>
                <li>식단을 등록하면 소비자가 확인할 수 있어요!</li>
                <li>식단을 지우고 싶으시면 내용을 비운뒤 저장버튼을 누르면 됩니다!</li>
            </Tip>
            <Calander/>
        </Wrapper>
        {location.pathname =='/backban' ? null : <Overlay/>}
        </>
    )
}

export default Backban;