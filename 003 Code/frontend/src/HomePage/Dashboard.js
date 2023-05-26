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
margin-top:5vh;
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
        font-weight:500;
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
    height:12vh;
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
    height:11vh;
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
    margin:20px 20px;
    background-color:#C8D5EF;
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
    const [prevLeftover, setPrevLeftover] = useState([]);
    const [nextLeftover, setNextLeftover] = useState([]);
    const [ShowInput, SetShowInput] = useState(false);
    const [totalPop, setTotalPop] = useState(0);
    const [todaypop, setTodaypop] = useState(0);
    const [Weekpop, setWeekpop] = useState([]);
    const [leftover, setLeftover] = useState('');
    const [goodmenu, setGoodmenu] = useState([]);
    const startDate = new Date();
    
    // 금일, 누적 이용자 수 
    useEffect(() => {
        axios.get('/api/manager/state/all')
        .then(res => setTotalPop(res.data))

        axios.get('/api/manager/state/today')
        .then(res => setTodaypop(res.data))

        axios.get('/api/manager/state/last-week/user')
        .then(res => setWeekpop(res.data))

        axios.get('/api/manager/leftover/1')
        .then(res => setPrevLeftover(res.data))

        axios.get(`/api/manager/leftover/0`)
        .then(res => setNextLeftover(res.data))
        
        axios.get(`/api/manager/state/menu`)
        .then(res => setGoodmenu(res.data))
    },[])

    // 지난주 대비 음식물 쓰레기 감소량
    let prevsum = 0;
    prevLeftover.forEach(n => {
        prevsum += n.leftover
    })
    
    let nextsum = 0;
    nextLeftover.forEach(n => {
        nextsum += n.leftover
    })

    // 오늘의 잔반량 등록 인풋
    const onClick = () => {
        SetShowInput(true);
    };
    
    const onsubmit = (e) => {
        e.preventDefault();
        let body={leftover}
        axios.post('/api/manager/leftover',body)
        .then(res => console.log(res))
        .catch(err=>console.log(err))
        SetShowInput(false);
    };
    
    return(<>
        <Wrapper>
            <Navtop pages={"홈"}/>

        <Statistis>
            <금일>
                <span>{format(startDate,'yy')}-{format(startDate,'MM')}-{format(startDate,'dd')}</span>
                <span>금일 이용자 수</span>
            </금일>

            <Total>
                <span>{todaypop}</span>
                <span>명</span>
            </Total>
            
            <div style={{marginLeft:'-20px',borderRight:'thin solid #DDDDDD',height:'15vh'}}></div>
            
            <누적이용자>
                <span>{totalPop}</span>
                <span>누적 이용자</span>
            </누적이용자>

            <div style={{marginLeft:'30px',borderRight:'thin solid #DDDDDD',height:'15vh'}}></div>

            <감소량>
                <span>{prevsum-nextsum}Kg</span>
                <span>지난주 대비 음식물 쓰레기 감소량</span>
            </감소량>
        </Statistis>
        
        <차트2개>
        <FirstChart>
            <div>
                <span>지난 2주간 잔반량 비교</span>
                <Button className='custom-secondary-button' onClick={onClick} variant="secondary">등록하러가기</Button>{' '}
            </div>
            <ApexCharts
                type="line"
                series={ [
                    {
                        name:'지난주',
                        data:prevLeftover.map(prev => prev.leftover)
                    },
                    {
                        name:'이번주',
                        data:nextLeftover.map(next => next.leftover)
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
                        categories:['월','화','수','목','금']
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
            <span style={{fontSize:'20px',fontWeight:'600',margin:'20px 20px',marginBottom:'20px'}}>
                최근 인기있는 메뉴는?
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

        <LastChart>
            <span>지난주 이용자 수</span>          
        <ApexCharts
                type="line"
                series={ [
                    {
                        name:'이용자 수',
                        data:Weekpop?.map(week => week.count)
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
                        // categories:Weekpop.map(week => week.date)
                        categories:['월','화','수','목','금']
                    },
                    fill:{
                        colors:'green',
                        opacity:0.9
                    },
                    
                }}
            />

        </LastChart>
        
        
        </Wrapper>
        {ShowInput ? <>
            <InputW>
                <Title>
                    <span style={{fontSize:'22px',fontWeight:'600', marginLeft:'20px'}}>오늘의 잔반량 등록</span>
                    <AiFillCloseCircle style={{fontSize:'22px',marginRight:'20px'}} onClick={()=>SetShowInput(false)}/>
                </Title>

                <div>
                    <Items>
                        <span>오늘 날짜</span>
                        <div>                       
                        {format(startDate,'yy')}-{format(startDate,'MM')}-{format(startDate,'dd')}
                        {/* <DatePicker selected={startDate} onChange={date => setStartDate(date)}/> */}
                        </div>
                    </Items>
                    <Items>
                        <span>총 잔반량</span>
                        <input 
                        value={leftover}
                        onChange={(e) => setLeftover(e.target.value)}
                        type='number' 
                        placeholder='kg수를 적으세요.'/>
                    </Items>
                </div>

                <Button variant="primary" onClick={onsubmit}>등록하기</Button>
            </InputW>
            <Overlay/>
        </>:null}
        </>
    )
}

export default Dashboard;

