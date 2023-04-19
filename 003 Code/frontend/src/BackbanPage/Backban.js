import styled from "styled-components";
import Calander from "./Calander";
import Navtop from "../Components/Navtop";

const Wrapper = styled.div`
display:flex;
flex-direction:column;
width:85vw;
height:100vh;
margin-top:30px;
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
    return(
        <Wrapper>
            <Navtop pages={'백반 관리'}/>
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