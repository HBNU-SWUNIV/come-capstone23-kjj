import {addMonths, subMonths, format, startOfMonth, endOfMonth, startOfWeek, endOfWeek, addDays} from 'date-fns';
import { useEffect, useState } from 'react';
import styled from 'styled-components';
import {AiOutlineLeft,AiOutlineRight} from "react-icons/ai";
import { useMatch, useNavigate } from 'react-router-dom';
import { AiFillCloseCircle } from "react-icons/ai";
import "react-datepicker/dist/react-datepicker.css";
import shortid from 'shortid';
import axios from 'axios';

const ArrowCSS = {color:'#969696', fontSize:'20px'}

const Wrapper = styled.div`
    display:flex;
    width:63vw;
    margin-left:30px;
    margin-top:60px;
    border:1px solid #5B5B5B;
    flex-direction:column;
    align-items:center;
`;

const HeaderW = styled.div`
    width:60vw;
    height:12vh;
    display:flex;
    justify-content:space-between;
    align-items:center;
    span{
        color:#383838;
        font-size:25px;
        font-weight:600;
    }
`;

const DaysDiv = styled.div`
    width:9vw;
    height:7vh;
    display:flex;
    justify-content:center;
    align-items:center;
    border:0.1px solid #5B5B5B;
    border-right-style:none;
`;

const DaysWrapper = styled.div`
    display:flex;
    justify-content:space-between;
    width:63vw;
`;

const DivDay = styled.div`
    width:9vw;
    height:13vh;
    display:flex;
    justify-content:space-between;
    border:0.1px solid #5B5B5B;
    border-right-style:none;
    span:first-child{
        font-size:20px;
        font-weight:600;
        margin:15px 15px;
    }
    span:last-child{
        font-size:13px;
    }
`;

const DivWeek = styled.div`
    display:flex;
    width:63vw;
    height:13vh;
`;

const DivWrapper = styled.div`
    width:63vw;
    flex-direction:column;

`;

const WriteWrapper = styled.div`
    z-index:1;
    width:34vw;
    height:40vh;
    position:absolute;
    margin:0 auto;
    margin-top:-220px;
    position:fixed;
    display:flex; 
    flex-direction:column;
    align-items:center;
    background-color:white;
    border:1px solid white;
    border-radius:15px;
    button{
        width:9vw;
        height:5vh;
        border:1px solid #1473E6;
        background-color:#1473E6;
        border-radius:15px;
        font-size:18px;
        color:white;
    }
`;

const WriteTitle = styled.div`
    background-color:#d9d9d9;
    width:34.1vw;
    border-top-right-radius:15px;
    border-top-left-radius:15px;
    margin-top:-1px;
    margin-bottom:40px;
    height:10vh;
    display:flex;
    justify-content:space-between;
    align-items:center;
    span:nth-child(2){
        font-size:20px;
        font-weight:600;
        margin-left:-85px;
        margin-bottom:25px;
    }
    span:first-child{
        font-weight:600;
        font-size:22px;
        margin:20px 20px;
        text-decoration:underline;
    }
`;

const WriteButton = styled.div`
    display:flex;
    align-items:center;
    justify-content:space-evenly;
    width:24vw;
    margin-top:10px;
`;

function Calander2(){
    const navigate = useNavigate();
    const [currentMonth, setCurrentMonth] = useState(new Date());
    const [offday, setOffday] = useState([]);
    const monthStart = startOfMonth(currentMonth),monthEnd = endOfMonth(monthStart);
    const startDate = startOfWeek(monthStart),endDate = endOfWeek(monthEnd);
    let day = startDate,dayss = [],line = [],formattedDate = '';
    const days = [],date = ['일','월','화','수','목','금','토'];
    const DayPathMatch = useMatch('/setting/:id'); 
    
    const onDay = (id) => {
        navigate(`/setting/${id}`);
    }

    useEffect(() => {
        axios.get(`api/manager/store/get/off/${format(currentMonth,'yyyy')}/${format(currentMonth,'MM')}`)
        .then(res => setOffday(res.data))
    },[currentMonth])
    console.log(offday)
    
    for (let i=0; i<7; i++){
        days.push(
            <DaysDiv key={shortid.generate()}>
                {date[i]}
            </DaysDiv>
        )
    }

    while(day <= endDate){
        for(let i=0; i<7; i++){
            formattedDate = format(day,'d').padStart(2,'0').toString();
            const id = format(day,'yyyyMMdd').toString();
            
            if(format(monthStart,'M') != format(day,'M')){
                dayss.push(
                    // 다른달일 경우 회색으로 표시
                    <DivDay style={{backgroundColor:'#383838',opacity:'0.5'}} key={shortid.generate()}>
                        <span style={{
                            fontSize:'20px',fontWeight:600,margin:'15px 15px'}}>
                            {formattedDate}
                        </span>
                    </DivDay>
                )}
            else{
                dayss.push(
                    <DivDay onClick={() => onDay(id)} key={shortid.generate()}>
                        <span style={{color:offday.filter(od => od.date == id)[0]?.off == true ? 'red' : 'black'}}>
                            {formattedDate}
                        </span>
                    </DivDay>
                )}
            day = addDays(day,1);
        }
        line.push(
            <DivWeek key={shortid.generate()}>
                {dayss}
            </DivWeek>
        )
        dayss=[];
    }

    const prevMonth = () => {
        setCurrentMonth(subMonths(currentMonth, 1));
    }

    const nextMonth = () => {
        setCurrentMonth(addMonths(currentMonth,1));
    }

    const onOffday = (date,year,month,day) => {
        let body = {date,off:true}
        axios.post(`/api/manager/store/set/off/${year}/${month}/${day}`,body)
        .then(res => console.log(res))
        .catch(err => console.log(err))

        navigate('/setting')
    }


    return(
        <Wrapper>
            <HeaderW>
                <AiOutlineLeft style={{...ArrowCSS, marginLeft:'20px'}} onClick={prevMonth}/>
                <span>{format(currentMonth,'yyyy')}. {format(currentMonth,'MM')}</span>
                <AiOutlineRight style={{...ArrowCSS, marginRight:'20px'}} onClick={nextMonth}/>
            </HeaderW>

            <DaysWrapper>
                {days}
            </DaysWrapper>

            <DivWrapper>
                {line}
            </DivWrapper>

            {DayPathMatch ? 
            <WriteWrapper>
                <WriteTitle>
                    <span>휴일 등록</span>
                    <AiFillCloseCircle 
                    onClick={() => navigate('/setting')}
                    style={{fontSize:'30px',marginRight:'10px',marginBottom:'10px'}}/>
                </WriteTitle>

                <h3>{DayPathMatch.params.id.slice(0,4)}-{DayPathMatch.params.id.slice(4,6)}-{DayPathMatch.params.id.slice(6,8)}일을 휴일로 지정하시겠습니까?</h3>
                
                <WriteButton>
                    <button onClick={() => onOffday(DayPathMatch.params.id,DayPathMatch.params.id.slice(0,4),DayPathMatch.params.id.slice(4,6),DayPathMatch.params.id.slice(6,8))}>
                        휴일로 지정
                    </button>
                    <button onClick={() => navigate('/setting')}>
                        영업일로 지정
                    </button>
                </WriteButton>

            </WriteWrapper>:null}
        </Wrapper>
    )// 
}

export default Calander2;