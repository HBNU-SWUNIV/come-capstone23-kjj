import styled from "styled-components";
import { makeImagePath } from "../api&utils";
import { useState } from "react";
import { useEffect } from "react";
import { useMatch, useNavigate } from "react-router-dom";
import axios from 'axios';
import { AiFillCloseCircle } from "react-icons/ai";
import Navtop from "../Components/Navtop";
import { BsToggle2Off,BsToggle2On } from "react-icons/bs";
import Overlay from "../Components/Overlay";
import { AiFillPlusCircle } from "react-icons/ai";
import shortid from "shortid";

const Wrapper = styled.div`
display:flex;
flex-direction:column;
width:85vw;
height:100vh;
margin-top:30px;
position:relative;
`;
const Tip = styled.ul`
    display:flex;
    flex-direction:column;
    align-items:space-evenly;
    justify-content:center;
    width:33vw;
    height:10vh;
    position:absolute;
    right:0;
    margin-top:3vh;
    margin-right:12vw;
    li{
        font-weight:500;
        font-size:15px;
        color:#C63333;
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
`;
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
`;
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
`;
const CheckDelete = styled.div`
    width:24vw;
    z-index:1;
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
`;
const CheckDelete_img = styled.div`
    width:10vw;
    height:15vh;
    background-image:url(${props => props.bgPhoto});
    background-size:cover;
    background-position:center center;
    
`;
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
const UpdateWrapper = styled.div`
    z-index:1;
    width:34vw;
    height:80vh;
    border-radius:15px;
    background-color:white;
    position:absolute;
    border:1px solid white;
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
    width:34.2vw;
    margin-top:-1.2px;
    height:10vh;
    border-top-left-radius:15px;
    border-top-right-radius:15px;
    display:flex;
    background-color:#d9d9d9;
    justify-content:space-between;
    align-items:center;
    span{
        font-size:20px;
        font-weight:600;
        margin-left:20px;
    }
`;
const UpdateText = styled.form`
    width:32vw;
    height:25vh;
    display:flex;
    flex-direction:column;
    justify-content:space-between;
    align-items:center;
    div{
        width:28vw;
        height:10vh;
        display:flex;
        justify-content:space-between;
        align-items:center;
        span{
            font-size:15px;
            font-weight:600;
            color:#A5A5A5;
        }
        input{
            width:14vw;
            height:3vh;
            border-radius:25px;
            border:1px solid #a9a9a9;
        }
    }
`;
const UpdateImg = styled.div`
    width:32vw;
    height:17vh;
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
        margin-top:20px;
    }
`;
const UpdateImg_defaultimg = styled.div`
width: 90px;
height: 90px;
border-radius:50px;
background-color:white;
display:flex;
justify-content:center;
align-items:center;
color:#979797;
border:1px solid #979797;
margin-left:20px;
`;
const UpdateImg_img = styled.div`
    width:10vw;
    height:20vh;
    background-image:url(${props => props.bgPhoto});
    background-size:cover;
    background-position:center center;
    margin-left:20px;
`;
const Updatebackban = styled.div`
    width:32vw;
    height:10vh;
    display:flex;
    justify-content:space-between;
    align-items:center;
`;
const Updatebackban_text = styled.div`
    display:flex;
    flex-direction:column;
    align-items:space-between;
    justify-content:center;
    span{
        font-size:20px;
        color:#a9a9a9;
    }
    span:last-child{
        font-size:10px;
        color:#a9a9a9;
    }
`;



function Menu(){
    const [isLoading, setIsLoading] = useState(true);
    const [isBackban, setIsbackban] = useState(true);
    let [savedData, setSaveddata] = useState([]);
    const navigate = useNavigate();
    const deletePathMatch = useMatch('/menu/:deleteId');
    const UpdatePathMatch = useMatch('/menu/update/:updateId');
    const [품절, set품절] = useState([]);

    useEffect(() => {
        // const getApi = async() => {
        //     const {data} = await axios.get('/api/user/menu');
        //     return data;
        // }
        // getApi().then(result => setSaveddata(result))
        // .then(setIsLoading(false));

        axios.get('/api/manager/menu')
        .then(res => setSaveddata(res.data))
        .then(setIsLoading(false))
    },[]) 

    
    const DeletePath = deletePathMatch?.params.deleteId && savedData?.find(data => data.id == deletePathMatch.params.deleteId);
    const UpdatePath = UpdatePathMatch?.params.updateId && savedData?.find(data => data.id == UpdatePathMatch.params.updateId);
    
    console.log(savedData)

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
    
    const [name, setMenu] = useState('');
    const [cost, setCost] = useState('');
    const [info, setInfo] = useState('');
    const [details, setDetails] = useState('');

    const onMenuUpdate = () => {
        const formdata = new FormData();
        let body = {name,cost,info,details};
        const blob = new Blob([JSON.stringify(body)], {type:"application/json"})
        formdata.append("data",blob);
        console.log(formdata)
        axios({
            method:'POST',
            url:'/api/manager/menu/add',
            data:formdata,
            headers: {
                "Content-Type": "multipart/form-data", // Content-Type을 반드시 이렇게 하여야 한다.
              }
        })

        // axios.post(`/api/manager/menu/add`,data)
        // .then(res => console.log(res))
        navigate('/menu');
        setMenu('');
    } 

    return(
        <>
        <Wrapper>
            <Navtop pages={"메뉴 관리"}/>
            <Tip>
                <li>요일별 다른 메뉴가 있다면 백반으로 지정해보세요!</li>
                <li>백반으로 지정된 메뉴는 백반관리 페이지에서 요일별 식단표를 추가할 수 있어요!</li>
            </Tip>
            {isLoading? <h1 style={{marginTop:'100px',marginLeft:'20px'}}>Loading...</h1> : 
            <ItemWrapper>
                <span>전체 {savedData?.length}종</span>
                {savedData?.map(data => 
                <Item 
                key={shortid.generate()}
                style={{opacity:`${품절.filter(soldout => soldout.id == data.id).length == 1 ? '0.5' : '1' }`}}>
                    <Itemimg bgPhoto={makeImagePath(data?.backdrop_path,'w400'||'')}/>
                    <ItemInfo>
                        <span>{data?.name}</span>  
                        <span>{data?.details}</span>
                        <span>{data?.info}</span>
                        <span>{data?.cost}</span>
                        {/* 
                        <span>{data?.overview.slice(0,8)+''}</span>  
                        <span>{data?.release_date}</span>  
                        <span>{data?.vote_count}</span>  */}
                        <span 
                            style={{position:'absolute',marginLeft:'12vw',color:'#DC3546',fontSize:'24px'}}>
                            {품절.filter(a=> a.id == data.id).length == 0 ? '':'품 절 되었어요'}
                        </span> 
                        {품절.filter(a=> a.id == data.id).length == 0 ? null : 
                        <button onClick={() => on재판매(data?.id)}>
                            재판매
                        </button> }  
                    </ItemInfo> 
                    <ItemUD>
                        <button onClick={() => onDelete(data?.id)}>
                            삭제
                        </button> 
                        <button onClick={() => on품절(data?.id)}>
                            품절
                        </button>    
                    </ItemUD>    
                    <Itemfinal>
                        <button type="submit" onClick={() => onUpdate(data?.id)}>
                            메뉴 수정
                        </button>
                    </Itemfinal>
                </Item>)}
                
            </ItemWrapper>}
        
            <AiFillPlusCircle
            style={{
                position:'absolute',right:0,top:'110px',marginRight:'12vw',fontSize:'30px'
            }}
            onClick={() => navigate('/menu/update/1')}/>
        </Wrapper>

        {deletePathMatch?
            <>
            <CheckDelete>
            <div 
            style={{display:'flex',justifyContent:'center',marginTop:'10px'}}>
            <CheckDelete_img bgPhoto={makeImagePath(DeletePath?.backdrop_path,'w400'||'')}/>
            <div 
            style={{display:'flex',flexDirection:'column',width:'10vw',height:'15vh',alignItems:'center',justifyContent:'center',marginLeft:'10px'}}>
                <span 
                style={{fontSize:'22px',fontWeight:'600'}}>
                    {DeletePath?.original_title}
                </span>
                <span>
                    {DeletePath?.vote_count}
                </span>
            </div>
            </div>
            <span style={{marginTop:'30px',fontWeight:'600'}}>
            정말 삭제하시겠습니까?
            </span>
            <CheckDelete_btn>
            <button onClick={() => onFinalDelete(DeletePath?.id)}>
                삭제
            </button>
            <button onClick={() => navigate('/menu')}>
                취소
            </button>
            </CheckDelete_btn>
            </CheckDelete>
            <Overlay/>
            </>
            :null}

        {UpdatePathMatch?
            <>
            <UpdateWrapper>

            <UpdateTitle>
                <span>{UpdatePathMatch.params.updateId == 1 ? '메뉴 추가' : '메뉴 수정'}</span>
                <AiFillCloseCircle onClick={() => navigate('/menu')} style={{fontSize:'20px',fontWeight:600,marginRight:'20px'}}/>
            </UpdateTitle>

            <UpdateText id="data">
                <div>
                    <span>메뉴명</span>
                    <input value={name} onChange={(e) => setMenu(e.target.value)}/>
                </div>
                <hr style={{color:'#a9a9a9',width:'34vw', marginLeft:'-1vw'}}/>
                <div>
                    <span>가격</span>
                    <input value={cost} onChange={e => setCost(e.target.value)}/>
                </div>
                <hr style={{color:'#a9a9a9',width:'34vw', marginLeft:'-1vw'}}/>
                <div>
                    <span>소개</span>
                    <input value={info} onChange={e => setInfo(e.target.value)}/>
                </div>
                <hr style={{color:'#a9a9a9',width:'34vw', marginLeft:'-1vw'}}/>
                <div>
                    <span>알레르기 정보</span>
                    <input value={details} onChange={e=> setDetails(e.target.value)}/>
                </div>
                <hr style={{color:'#a9a9a9',width:'34vw', marginLeft:'-1vw'}}/>
            </UpdateText>

            <UpdateImg>
                <UpdateImg_defaultimg>
                    이미지없음
                </UpdateImg_defaultimg>

                <button>
                    이미지 첨부
                </button>
            </UpdateImg>

            <hr style={{width:'34vw'}}/>
            
                {isBackban ? 
                <Updatebackban>
                <Updatebackban_text>
                    <span>백반으로 지정하시겠습니까?</span>
                    <span>백반은 최대 1개의 메뉴만 저장가능하며, 요일 별 식단표는 백반관리에서 등록할 수 있습니다.</span>
                </Updatebackban_text>
                <BsToggle2Off
                onClick={()=> setIsbackban(false)}
                style={{fontSize:'30px',marginRight:'20px',color:'#d9d9d9'}}/>
                </Updatebackban>
                :
                <Updatebackban>
                <Updatebackban_text>
                    <span>지정된 백반이 이미 있습니다.</span>
                    <span>백반을 등록하시려면 우선 지정된 백반을 삭제해주세요.</span>
                </Updatebackban_text>
                <BsToggle2On
                onClick={()=> setIsbackban(true)}
                style={{fontSize:'30px',marginRight:'20px',color:'#5856D6'}}/>
                </Updatebackban>
                }
            

            <button onClick= {onMenuUpdate}>저장</button>

        </UpdateWrapper>
        <Overlay/>
        </>:null}
        </>
    )}

export default Menu;