import styled from 'styled-components';
import { RiHomeSmileFill } from "react-icons/ri";
import { TbChartBar } from "react-icons/tb";
import { BiHomeSmile } from "react-icons/bi";
import { BsFillBarChartFill } from "react-icons/bs";
import { AiOutlineSetting, AiTwotoneSetting} from "react-icons/ai";
import { useState } from 'react';
import {useNavigate} from 'react-router-dom';

const Wrapper = styled.div`
    width:15vw;
    height:170vh;
    border:1px solid #DDDDDD;
`

const Image = styled.button`
    width: 100px;
    height: 100px;
    border-radius:50px;
    margin-bottom:20px;
    margin-top:-40px;
    background-color:white;
    color:#979797;
    border:1px solid #979797;
`;

const Title = styled.div`
    width:15vw;
    height:20vh;
    display:flex;
    flex-direction:column;
    align-items:center;
    justify-content:center;
    margin-top:60px;
    span{
        font-size:22px;
        color:#0A376E;
        font-family:'Alegreya';
        font-weight:600;
    }
`

const Menus = styled.div`
    width:15vw;
    height:30vh;
    margin-top:10px;
    display:flex;
    flex-direction:column;
    justify-content:space-evenly;
    align-items:center;
    div{
        margin-left:40px;
        display:flex;
        justify-content:flex-between;
        align-items:center;
        width:10vw;
        height:4vh;
        border-radius:15px;
        font-size:17px;
        color:#0C4284;
    }
    span{
        margin-left:20px;
    }
    div:first-child{
        width:4vw;
        margin-left:-45px;
    }
    div:nth-child(2),div:nth-child(3){
        width:8vw;
        margin-left:10px;
    }
    div:last-child{
        width:5vw;
        margin-left:-32px;
    }
`

function Navbar(){
    let [id,setId] = useState(1);
    const navigate = useNavigate();
    
    return(
       <Wrapper>
            <Title>
                <Image>default Image</Image>
                <span
                style={{marginLeft:'-40px',fontSize:'30px'}}>
                    잔반제로
                </span>
                <span>수요 관리 시스템</span>
            </Title>
            <Menus>
                <div onClick={() => {setId(1)
                                     navigate('/home')}}
                    style={{backgroundColor:`${id==1?'#DAE9FC':'white'}`}}>
                    {id==1? <RiHomeSmileFill style={{fontSize:'20px'}}/>:<BiHomeSmile style={{fontSize:'20px'}}/>}
                    <span>홈</span>
                </div>
                
                <div onClick={() => {setId(2)
                                     navigate('/Menu')}}
                    style={{backgroundColor:`${id==2?'#DAE9FC':'white'}`}}>
                    {id==2? <BsFillBarChartFill style={{fontSize:'20px'}}/> : <TbChartBar style={{fontSize:'20px'}}/>}
                    <span>메뉴 관리</span>
                </div>

                <div onClick={() => {setId(3)
                                     navigate('/backban')}}
                    style={{backgroundColor:`${id==3?'#DAE9FC':'white'}`}}>
                     {id==3? <BsFillBarChartFill style={{fontSize:'20px'}}/> : <TbChartBar style={{fontSize:'20px'}}/>}
                    <span>백반 관리</span>
                </div>

                <div onClick={() => {setId(4)
                                     navigate('/setting')}}
                    style={{backgroundColor:`${id==4?'#DAE9FC':'white'}`}}>
                    {id==4? <AiTwotoneSetting style={{fontSize:'20px'}}/> : <AiOutlineSetting style={{fontSize:'20px'}}/>}        
                    <span>설정</span>
                </div>
            </Menus>
       </Wrapper>
    )
}

export default Navbar;