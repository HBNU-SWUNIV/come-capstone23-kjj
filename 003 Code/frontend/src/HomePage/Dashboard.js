import styled from 'styled-components';
import {useDispatch} from 'react-redux';
import ApexCharts from "react-apexcharts";
import { useState } from 'react';
import { AiFillCloseCircle } from "react-icons/ai";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import { useNavigate } from "react-router-dom";
import Navtop from '../Components/Navtop';


const Wrapper = styled.div`
margin-top:35px;
display:flex;
flex-direction:column;
width:85vw;
height:100vh;
`;

const Statistis = styled.div`
    width:70vw;
    height:15vh;
    display:flex;
    justify-content:flex-start;
    align-items:center;
`;

const 금일 = styled.div`
    display:flex;
    flex-direction:column;
    justify-content:space-between;
    align-items:flex-start;
    width:15vw;
    height:14vh;
    background-color:#C8D5EF;
    margin-left:30px;
    border-radius:5px;
    span{
        font-weight:600;
        font-size:20px;
        margin-left:10px;
    }
    span:first-child{
        margin-top:10px;
    }
    span:last-child{
        margin-bottom:10px;
    }
`;

const Total = styled.div`
    display:flex;
    justify-content:center;
    align-items:flex-end;
    width:15vw;
    height:15vh;
    margin-left:-20px;
    span{
        font-size:25px;
        font-weight:600;
        margin-bottom:10px;
    }
    span:first-child{
        font-size:65px;
        margin-right:5px;
        text-decoration:underline;
        text-decoration-thickness:3px;
        font-weight:400;
    }
`;

const 누적이용자 = styled.div`
    display:flex;
    flex-direction:column;
    justify-content:center;
    align-items:flex-start;
    width:13vw;
    height:12vh;
    background-color:#C8D5EF;
    margin-left:30px;
    span{
        margin-left:10px;
        font-size:15px;
    }
    span:first-child{
        font-size:30px;
        font-weight:600;
        margin-bottom:5px;
    }
`;

const 감소량 = styled.div`
    display:flex;
    flex-direction:column;
    justify-content:center;
    align-items:flex-start;
    width:16vw;
    height:11vh;
    background-color:#C8D5EF;
    border-radius:5px;
    margin-left:30px;
    span{
        margin-left:10px;
    }
    span:first-child{
        font-size:30px;
        font-weight:600;
        margin-bottom:7px;
    }
    span:last-child{
        font-size:15px;
    }
`;

const 차트2개 = styled.div`
    width:80vw;
    height:60vh;
    display:flex;
    justify-content:space-between;
    position:relative;
`

const FirstChart = styled.div`
    width:35vw;
    height:50vw;
    margin:20px 10px;
    span:first-child{
        font-size:20px;
        font-weight:600;
        margin-left:20px;
    }
    button{
        background-color:#C8D5EF;
        border-radius:5px;
        border:1px solid #C8D5EF;
        height:22px;
    }
    div{
        display:flex;
        justify-content:space-between;
    }
`;

const FirstChartGradient = styled.div`
    //아직 미구현
    width:35vw;
    height:10vh;
    margin-top:-60px;
    top:0;
`

const SecondChart = styled.div`
    width:38vw;
    height:55vh;
    margin:20px 20px;
    background-color:#C8D5EF;
`;

const LastChart = styled.div`
    width:50vw;
    margin-top:1vw;
    span{
        margin-left:20px;
        font-size:18px;
        font-weight:600;
    }
`;

const InputW = styled.form`
    width:460px;
    height:300px;
    border:2px solid #1473E6;
    position:absolute;
    left:0;
    right:0;
    margin:0 auto;
    top:250px;
    background-color:white;
    display:flex;
    justify-content:space-evenly;
    align-items:center;
    flex-direction:column;
    button:last-child{
        width:140px;
        height:35px;
        border-radius:20px;
        border:1px solid #1473E6;
        background-color:#1473E6;
        font-size:18px;
        color:white;
    }
`;

const Title = styled.div`
    display:flex;
    width:420px;
    height:70px;
    justify-content:space-between;
    align-items:center;
    margin-top:-30px;
`
const Items = styled.div`
    display:flex;
    width:360px;
    height:50px;
    justify-content:space-between;
    align-items:center;
    span{
        font-weight:600;
    }
`
    
function Dashboard(){
    const [Input, setInput] = useState(false);
    const onClick = () => {
        setInput(true);
    }

    const onsubmit = (e) => {
        e.preventDefault();
        setInput(false);
    }

    const [startDate, setStartDate] = useState(new Date());
    
    return(
        <Wrapper>
            <Navtop pages={"홈"}/>
        <Statistis>
            <금일>
                <span>23-04-07</span>
                <span>금일 이용자 수</span>
            </금일>

            <Total>
                <span>140</span>
                <span>명</span>
            </Total>
            
            <div style={{marginLeft:'-20px',borderRight:'thin solid #DDDDDD',height:'15vh'}}></div>
            
            <누적이용자>
                <span>4987</span>
                <span>누적 이용자</span>
            </누적이용자>

            <div style={{marginLeft:'30px',borderRight:'thin solid #DDDDDD',height:'15vh'}}></div>

            <감소량>
                <span>3.2Kg</span>
                <span>지난주 대비 음식물 쓰레기 감소량</span>
            </감소량>
        </Statistis>
        
        <차트2개>
        <FirstChart>
            <div>
                <span>지난주 대비 잔반량</span>
                <button onClick={onClick}>등록하러가기</button>
            </div>
            <ApexCharts
                type="line"
                series={ [
                    {
                        name:'지난주',
                        data:[20,30,10,50,60,20,40]
                    },
                    {
                        name:'이번주',
                        data:[30,30,40,20,10,10,20]
                    },
                ]}
                options={{
                    chart:{
                        toolbar:{show:false}
                    },
                    stroke:{
                        curve:'smooth',
                        width:2.5
                    },
                    xaxis:{
                        type:'category',
                        categories:['월','화','수','목','금','토','일']
                    },
                    fill:{
                        colors:'green',
                        opacity:0.9
                    }
                }}
            />
            <FirstChartGradient/>
        </FirstChart>
        
        <SecondChart>
            <span style={{fontSize:'20px',fontWeight:'600',margin:'40px 20px'}}>
                최근 인기있는 메뉴는?
            </span>
            <ApexCharts
                type='pie'
                series={[100,30,40]}
                options={{
                    chart:{
                        
                        type:'pie',
                        toolbar:{show:false}
                    },
                    labels:['백반 정식','해물 순두부찌개','촌돼지 부대찌개']
                }}
                />

        </SecondChart>
        </차트2개>

        {Input? 
        (<>
            <InputW>
                <Title>
                    <span style={{fontSize:'22px',fontWeight:'600',textDecoration: 'underline', textDecorationColor:'#1473E6',textDecorationThickness:'2px'}}>잔반량 등록</span>
                    <AiFillCloseCircle style={{fontSize:'22px',marginRight:'-15px',marginTop:'-40px'}} onClick={()=>setInput(false)}/>
                </Title>

                <div>
                    <Items>
                        <span>날짜선택</span>
                        <div>                       
                        <DatePicker selected={startDate} onChange={date => setStartDate(date)}/>
                        </div>
                    </Items>
                    <Items>
                        <span>총 잔반량</span>
                        <input type='number' placeholder='kg수를 적으세요.'/>
                    </Items>
                </div>

                <button onClick={onsubmit}>등록하기</button>
            </InputW>
        </>) 
        : null}        

        <LastChart>
            <span>이번주 이용자 수</span>          
        <ApexCharts
                type="line"
                series={ [
                    {
                        data:[200,130,110,150,160,120,140]
                    }
                ]}
                options={{
                    chart:{
                        toolbar:{show:false}
                    },
                    stroke:{
                        curve:'smooth',
                        width:2.5
                    },
                    xaxis:{
                        type:'category',
                        categories:['월','화','수','목','금','토','일']
                    },
                    fill:{
                        colors:'green',
                        opacity:0.9
                    }
                }}
            />

        </LastChart>
        

        </Wrapper>
    )
}

export default Dashboard;

