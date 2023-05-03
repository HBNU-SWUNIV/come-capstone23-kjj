import styled from "styled-components";
import Navtop from "../Components/Navtop";
import Calander2 from "./Calander2";
import axios from "axios";
import { useEffect } from "react";
import { useState } from "react";
import Overlay from '../Components/Overlay';
import { useLocation } from "react-router-dom";


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
    position:relative;
    span{
        font-size:20px;
        font-weight:600;
    }
`;

const Calandertip = styled.ul`
width:16vw;
height:3.5vh;
position:absolute;
right:0;
li{
    font-weight:500;
    font-size:15px;
    color:#C63333;
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
    textarea{
        margin:0 4px;
        width:34vw;
        height:15vh;
        white-space:pre-wrap;
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
    const [info, setInfo] = useState('');
    const location = useLocation();

    useEffect(() => {
        axios.get('/api/user/store')
        .then(res => setInfo(res.data.info))
    },[])

    const onInfo = (e) => {
        setInfo(e);
    }

    const onInfoUpdate = () => {
        let body = {info};
        axios.patch('/api/manager/store/set/info',body)
        .then(res => res.status === 200 && alert('수정되었습니다.'))
    }

    return(
        <>
        <Wrapper>
        <Navtop pages={"설정"} isLogin={"한밭대학교"}/>

        <FirstWrapper>
            <Message>
                <span>식당 소개 메시지를 설정할 수 있어요.</span>
                <textarea value={info} placeholder={info} onChange={e => onInfo(e.target.value)}/>
                <MessageDiv>
                    <button onClick={onInfoUpdate}>
                        수정
                    </button>
                </MessageDiv>
            </Message>     
        </FirstWrapper>

        <CalanderWrapper>
            <CalanderHeader>
                <span>휴일을 설정할 수 있어요.</span>
                <Calandertip>
                <li>요일을 클릭해서 휴일을 설정해보세요!</li>
                </Calandertip>
            </CalanderHeader>
            <Calander2/>
        </CalanderWrapper>
        </Wrapper>
        {location.pathname == '/setting' ? null : <Overlay/>}
        </>
    )
}
export default Setting;