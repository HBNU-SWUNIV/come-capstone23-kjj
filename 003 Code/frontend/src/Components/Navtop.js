import styled from "styled-components";
import man from "../image/man.png";
import { useDispatch, useSelector } from "react-redux";
import { useEffect,useState } from "react";
import axios from "axios";
import { R_logout } from "../store";
import {useNavigate} from 'react-router-dom';

const NavWrapper = styled.div`
display:flex;
width:85vw;
height:15vh;
justify-content:space-between;
align-items:center;
span{
    margin-left:30px;
    font-size:30px;
    font-weight:500;
    font-family:'DeliveryFont';
    color:#0A376E;
}
div{
    margin-right:30px;
    font-size:30px;
    font-weight:600;
    font-family:'Alegreya';
}
`;

const UserImage = styled.div`
width:3vw;
height:6vh;
background-image:url(${man});
background-size:cover;
background-position:center center;
position:relative;
`;

const IsloginWrapper = styled.div`
display:flex;
flex-direction:column;
justify-content:center;
align-items:center;
position:absolute;
margin-left:67vw;
span:first-child{
    font-size:20px;
    font-weight:500;
    color:black;
}
`;  

const UserNameDiv = styled.div`
    display:flex;
    justify-content:flex-end;
    align-items:center;
    width:20vw;
    margin-left:-7vw;   
    height:5vh;
    span:first-child{
        text-align:right;
    }
`;

const UpdateWrapper = styled.div`
    width:10vw;
    height:20vh;
    border:1px solid black;
    border-radius:15px;
    position:absolute;
    right:0;
    background-color:white;
    margin-top:30vh;
    display:flex;
    flex-direction:column;
    justify-content:space-around;
    align-items:center;
    z-index:1;
    div{
        width:10vw;
        height:4vh;
        display:flex;
        justify-content:center;
        align-items:center;
        margin-right:0;
        font-size:16px;
        font-weight:600;
    }
`;

const Dotted_line = styled.hr`
    border:0px;
    border-top:2px dotted black;
    width:6vw;
    height:1vh;
    margin:-2vh 0px;
`;

const UpdateNameWrapper = styled.div`
    width:18vw;
    height:12vh;
    display:flex;
    position:absolute;
    margin:0 auto;
    right:0;
    left:0;
    flex-direction:column;
    justify-content:center;
    align-items:center;
    input{
        margin-top:3vh;
        width:9vw;
        height:3.5vh;
        font-size:15px;
    }
    button{
        background-color:#DC3546;
        position:absolute;
        right:0;
        margin-top:3vh;
        width:3vw;
        height:4vh;
        margin-right:0.2vw;
        border-radius:10px;
        border:1px solid #DC3546;
    }
`;

function Navtop(props){
    const User = useSelector(data => data.User);
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const [name,setName] = useState('');
    const [update, setUpdate] = useState(false);
    const [updateName, setUpdateName] = useState(false);
    const [newname, setNewname] = useState('');

    useEffect(() => {
       axios.get('/api/user/store')
       .then(res => setName(res.data.name))
        })

    const onUpdate = () => {
        setUpdate(prev => !prev)
    }

    const onUpdateName = () => {
        setUpdateName(prev => !prev)
    }

    const onLogout = () => {
        dispatch(R_logout())
        setUpdate(prev => !prev)
        // if(updateName ==true){
        //     setUpdateName(false)
        // }
    }

    const onExit = () => {
        setUpdate(false)
        setUpdateName(false)
    }

    const onChangename = () => {
        let body = {
            name:newname
        };
        axios({
            method:'PATCH',
            url:'/api/manager/store/title',
            data:body,
            headers: {
                "Content-Type": "application/json", // Content-Type을 반드시 이렇게 하여야 한다.
              }
        }).then(axios.get(`/api/user/store`).then(res=>{
            setName(res.data.name);
        }))
        .then(onExit())
        }

    return(<>
        <NavWrapper>
            <span>{props.pages}</span>
            
            {User.isLogin ? 
            <IsloginWrapper>
                <span style={{marginLeft:'5vw'}}>관리자</span>
                <UserNameDiv>
                    <span style={{fontSize:'22px', fontWeight:'600', color:'black',textDecoration:'underline',textDecorationThickness:'2px'}}>{name}</span>
                    <span style={{fontSize:'15px',color:'black',marginLeft:'3px',marginTop:'2px'}}>님</span>
                </UserNameDiv>
            </IsloginWrapper> 
            : null}

            <UserImage onClick={onUpdate}/>

            {update ? User.isLogin?
            <UpdateWrapper>
                <div onClick={onUpdateName}>닉네임 변경</div>
                <Dotted_line/>
                <div>비밀번호 변경</div>
                <Dotted_line/>
                <div onClick={onLogout}>로그아웃</div>
            </UpdateWrapper>   :
            <UpdateWrapper>
                <Dotted_line/>
                <div onClick={() => navigate('/')}>로그인</div>
                <Dotted_line/>
            </UpdateWrapper>
            :null
            }
            

            {updateName && 
            <UpdateNameWrapper>
                <input value={newname} onChange={e => setNewname(e.target.value)} placeholder={name}/>
                <button onClick={onChangename}>수정</button>
            </UpdateNameWrapper>}

        </NavWrapper>

        
            </>
    )
}

export default Navtop;