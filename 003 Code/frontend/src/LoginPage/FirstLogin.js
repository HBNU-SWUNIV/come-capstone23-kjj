import styled from "styled-components";
import background from '../image/capstone_background.png';
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import { useState } from "react";
import axios from "axios";


const Wrapper = styled.div`
    background-image:url(${background});
    background-repeat:no-repeat;
    background-position:top center;
    background-size:cover;
    background-attachment:fixed;
    width:100vw;
    height:100vh;
    z-index:5;
    display:flex;
    justify-content:center;
    align-items:center;
`

const LoginW = styled.div`
    width:500px;
    height:500px;
    background-color:white;
    z-index:1;
    display:flex;
    border-radius:30px;
    flex-direction:column;
    justify-content:space-between;
    align-items:center;
`

const Title = styled.div`
display:flex;
justify-content:center;
align-items:center;
margin-top:10px;
flex-direction:column;
    span:first-child{
        font-size:50px;
        color:#0A376E;
        font-weight:600;
        font-family:'Alegreya';
    }
    span:last-child{
        font-size:35px;
        margin-top:-20px;
        color:#0A376E;
        font-weight:600;
        font-family:'Alegreya';
    }
`

const Text = styled.ul`
    display:flex;
    flex-direction:column;
    align-items:flex-start;
    width:500px;
    li{
        margin:5px 0px;
        font-weight:600;
    }
`

const TW = styled.form`
    display:flex;
    flex-direction:column;
    width:500px;
    height:300px;
    align-items:center;
    justify-content:space-around;
    button{
        width:100px;
        height:35px;
        font-weight:600;
        font-size:20px;
        color:white;
        background-color:#1473E6;
        border-radius:15px;
        border:1px solid #1473E6;
        margin-top:-30px;
    }
`

const Name = styled.div`
    display:flex;
    justify-content:space-around;
    align-items:center;
    width:500px;
    height:30px;
    span{
        font-weight:600;
        font-size:20px;
        margin-left:20px;
    }
`

const Sogae = styled.div`
    display:flex;
    justify-content:space-around;
    align-items:flex-start;
    width:500px;
    height:150px;
    margin-top:5px;
    span{
        font-weight:600;
        font-size:20px;
        margin-left:10px;
    }
`

function FirstLogin(){
    const [name, setName] = useState(''),
        [info, setInfo] = useState('');

    const dispatch = useDispatch(),
        navigate = useNavigate();
    
    const onName = e => {
        setName(e.target.value);
    }

    const onInfo = e => {
        setInfo(e.target.value);
    }

    const onSubmit = (e) => {
        e.preventDefault();
        let body = {name,info};
        axios.post(`/api/manager/setSetting`,body)
        .then(res => res.status == 200 ? navigate('/home') : null)
    }

    return(
        <Wrapper>
            <LoginW>
                <Title>
                    <span>잔반제로</span>
                    <span>초기 설정</span>
                </Title>
                <Text>
                    <li>본 설정은 초기 1회만 필요합니다.</li>
                    <li>단체명은 초기 설정 후 변경이 불가능합니다.</li>
                    <li>소개 문구는 설정에서 변경 가능합니다.</li>
                    <li>모든 설정은 일반 사용자에게 공개되니 신중히 입력해주세요.</li>
                </Text>
                <TW>
                    <Name>
                        <span>단체명</span>
                        <input 
                        style={{marginRight:'20px',width:'300px',height:'30px',borderRadius:'20px',border:'1px solid gray',padding:'0px 8px'}}
                        value={name}
                        onChange={onName}
                        />
                    </Name>
                    <Sogae>
                        <span>소개</span>
                        <textarea 
                        style={{marginRight:'10px', width:'300px',height:'100px',borderRadius:'20px',border:'1px solid gray',padding:'8px 8px'}}
                        value={info}
                        onChange={onInfo}
                        />
                    </Sogae>
                    <button onClick={onSubmit}>저장</button>
                </TW>
            </LoginW>
        </Wrapper>
    )
}

export default FirstLogin;