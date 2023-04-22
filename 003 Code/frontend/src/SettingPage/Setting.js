import styled from "styled-components";
import Navtop from "../Components/Navtop";
import Calander2 from "./Calander2";


const Wrapper = styled.div`
display:flex;
flex-direction:column;
width:85vw;
height:100vh;
margin-top:30px;
`;

const CalanderWrapper = styled.div`
    width:85vw;
    height:60vh;
    margin-left:56px;
    display:flex;
    flex-direction:column;
`;

const CalanderHeader = styled.div`
    width:63vw;
    height:15vh;
    margin-left:30px;
    margin-top:40px;
    margin-bottom:-55px;
    display:flex;
    justify-content:space-between;
    align-items:center;
    span{
        font-size:20px;
        font-weight:600;
    }
    button{
        width:10vw;
        height:4vh;
        border-radius:5px;
        background-color:#C8D5EF;
        border:1px solid #C8D5EF;
    }
`;

const FirstWrapper = styled.div`
    display:flex;
    width:80vw;
    height:40vh;
    margin-left:80px;
    margin-top:50px;
`;

const Message = styled.div`
    width:35vw;
    height:40vh;
    display:flex;
    flex-direction:column;
    span{
        font-size:18px;
        font-weight:600;
        margin-bottom:5px;
    }
    input{
        margin:0 4px;
        width:34vw;
        height:15vh;
    }
`;

const MessageDiv = styled.div`
    display:flex;
    align-items:center;
    justify-content:flex-end;
    width:35vw;
    height:4vh;
    button{
        margin-top:10px;
        margin-right:10px;
        width:5vw;
        height:4vh;
        background-color:#C8D5EF;
        border: 1px solid #C8D5EF;
        border-radius:5px;
    }
`;



function Setting(){

    return(
        <Wrapper>
        <Navtop pages={"설정"} isLogin={"한밭대학교"}/>
        <FirstWrapper>
            <Message>
                <span>식당 소개 메시지를 설정할 수 있어요.</span>
                <input placeholder="백반단가 5000원"/>
                <MessageDiv><button>수정</button></MessageDiv>
            </Message>     
        </FirstWrapper>

        <CalanderWrapper>
            <CalanderHeader>
                <span>휴일을 설정할 수 있어요.</span>
                <button>휴일 등록</button>
            </CalanderHeader>
            <Calander2/>
        </CalanderWrapper>
        </Wrapper>
    )
}
export default Setting;