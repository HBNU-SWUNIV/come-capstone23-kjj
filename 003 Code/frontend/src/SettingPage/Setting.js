import { useDispatch } from "react-redux";
import styled from "styled-components";
import { IoMdLogOut } from "react-icons/io";
import { R_logout } from '../store';
import Calander from "../BackbanPage/Calander";
import { useState } from "react";


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

const UserWrapper = styled.div`
    width:25vw;
    height:40vh;
    background-color:#C8D5EF;
    margin-left:10vw;
    display:flex;
    flex-direction:column;
    align-items:center;
`;

const UserTop = styled.div`
    width:21vw;
    height:18vh;
    display:flex;
    justify-content:space-between;
    align-items:center;
`;

const UserBottom = styled.div`
    margin-top:20px;
    width:15vw;
    height:18vh;
    display:flex;
    flex-direction:column;
    justify-content:space-around;
    align-items:center;
    button{
        width:13vw;
        height:4vh;
        border:1px solid black;
        background-color:#D9D9D9;
        border-radius:5px;
    }
`;

const Img = styled.div`
    width:120px;
    height:120px;
    background-color:white;
    border-radius:100px;
    margin-top:20px;
    display:flex;
    justify-content:center;
    align-items:center;
    color:#979797;
    `;

const UserBottom_nickname = styled.div`
margin-top:20px;
width:24vw;
height:18vh;
display:flex;
justify-content:flex-end;
align-items:flex-end;
button{
    width:80px;
    height:33px;
    margin:0px 5px;
    border-radius:7px;
}
button:first-child{
    background-color:#6F4FF2;
}
button:last-child{
    background-color:#DC3546;
}
`;

const UserBottom_password = styled.div`
    margin-top:20px;
    width:24vw;
    height:18vh;
    display:flex-direction;
    justify-content:flex-start;
    align-items:center;
    div{
        display:flex;
        width:24vw;
        height:5vh;
        justify-content:space-between;
        align-items:center;
        input{
            width:15vw;
            height:2.5vh;
            border:1px solid black;
            border-radius:15px;
        }
    }
    div:last-child{
        display:flex;
        justify-content:flex-end;
        align-items:center;
    }
`;

const UserBottom_password_btn = styled.div`
    width:24vw;
    height:5vh;
    margin-top:20px;
    button{
        width:80px;
        height:33px;
        margin:0px 5px;
        border-radius:7px;
    }
    button:first-child{
        background-color:#6F4FF2;
    }
    button:last-child{
        background-color:#DC3546;
    }
`;

const UserBottom_logout = styled.div`
    margin-top:20px;
    width:15vw;
    height:10vh;
    display:flex;
    flex-direction:column;
    justify-content:center;
    align-items:center;
    span{
        font-size:18px;
        font-weight:500;
        margin:10px 0px;
    }
    `;

const UserBottom_logout_btndiv = styled.div`
    width:14vw;
    height:5vh;
    display:flex;
    justify-content:space-between;
    align-items:center;
    button{
        width:80px;
        height:33px;
        margin:0px 5px;
        border-radius:7px;
    }
    button:first-child{
        background-color:#6F4FF2;
    }
    button:last-child{
        background-color:#DC3546;
    }
`;

function Setting(){
    const dispatch = useDispatch();
    let [test, setTest] = useState('');
    

    return(
        <Wrapper>
        <Nav>
            <span>설정</span>
            <div><IoMdLogOut onClick={() => dispatch(R_logout())}/></div>
        </Nav>
        <FirstWrapper>
            <Message>
                <span>식당 소개 메시지를 설정할 수 있어요.</span>
                <input placeholder="백반단가 5000원"/>
                <MessageDiv><button>수정</button></MessageDiv>
            </Message>
            <UserWrapper>
                <UserTop>
                    <div>
                        <span style={{fontSize:'16px',fontWeight:'500'}}>관리자</span>
                        <div>
                        {test == '' || test == 'changepassword' || test == 'logout' ? <span style={{fontSize:'32px',fontWeight:'600'}}>한밭대학교</span> :
                        test == 'changenickname' ? <input style={{width:'10vw',height:'4vh',border:'1px solid white'}} placeholder="한밭대학교"/> : null }
                        <span style={{fontSize:'20px',fontWeight:'400',marginLeft:'5px'}}>님</span>
                        </div>
                    </div>
                    <Img>
                        이미지없음
                    </Img>
                </UserTop>
                {test == '' ? 
                <UserBottom>
                <button onClick={() => setTest('changenickname')}>닉네임 변경</button>
                <button onClick={() => setTest('changepassword')}>비밀번호 변경</button>
                <button onClick={() => setTest('logout')}>로그아웃</button>
                </UserBottom>:

                test == 'changenickname' ?
                <UserBottom_nickname>
                    <button onClick={() => setTest('')}>변경</button>
                    <button onClick={() => setTest('')}>취소</button>
                </UserBottom_nickname>:
                
                test == 'changepassword' ? 
                <UserBottom_password>
                    <div>
                        <span>현재 비밀번호</span>
                        <input/>
                    </div>
                    <div>
                        <span style={{marginLeft:'14px'}}>새 비밀번호</span>
                        <input/>
                    </div>
                    <UserBottom_password_btn>
                    <button onClick={() => setTest('')}>변경</button>
                    <button onClick={() => setTest('')}>취소</button>
                    </UserBottom_password_btn>
                </UserBottom_password>:
                
                test == 'logout' && 
                <UserBottom_logout>
                    <span>로그아웃 하시겠습니까?</span>
                    <UserBottom_logout_btndiv>
                        <button onClick={() => setTest('')}>확인</button>
                        <button onClick={() => setTest('')}>취소</button>
                    </UserBottom_logout_btndiv>
                </UserBottom_logout>
                }
            </UserWrapper>
        </FirstWrapper>

        <CalanderWrapper>
            <CalanderHeader>
                <span>휴일을 설정할 수 있어요.</span>
                <button>휴일 등록</button>
            </CalanderHeader>
            <Calander/>
        </CalanderWrapper>
        </Wrapper>
    )
}
export default Setting;