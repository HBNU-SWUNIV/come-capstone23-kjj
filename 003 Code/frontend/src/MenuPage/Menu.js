import styled from "styled-components";
import { makeImagePath } from "../api&utils";
import { useState } from "react";
import { useEffect } from "react";
import { useMatch, useNavigate } from "react-router-dom";
import axios from 'axios';
import { AiFillCloseCircle } from "react-icons/ai";
import man from "../image/man.png";
import Navtop from "../Components/Navtop";

const Wrapper = styled.div`
display:flex;
flex-direction:column;
width:85vw;
height:100vh;
margin-top:30px;
`;

const ItemWrapper = styled.div`
    width:85vw;
    height:100vh;
    position:relative;
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
    justify-content:center;
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
    position:relative;
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
    button{
        margin-left:14vw;
        background-color:#6F4FF2;
        width:5vw;
        height:4vh;
        color:white;
        border:1px solid #6F4FF2;
        border-radius:5px;
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

const CheckDelete = styled.div`
    width:24vw;
    height:36vh;
    background-color:white;
    border:1px solid #1473E6;
    position:absolute; 
    margin:0 auto;
    left:0;
    right:0;
    top:250px;
    position:fixed;
    display:flex;
    flex-direction:column;
    align-items:center;
`

const CheckDelete_img = styled.div`
    width:10vw;
    height:15vh;
    background-image:url(${props => props.bgPhoto});
    background-size:cover;
    background-position:center center;
    
`

const CheckDelete_btn = styled.div`
    width:12vw;
    height:15vh;
    display:flex;
    align-items:center;
    justify-content:space-between;
    button{
        width:4.5vw;
        height:4vh;
        border-radius:5px;
        color:white;
    }
    button:first-child{
        background-color:#DC3546;
        border:1px solid #DC3546;
    }
    button:last-child{
        background-color:#6F4FF2;
        border:1px solid #6F4FF2;
    }
`;

const 메뉴추가 = styled.button`
    width:12vw;
    height:5vh;
    background-color:#C8D5EF;
    border-radius:5px;
    border: 1px solid black;
    position:absolute;
    right:0;
    top:110px;
    margin-right:10vw;
`;

const UpdateWrapper = styled.div`
    width:30vw;
    height:80vh;
    background-color:white;
    position:absolute;
    border:1px solid #1473E6;
    left:0;
    right:0;
    margin:0 auto;
    top:100px;
    position:fixed;
    display:flex;
    flex-direction:column;
    justify-content:space-between;
    align-items:center;
    button{
        width:6vw;
        height:5vh;
        background-color:#6F4FF2;
        border:1px solid #6F4FF2;
        border-radius:15px;
        margin-bottom:25px;
        color:white;
    }
`;

const UpdateTitle = styled.div`
    width:26vw;
    height:10vh;
    display:flex;
    justify-content:space-between;
    align-items:center;
    span{
        font-size:20px;
        font-weight:600;
    }
`

const UpdateImg = styled.div`
    width:24vw;
    height:30vh;
    display:flex;
    justify-content:space-between;
    align-items:center;
    button{
        background-color:#1473E6;
        border:1px solid #1473E6;
        border-radius:15px;
        width:6vw;
        height:5vh;
        color:white;
        margin-right:60px;
    }
`;

const UpdateImg_img = styled.div`
    width:10vw;
    height:20vh;
    background-image:url(${props => props.bgPhoto});
    background-size:cover;
    background-position:center center;
`

const UpdateText = styled.form`
    width:22vw;
    height:25vh;
    margin-bottom:60px;
    display:flex;
    flex-direction:column;
    justify-content:space-between;
    align-items:center;
    div{
        width:22vw;
        height:7vh;
        display:flex;
        justify-content:space-between;
        align-items:center;
        span{
            font-size:15px;
            font-weight:600;
        }
        input{
            width:12vw;
            height:3vh;
            border-radius:25px;
            border:1px solid gray;
        }
    }
`;


function Menu(){
    const [isLoading, setIsLoading] = useState(true);
    let [savedData, setSaveddata] = useState([]);
    const navigate = useNavigate();
    const deletePathMatch = useMatch('/menu/:deleteId');
    const UpdatePathMatch = useMatch('/menu/update/:updateId');
    const [품절, set품절] = useState([]);

    useEffect(() => {
        const getApi = async() => {
            const {data} = await axios.get('https://api.themoviedb.org/3/movie/top_rated?api_key=505148347d18c10aeac2faa958dbbf5c');
            return data;
        }
        getApi().then(result => setSaveddata(result.results))
        .then(setIsLoading(false));
    },[]) 
    
    const DeletePath = deletePathMatch?.params.deleteId && savedData?.find(data => data.id == deletePathMatch.params.deleteId);
    const UpdatePath = UpdatePathMatch?.params.updateId && savedData?.find(data => data.id == UpdatePathMatch.params.updateId);
    
    const onDelete = (id) => {
        navigate(`/menu/${id}`);
    }
    const onFinalDelete = (id) => {
        let newData = [];
        newData = savedData?.filter(prev => prev.id !== id);
        setSaveddata(newData);
        navigate('/menu');
    }
    const onUpdate = (id) => {
        navigate(`/menu/update/${id}`);
    }
    const on품절 = (id) => {
        set품절(prev => [
            ...prev, {id:id}
        ])  
    };
    const on재판매 = (id) => {
        let 재판매 = [...품절];
        재판매 = 재판매.filter(a => a.id !== id);
        set품절(재판매);
    }

    return(
        <Wrapper>
            <Navtop pages={"메뉴 관리"}/>
            {
                isLoading? <h1 style={{marginTop:'150px'}}>'Loading..'</h1> : 
            
            <ItemWrapper>
                <span>전체 {savedData?.length}종</span>
            
                {savedData?.map(data => 
                <Item 
                style={{opacity:`${품절.filter(soldout => soldout.id == data.id).length == 1 ? '0.5' : '1' }`}}>
                    <Itemimg bgPhoto={makeImagePath(data?.backdrop_path,'w400'||'')}/>
                    <ItemInfo>
                        <span>{data?.original_title}</span>  
                        <span>{data?.overview.slice(0,8)+''}</span>  
                        <span>{data?.release_date}</span>  
                        <span>{data?.vote_count}</span> 
                        <span 
                        style={{position:'absolute',marginLeft:'12vw',color:'#DC3546',fontSize:'24px'}}>
                            {품절.filter(a=> a.id == data.id).length == 0 ? '':'품 절 되었어요'}
                        </span> 
                        {품절.filter(a=> a.id == data.id).length == 0 ? null : 
                        <button onClick={() => on재판매(data?.id)}>재판매</button> }  
                    </ItemInfo> 
                    <ItemUD>
                        <button onClick={() => onDelete(data?.id)}>삭제</button> 
                        <button onClick={() => on품절(data?.id)}>품절</button>    
                    </ItemUD>    
                    <Itemfinal>
                        <button type="submit" onClick={() => onUpdate(data?.id)}>메뉴 수정</button>
                    </Itemfinal>
                </Item>)}
                
            </ItemWrapper>
            }
        
        {deletePathMatch?
        <CheckDelete>
        <div style={{display:'flex',justifyContent:'center',marginTop:'10px'}}>
            <CheckDelete_img bgPhoto={makeImagePath(DeletePath?.backdrop_path,'w400'||'')}/>
            <div style={{display:'flex',flexDirection:'column',width:'10vw',height:'15vh',alignItems:'center',justifyContent:'center',marginLeft:'10px'}}>
                <span style={{fontSize:'22px',fontWeight:'600'}}>{DeletePath?.original_title}</span>
                <span>{DeletePath?.vote_count}</span>
            </div>
        </div>
        <span style={{marginTop:'30px',fontWeight:'600'}}>정말 삭제하시겠습니까?</span>
        <CheckDelete_btn>
            <button onClick={() => onFinalDelete(DeletePath?.id)}>삭제</button>
            <button onClick={() => navigate('/menu')}>취소</button>
        </CheckDelete_btn>
        </CheckDelete>
        :
        null}

        {UpdatePathMatch?
        <UpdateWrapper>
            <UpdateTitle>
                <span>메뉴 수정</span>
                <AiFillCloseCircle onClick={() => navigate('/menu')} style={{fontSize:'20px',fontWeight:600}}/>
            </UpdateTitle>
            <UpdateImg>
                <UpdateImg_img bgPhoto={makeImagePath(UpdatePath?.backdrop_path,'w400'||'')}/>
                <button>이미지 수정</button>
            </UpdateImg>
            <UpdateText>
                <div>
                    <span>메뉴명</span>
                    <input placeholder={UpdatePath?.original_title}/>
                </div>
                <div>
                    <span>가격</span>
                    <input placeholder={UpdatePath?.overview.slice(0,8)+''}/>
                </div>
                <div>
                    <span>소개</span>
                    <input placeholder={UpdatePath?.release_date}/>
                </div>
                <div>
                    <span>추가정보</span>
                    <input placeholder={UpdatePath?.vote_count}/>
                </div>
            </UpdateText>
            <button onClick={()=>navigate('/menu')}>저장</button>
        </UpdateWrapper>
        :
        null}

        <메뉴추가 onClick={() => navigate('/menu/update/1')}>메뉴 추가</메뉴추가>


        </Wrapper>
    )
}

export default Menu;