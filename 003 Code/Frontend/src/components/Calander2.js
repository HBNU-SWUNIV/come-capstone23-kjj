import {addMonths, subMonths, format, startOfMonth, endOfMonth, startOfWeek, endOfWeek, addDays} from 'date-fns';
import { useEffect, useState } from 'react';
import styled from 'styled-components';
import {AiOutlineLeft,AiOutlineRight} from "react-icons/ai";
import {  useMatch, useNavigate } from 'react-router-dom';
import shortid from 'shortid';
import axios from 'axios';
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import TextField from '@mui/material/TextField';
import Snackbar from '@mui/material/Snackbar';
import Alert from '@mui/material/Alert';

const ArrowCSS = {color:'#969696', fontSize:'20px'}

const Wrapper = styled.div`
    display:flex;
    width:63vw;
    border:1px solid #5B5B5B;
    margin-left:-1vw;
    margin-top:-4vh;
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
        font-size:13px;
        font-weight:600;
        margin:5px 5px;
    }
    span:last-child{
        margin-top:0.2vh;
        margin-right:0.2vw;
        font-size:12px;
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
    border-radius:15px;
    height:40vh;
    z-index:1;
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
    border:1px solid white;
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
    width:40vw;
    margin-top:-1px;
    margin-bottom:10px;
    height:10vh;
    display:flex;
    background-color:#d9d9d9;
    justify-content:space-between;
    border-top-left-radius:15px;
    border-top-right-radius:15px;
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

function Calander2(){
    const navigate = useNavigate();
    const [offday, setOffday] = useState([]);
    const [currentMonth, setCurrentMonth] = useState(new Date());
    const [dayId, setDayId] = useState(0);
    const [open, setOpen] = useState(false);
    const [success, setSuccess] = useState(false);
    const handleSuccessOpen = () => {
    setSuccess(true)
    }
    const handleSuccessClose = () => {
    setSuccess(false)
    }

    const handleClickOpen = (id) => {
        setOpen(true);
        setDayId(id);
      };
    
      const handleClose = () => {
        setOpen(false);
      };

      useEffect(() => {
        axios.get(`api/manager/store/off/${format(currentMonth,'yyyy')}/${format(currentMonth,'MM')}`)
        .then(res => setOffday(res.data))
    },[currentMonth])

    const onOffday = (year,month,day) => {
        let body = {off:true}
        axios.post(`/api/manager/store/off/${year}/${month}/${day}`,body)
        .then(res => {
            res.status == 200 &&
            axios.get(`api/manager/store/off/${format(currentMonth,'yyyy')}/${format(currentMonth,'MM')}`)
            .then(re => setOffday(re.data))
        })
        handleClose();
        handleSuccessOpen();
    }

    const onOnday = (year,month,day) => {
        let body = {off:false}
        axios.post(`/api/manager/store/off/${year}/${month}/${day}`,body)
        .then(res => {
            res.status == 200 &&
            axios.get(`api/manager/store/off/${format(currentMonth,'yyyy')}/${format(currentMonth,'MM')}`)
            .then(re => setOffday(re.data))
        })
        handleClose();
        handleSuccessOpen();
    }


    const days = [],date = ['일','월','화','수','목','금','토']; 
    for (let i=0; i<7; i++){
        days.push(
            <DaysDiv key={shortid.generate()}>
                {date[i]}
            </DaysDiv>
        )
    }

    const monthStart = startOfMonth(currentMonth),monthEnd = endOfMonth(monthStart);
    const startDate = startOfWeek(monthStart),endDate = endOfWeek(monthEnd);
    let day = startDate,formattedDate = '',dayss = [],line = [];
    const DayPathMatch = useMatch('/backban/:id'); 

    while(day <= endDate){
        for(let i=0; i<7; i++){
            formattedDate = format(day,'d').padStart(2,'0').toString();
            const id = format(day,'yyyyMMdd').toString();
            if(format(monthStart,'M') != format(day,'M')){
                dayss.push(
                    <DivDay style={{backgroundColor:'#383838',opacity:'0.5'}} 
                            key={shortid.generate()}>
                        <span style={{
                            fontSize:'13px',fontWeight:600,margin:'5px 5px'
                        }}>
                            {formattedDate}
                        </span>
                    </DivDay>
                )
            }
            else{
                dayss.push(
                    <DivDay 
                    onClick={() => handleClickOpen(id)} 
                    key={shortid.generate()}>
                        <span style={{color:offday.filter(od => od.date == id)[0]?.off == true ? 'red' : 'black'}}>
                            {formattedDate}
                        </span>
                    </DivDay>
                )
            }
            day = addDays(day,1);
        }
        line.push(
            <DivWeek key={shortid.generate()}>
                {dayss}
            </DivWeek>
        )
        dayss=[];
    }

    // 월 넘기기
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


        <Dialog open={open} onClose={handleClose}>
        <DialogTitle>{dayId}을 휴일로 지정하시겠습니까?</DialogTitle>
        <DialogContent>
          <DialogContentText sx={{marginBottom:'1vh'}}>
            휴일을 설정해주세요.
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => onOffday(dayId.slice(0,4), dayId.slice(4,6), dayId.slice(6,8))}>휴일 등록</Button>
          <Button onClick={() => onOnday(dayId.slice(0,4), dayId.slice(4,6), dayId.slice(6,8))}>영업일 등록</Button>
          <Button color='error' onClick={handleClose}>닫기</Button>
        </DialogActions>
      </Dialog>

      <Snackbar open={success} autoHideDuration={6000} onClose={handleSuccessClose}>
        <Alert onClose={handleSuccessClose} severity="success" sx={{ width: '100%' }}>
          This is a success message!
        </Alert>
      </Snackbar>
            
        </Wrapper>
    )
}

export default Calander2;