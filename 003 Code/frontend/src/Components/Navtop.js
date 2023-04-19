import styled from "styled-components";
import man from "../image/man.png";


const NavWrapper = styled.div`
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
margin-left:68vw;
span:first-child{
    font-size:20px;
    font-weight:600;
    color:black;
}
`;  

const UserNameDiv = styled.div`
    display:flex;
    justify-content:center;
    align-items:center;
    width:12vw;
`;
function Navtop(props){
    return(
        <NavWrapper>
            <span>{props.pages}</span>
            
            {props.isLogin ? 
            <IsloginWrapper>
                <span style={{marginLeft:'-3px'}}>관리자</span>
                <UserNameDiv>
                    <span style={{fontSize:'24px', fontWeight:'600', color:'black',textDecoration:'underline',textDecorationThickness:'2px'}}>{props.isLogin}</span>
                    <span style={{fontSize:'18px',color:'black',marginLeft:'3px',marginTop:'2px'}}>님</span>
                </UserNameDiv>
            </IsloginWrapper> : null}
            <UserImage/>
        </NavWrapper>
    )
}

export default Navtop;