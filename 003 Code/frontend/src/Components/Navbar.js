import styled from 'styled-components';
import { RiHomeSmileFill } from "react-icons/ri";
import { TbChartBar } from "react-icons/tb";
import { BiHomeSmile } from "react-icons/bi";
import { BsFillBarChartFill } from "react-icons/bs";
import { AiOutlineSetting, AiTwotoneSetting} from "react-icons/ai";
import {useLocation, useNavigate} from 'react-router-dom';
import { IoCalendarNumberOutline,IoCalendarNumber,IoFastFoodOutline,IoFastFoodSharp } from "react-icons/io5";
import imagesrc from '../image/icon.png'

const Wrapper = styled.div`
    width:15vw;
    height:170vh;
    border:1px solid #DDDDDD;
    position:fixed;
`

const PWrapper = styled.div`
    width:15vw;
    height:170vh;
`

const Image = styled.button`
    width: 90px;
    height: 90px;
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
        font-family:'DeliveryFont';
        font-weight:500;
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
        display:flex;
        justify-content:flex-between;
        align-items:center;
        width:20vw;
        height:7vh;
        border-radius:15px;
        font-size:17px;
        color:#0C4284;
        span{
            font-family:'DeliveryFont';
        }
    }
    span{
        margin-left:20px;
    }
    div:first-child{
        width:10vw;
        // margin-left:-45px;
    }
    div:nth-child(2),div:nth-child(3){
        width:10vw;
        // margin-left:10px;
    }
    div:last-child{
        width:10vw;
        // margin-left:-32px;
    }
`

function Navbar(){
    const navigate = useNavigate();
    const location = useLocation();
    const nowPath = location.pathname;

    return(
        <>
         <Wrapper>
            <Title>
                {/* <Image>default Image</Image> */}
                <img src={imagesrc} />
                <span
                style={{fontSize:'30px'}}>
                    식재료 절약단
                </span>
                {/* <span>수요 관리 시스템</span> */}
            </Title>
            <Menus>
                <div onClick={() => {navigate('/home')}}
                     style={{backgroundColor:`${nowPath.startsWith('/home') ?'#DAE9FC':'white'}`}}>
                    {nowPath.startsWith('/home')? <RiHomeSmileFill style={{fontSize:'20px'}}/>:<BiHomeSmile style={{fontSize:'20px'}}/>}
                    <span>홈</span>
                </div>
                
                <div onClick={() => {navigate('/menu')}}
                     style={{backgroundColor:`${nowPath.startsWith('/menu') ?'#DAE9FC':'white'}`}}>
                    {nowPath.startsWith('/menu') ? <IoFastFoodSharp style={{fontSize:'20px'}}/> : <IoFastFoodOutline style={{fontSize:'20px'}}/>}
                    <span>사이드 메뉴</span>
                </div>

                <div onClick={() => {navigate('/backban')}}
                    style={{backgroundColor:`${nowPath.startsWith('/backban')?'#DAE9FC':'white'}`}}>
                     {nowPath.startsWith('/backban')? <IoCalendarNumber style={{fontSize:'20px'}}/> : <IoCalendarNumberOutline style={{fontSize:'20px'}}/>}
                    <span>오늘의 메뉴</span>
                </div>

                <div onClick={() => {navigate('/setting')}}
                    style={{backgroundColor:`${nowPath.startsWith('/setting')?'#DAE9FC':'white'}`}}>
                    {nowPath.startsWith('/setting')? <AiTwotoneSetting style={{fontSize:'20px'}}/> : <AiOutlineSetting style={{fontSize:'20px'}}/>}        
                    <span>설정</span>
                </div>
            </Menus>
       </Wrapper>
        <PWrapper/>
        </>
      
    )
}

export default Navbar;