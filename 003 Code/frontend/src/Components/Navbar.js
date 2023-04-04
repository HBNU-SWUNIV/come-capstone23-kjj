import styled from "styled-components";
import { MdOutlineAddBox ,MdAddBox} from "react-icons/md";
import { AiOutlineHeart ,AiFillHeart, AiFillStar, AiOutlineStar} from "react-icons/ai";
import { FaRegUserCircle,FaUserCircle } from "react-icons/fa";
import { DiUbuntu } from "react-icons/di";
import { useState } from "react";

const 래퍼 = styled.div`
width:60px;
height:1200px;
background-color:#1D1932;
display:flex;
flex-direction:column;
align-items:center;
justify-content:flex-start;
position:fixed;
`

const ItemWrapper = styled.div`
font-size:20px;
width:40px;
margin-top:30px;
height:40px;
display:flex;
justify-content:center;
align-items:center;
`

function Navbar(){
    let [now,setNow] = useState(2);
    
    const onclick = (id) => {
        setNow(id);
    }
    return(
        <래퍼>
            {/* 클릭시색상=6F4FF2, 클릭안할시=65646A */}
            <ItemWrapper>
                <DiUbuntu style={{color:'#6F4FF2',fontSize:'40px',marginBottom:'20px'}}/>
            </ItemWrapper>
            <ItemWrapper>
                <MdOutlineAddBox 
                    onClick={() => onclick(2)} 
                    style={{color:`${now==2?'#6F4FF2':'#65646A'}`}}/>
            </ItemWrapper>
            <ItemWrapper>
                <AiOutlineHeart 
                    onClick={() => onclick(3)}
                    style={{color:`${now==3?'#6F4FF2':'#65646A'}`}}/></ItemWrapper>
            <ItemWrapper>
                <AiFillStar 
                    onClick={() => onclick(4)}
                    style={{color:`${now==4?'#6F4FF2':'#65646A'}`}}/>
            </ItemWrapper>
            <ItemWrapper>
                <FaRegUserCircle 
                    onClick={() => onclick(5)}
                    style={{color:`${now==5?'#6F4FF2':'#65646A'}`}}/>
            </ItemWrapper>
        </래퍼>
    )
}

export default Navbar;