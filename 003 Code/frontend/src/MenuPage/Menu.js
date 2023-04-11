import { useDispatch } from "react-redux";
import styled from "styled-components";
import { IoMdLogOut } from "react-icons/io";
import { R_logout } from '../store';
import { getTest, makeImagePath } from "../api&utils";
import {useQuery} from '@tanstack/react-query';

const Wrapper = styled.div`
display:flex;
flex-direction:column;
width:85vw;
height:100vh;
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

const ItemWrapper = styled.div`
    width:85vw;
    height:100vh;
    span{
        margin-left:30px;
        font-weight:600;
    }
`;

const Item = styled.div`
    width:57vw;
    height:18vh;
    background-color:#C8D5EF;
    margin:10px 30px;
    display:flex;
    align-items:center;
`;

const Itemimg = styled.div`
width:11vw;
margin-left:12px;
height:15vh;
background-image:url(${props => props.bgPhoto});
background-size:cover;
background-position:center center;
`;

const ItemInfo = styled.div`
    width:30vw;
    height:15vh;
    display:flex;
    flex-direction:column;
    justify-content:center;
    align-items:flex-start;
    span{
        font-size:13px;
    }
    span:first-child{
        margin-left:28px;
        font-size:24px;
        font-weight:600;
    }
`
const ItemUD = styled.div`
    width:7vw;
    height:15vh;
    display:flex;
    flex-direction:column;
    justify-content:flex-end;
    align-items:center;
    button{
        width:6vw;
        height:4vh;
        margin-bottom:5px;
        background-color:#DC3546;
        color:white;
        border-radius:5px;
        border:1px solid #DC3546;
    }
`

const Itemfinal = styled.div`
    width:10vw;
    height:15vh;
    display:flex;
    justify-content:center;
    align-items:flex-end;
    button{
        width:7vw;
        height:4vh;
        margin-bottom:5px;
        background-color:#6F4FF2;
        color:white;
        border-radius:5px;
        border:1px solid #6F4FF2;
    }
`

function Menu(){
    const dispatch = useDispatch();
    const {data, isLoading} = useQuery(['test'],getTest)
    
    console.log(data)
    return(
        <Wrapper>
            <Nav>
                <span>메뉴 관리</span>
                <div><IoMdLogOut onClick={() => dispatch(R_logout())}/></div>
            </Nav>
            <ItemWrapper>
                <span>전체 3종</span>
                <Item>
                    <Itemimg>1</Itemimg>
                    <ItemInfo>
                        <span>해물순두부 찌개</span>
                        <span>따뜻한 순두부찌개입니다</span>
                        <span>알레르기정보: 복숭아,대두</span>
                        <span>5000원</span>
                    </ItemInfo>
                    <ItemUD>
                        <button>삭제</button>
                        <button>품절</button>
                    </ItemUD>
                    <Itemfinal>
                        <button>메뉴 수정</button>
                    </Itemfinal>
                </Item>
                {data?.results.map(data => 
                <Item >
                    <Itemimg bgPhoto={makeImagePath(data.backdrop_path,'w400'||'')}/>
                    <ItemInfo>
                        <span>{data.original_title}</span>  
                        <span>{data.overview.slice(0,8)+''}</span>  
                        <span>{data.release_date}</span>  
                        <span>{data.vote_count}</span>    
                    </ItemInfo> 
                    <ItemUD>
                        <button>삭제</button> 
                        <button>품절</button>    
                    </ItemUD>    
                    <Itemfinal>
                        <button>메뉴 수정</button>
                    </Itemfinal>
                </Item>)}
            </ItemWrapper>

        </Wrapper>
    )
}

export default Menu;