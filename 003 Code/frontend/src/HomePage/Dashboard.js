import styled from 'styled-components';
import ApexCharts from "react-apexcharts";
import { useState,useEffect } from 'react';
import { AiFillCloseCircle } from "react-icons/ai";
import "react-datepicker/dist/react-datepicker.css";
import Navtop from '../Components/Navtop';
import { format } from 'date-fns';
import axios from 'axios';
import Overlay from '../Components/Overlay';
import Button from 'react-bootstrap/Button';



const Wrapper = styled.div`
display:flex;
flex-direction:column;
width:85vw;
height:100vh;
font-family:'DeliveryFont';
`;

const Statistis = styled.div`
    width:70vw;
    height:15vh;
    display:flex;
    justify-content:flex-start;
    align-items:center;
    margin-top:2vh;
    margin-left:6vw;
`;

const 금일 = styled.div`
    display:flex;
    flex-direction:column;
    justify-content:center;
    align-items:center;
    width:14vw;
    height:14vh;
    background-color:#C8D5EF;
    margin-left:30px;
    border-radius:5px;
    span{
        font-weight:500;
        font-size:23px;
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
        font-weight:400;
        margin-bottom:10px;
    }
    span:first-child{
        font-size:65px;
        margin-right:5px;
        // text-decoration:underline;
        // text-decoration-thickness:3px;
        font-weight:400;
        margin-bottom:-0.5vh;
    }
`;

const 누적이용자 = styled.div`
    display:flex;
    flex-direction:column;
    justify-content:center;
    align-items:flex-start;
    width:13vw;
    height:13vh;
    background-color:#C8D5EF;
    margin-left:30px;
    span{
        margin-left:10px;
        font-size:15px;
    }
    span:first-child{
        font-size:30px;
        font-weight:400;
        margin-bottom:5px;
    }
`;

const 감소량 = styled.div`
    display:flex;
    flex-direction:column;
    justify-content:center;
    align-items:flex-start;
    width:16vw;
    height:13vh;
    background-color:#C8D5EF;
    border-radius:5px;
    margin-left:30px;
    span{
        margin-left:10px;
    }
    span:first-child{
        font-size:30px;
        font-weight:400;
        margin-bottom:7px;
    }
    span:last-child{
        font-size:15px;
    }
`;

const 차트2개 = styled.div`
    margin-top:5vh;
    margin-left:15vw;
    width:45vw;
    height:60vh;
    position:relative;
    display:flex;
    flex-direction:column;
    span{
        margin-top:5vh;
        font-size:20px;
        font-weight:600;
        margin-left:20px;
        font-family:'DeliveryFont';
    }
`

const FirstChart = styled.div`
    width:35vw;
    height:50vw;
    margin:20px 10px;
    span:first-child{
        font-size:20px;
        font-weight:500;
        margin-left:20px;
        font-family:'DeliveryFont';
    }
    button{
        border-radius:5px;
        border:1px solid #C8D5EF;
        font-family:'DeliveryFont';
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
    height:60vh;
    margin:30px 20px;
    span{
        font-family:'DeliveryFont';
    }
`;

const LastChart = styled.div`
    width:50vw;
    margin-top:1vw;
    span{
        margin-left:20px;
        font-size:18px;
        font-weight:600;
        font-family:'DeliveryFont';
    }
`;

const InputW = styled.form`
    width:460px;
    font-family:'DeliveryFont';
    z-index:1;
    height:300px;
    border:2px solid white;
    border-radius: 15px;
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
        display:flex;
        justify-content:center;
        align-items:center;
        border-radius:20px;
        border:1px solid #1473E6;
        color:white;
    }
`;

const Title = styled.div`
    display:flex;
    width:460px;
    border-top-left-radius:15px;
    border-top-right-radius:15px;
    height:70px;
    background-color:#d9d9d9;
    justify-content:space-between;
    align-items:center;
    margin-top:-35px;  

`
const Items = styled.div`
    display:flex;
    width:360px;
    height:50px;
    justify-content:space-between;
    align-items:center;
    span{
        font-weight:500;
    }
`


    
function Dashboard(){
    const [totalPop, setTotalPop] = useState(0);
    const [todaypop, setTodaypop] = useState(0);
    const [goodmenu, setGoodmenu] = useState([]);
    const [predictitems, setPredictItems] = useState([]);
    const [predictUser, setPredictUser] = useState(0);
    const [predictMenus, setPredictMenus] = useState([]);


    const startDate = new Date();
    
    useEffect(() => {
        axios.get('/api/manager/state/all')
        .then(res => setTotalPop(res.data))

        axios.get('/api/manager/state/today')
        .then(res => setTodaypop(res.data))
        
        axios.get(`/api/manager/state/menu`)
        .then(res => setGoodmenu(res.data))

        axios.get(`/api/manager/state/predict/food`)
        .then(res => setPredictItems(res.data))

        axios.get('/api/manager/state/predict/user')
        .then(res => setPredictUser(res.data))

        axios.get('/api/manager/state/predict/menu')
        .then(res=> setPredictMenus(res.data))
    },[])

    // Object 형식을 배열로 변환하는 이유 => apexchart에 반영하기 위해
    const predictItemsArray = Object.entries(predictitems);
    const predictMenusArray = Object.entries(predictMenus);
    
    return(<>
        <Wrapper>
            <Navtop pages={"홈"}/>

        <Statistis>
            <금일>
                {/* <span>오늘의 날짜 {format(startDate,'yy')}-{format(startDate,'MM')}-{format(startDate,'dd')}</span> */}
                <span>내일 예약자 수</span>
            </금일>

            <Total>
                {/* <span>{predict_user.length > 0 ? predict_user[0][1] : 'Loading...'}</span> */}
                <span>{predictUser ? predictUser : 'Loading...'}</span>
                <span>명</span>
            </Total>
            
            <div style={{marginLeft:'-20px',borderRight:'thin solid #DDDDDD',height:'15vh'}}></div>
            
            <누적이용자>
                <span>{totalPop}명</span>
                <span>누적 이용자 수</span>
            </누적이용자>

            <div style={{marginLeft:'30px',borderRight:'thin solid #DDDDDD',height:'15vh'}}></div>

            <감소량>
                <span>{todaypop}명</span>
                <span>오늘 이용자 수 </span>
            </감소량>
        </Statistis>
        
        <차트2개>
            <span>내일에는 다음과 같은 식재료 예측이 있습니다.</span>
            <ApexCharts
                type="bar"
                series={ [
                    {
                        name:'',
                        data:predictItemsArray.map(items => items[1])
                    }
                ]}
                options={{
                    chart:{
                        toolbar:{show:false}
                    },
                    stroke:{
                        curve:'smooth',
                        width:1
                    },
                    xaxis:{
                        type:'category',
                        categories:predictItemsArray.map(items => items[0])
                    },
                    fill:{
                        colors:'black',
                        opacity:1
                    }
                }}
            />
            
            <span>내일의 일정에는 다음과 같은 메뉴 예약이 있습니다.</span>
            <ApexCharts
                type="bar"
                series={ [
                    {
                        name:'',
                        data:predictMenusArray.map(items => items[1])
                    }
                ]}
                options={{
                    chart:{
                        toolbar:{show:false}
                    },
                    plotOptions:{
                        bar:{
                            borderRadius:4,
                            horizontal:true,
                        }
                    },
                    stroke:{
                        curve:'smooth',
                        width:1
                    },
                    xaxis:{
                        type:'category',
                        categories:predictMenusArray.map(items => items[0])
                    },
                    fill:{
                        colors:'green',
                        opacity:1
                    }
                }}
            />


            <SecondChart>
            <span style={{fontSize:'20px',fontWeight:'600',margin:'20px 20px',marginBottom:'20px'}}>
            요즘 가장 인기 있는 메뉴는 다음과 같습니다.
            </span>   
            <ApexCharts
                type='pie'
                series={goodmenu.map(menu => menu.count)}
                options={{
                    chart:{
                        type:'pie',
                        toolbar:{show:false}
                    },
                    labels:goodmenu.map(menu => menu.name)
                }}
                />

            </SecondChart>
                
        </차트2개>
        
        </Wrapper>
        </>
    )
}

export default Dashboard;

