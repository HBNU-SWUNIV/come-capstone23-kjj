import {addMonths, subMonths, format, startOfMonth, endOfMonth, startOfWeek, endOfWeek, addDays} from 'date-fns';
import { useEffect, useState } from 'react';
import styled from 'styled-components';
import {AiOutlineLeft,AiOutlineRight} from "react-icons/ai";
import { Navigate, useMatch, useNavigate } from 'react-router-dom';
import { AiFillCloseCircle } from "react-icons/ai";

const ArrowCSS = {color:'#969696', fontSize:'20px'}

const Wrapper = styled.div`
    display:flex;
    width:63vw;
    margin-left:30px;
    margin-top:60px;
    border:1px solid #5B5B5B;
    flex-direction:column;
    align-items:center;
`

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
        white-space:pre-wrap;
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

const WriteWrapper = styled.form`
    width:40vw;
    height:40vh;
    position:absolute;
    left:0;
    right:0;
    margin:0 auto;
    position:fixed;
    margin-top:10vw;
    display:flex; 
    flex-direction:column;
    align-items:center;
    background-color:white;
    border:1px solid #1473E6;
    button{
        width:15vw;
        height:5vh;
        border:1px solid #1473E6;
        background-color:#1473E6;
        border-radius:15px;
        font-size:18px;
        color:white;
    }
`;

const WriteTitle = styled.div`
    width:38vw;
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
        margin:20px 20px;
        text-decoration:underline;
    }
`;

const WriteInfo = styled.div`
    width:35vw;
    height:20vh;
    display:flex;
    justify-content:space-between;
    span{
        font-weight:600;
    }
    textarea{
        width:30vw;
        height:18vh;
        border:1px solid gray;
        border-radius:10px;
        resize:none;
    }
`;


function Calander(){
    const navigate = useNavigate();
    const [currentMonth, setCurrentMonth] = useState(new Date());
    const [Backbaninfo, setBackbaninfo] = useState('');
    const [savedBackbaninfo, setSavedBackbaninfo] = useState([]);

    const onBackban = event => {
        event.preventDefault();
        setBackbaninfo(event.target.value);
    }

    // const onBackbanTap구현 = (e) => {
    //     // tap의 keycode는 9다.
    //     if (e.keyCode === 13) {
    //         e.preventDefault();
    //         let val = e.target.value;
    //         let start = e.target.selectionStart;
    //         let end = e.target.selectionEnd;
    //         e.target.value = val.substring(0, start) + "\t" + val.substring(end);
    //         e.target.selectionStart = e.target.selectionEnd = start + 1;
    //         onBackban(e);
    //         return false; 
    //       }
    // }

    const onSave = (id) => {
        setSavedBackbaninfo(prev => [
            ...prev, {id:id, text:Backbaninfo}
        ]);
        navigate('/backban');
        setBackbaninfo('');
    }

    console.log(savedBackbaninfo.map(a => a.id));

    const days = [];
    const date = ['일','월','화','수','목','금','토'];
    for (let i=0; i<7; i++){
        days.push(
            <DaysDiv>
                {date[i]}
            </DaysDiv>
        )
    }

    const monthStart = startOfMonth(currentMonth);
    const monthEnd = endOfMonth(monthStart);
    const startDate = startOfWeek(monthStart);
    const endDate = endOfWeek(monthEnd);
    let day = startDate;
    let dayss = [];
    let line = [];
    let formattedDate = '';
    const DayPathMatch = useMatch('/backban/:id'); 

    const onDay = (id) => {
        navigate(`/backban/${id}`);
    }

    while(day <= endDate){
        for(let i=0; i<7; i++){
            formattedDate = format(day,'d').padStart(2,'0').toString();
            const id = format(day,'yyyyMMdd').toString();
            if(format(monthStart,'M') != format(day,'M')){
                dayss.push(
                    <DivDay style={{backgroundColor:'#383838',opacity:'0.5'}} key={day+''}>
                        <span style={{
                            fontSize:'20px',fontWeight:600,margin:'15px 15px'
                        }}>
                            {formattedDate}
                        </span>
                    </DivDay>
                )
            }
            else{
                dayss.push(
                    <DivDay onClick={() => onDay(id)} key={day+'1'}>
                        <span>
                            {formattedDate}
                        </span>
                        <span>
                            {savedBackbaninfo.map(savedbackban => savedbackban.id === id ? savedbackban.text : null)}
                        </span>
                    </DivDay>
                )
            }
            day = addDays(day,1);
        }
        line.push(
            <DivWeek>
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
                    <span>{DayPathMatch.params.id.slice(0,4)}-{DayPathMatch.params.id.slice(4,6)}-{DayPathMatch.params.id.slice(6,8)}</span>
                    <span>백반 식단 관리</span>
                    <AiFillCloseCircle 
                    onClick={() => navigate('/backban')}
                    style={{fontSize:'30px',marginRight:'10px',marginBottom:'10px'}}/>
                </WriteTitle>
                <WriteInfo>
                    <span>Menu</span>
                    <textarea
                    placeholder='여기에 백반메뉴를 입력하세요.'
                    value={Backbaninfo}
                    onChange={onBackban}
                    // onKeyDown={onBackbanTap구현}
                    />
                </WriteInfo>
                <button onClick={() => onSave(DayPathMatch.params.id)}>
                    저장
                </button>
            </WriteWrapper>:null}



        </Wrapper>
    )
}

export default Calander;