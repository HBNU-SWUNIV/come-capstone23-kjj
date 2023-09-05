import {
  addMonths,
  subMonths,
  format,
  startOfMonth,
  endOfMonth,
  startOfWeek,
  endOfWeek,
  addDays,
} from 'date-fns';
import { useEffect, useState } from 'react';
import styled from 'styled-components';
import { AiOutlineLeft, AiOutlineRight } from 'react-icons/ai';
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
import { ConfigWithToken, ManagerBaseApi } from '../../auth/authConfig';

const ArrowCSS = {
  color: 'black',
  fontSize: '1.2rem',
  margin: '0 1.25rem',
  marginBottom: '3px',
  cursor: 'pointer',
};

const Wrapper = styled.div`
  @media screen and (max-width: 1200px) {
    width: 95%;
  }
  width: 65%;
  display: flex;
  flex-direction: column;
  align-items: center;
`;
const HeaderW = styled.div`
  width: 100%;
  display: flex;
  position: relative;
  justify-content: center;
  align-items: center;
  margin-bottom: 3%;
  span {
    color: #383838;
    font-size: 1.563rem;
    font-weight: 600;
  }
`;
const DaysDiv = styled.div`
  width: 15%;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 15px;
  font-weight: 500;
`;
const DaysWrapper = styled.div`
  display: flex;
  justify-content: space-around;
  align-items: center;
  width: 100%;
`;
const DivDay = styled.div`
  &:hover {
    cursor: pointer;
  }
  background-color: #f5f5f5;
  overflow: scroll;
  scrollbar-width: none;
  -ms-overflow-style: none;
  &::-webkit-scrollbar {
    display: none;
  }

  height: 12vh;
  width: 90%;
  border-radius: 20px;

  display: flex;
  justify-content: space-between;

  box-sizing: border-box;
  margin-bottom: 10%;
  padding: 10px;

  span:first-child {
    font-size: 0.188rem;
    font-weight: 600;
  }
  span:last-child {
    font-size: 12px;
    font-weight: 600;
    font-family: NotoSans;
    white-space: pre-wrap;
    text-align: right;
  }
`;
const DivWeek = styled.div`
  width: 100%;
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  place-items: center;
`;
const DivWrapper = styled.div`
  width: 100%;
`;

function Calander() {
  const config = ConfigWithToken();

  const [currentMonth, setCurrentMonth] = useState(new Date());
  const [Backbaninfo, setBackbaninfo] = useState('');
  const [savedBackbaninfo, setSavedBackbaninfo] = useState([]);
  const [dayId, setDayId] = useState(0);
  const [open, setOpen] = useState(false);
  const [success, setSuccess] = useState(false);
  const handleSuccessOpen = () => {
    setSuccess(true);
  };
  const handleSuccessClose = () => {
    setSuccess(false);
  };
  const handleClickOpen = (id) => {
    setOpen(true);
    setDayId(id);
  };
  const handleClose = () => {
    setOpen(false);
  };

  useEffect(() => {
    axios
      .get(
        `/api/user/planner/${format(currentMonth, 'yyyy')}/${format(currentMonth, 'MM')}`,
        config
      )
      .then((res) => setSavedBackbaninfo(res.data))
      .catch((err) => {});
  }, [currentMonth]);

  const onBackban = (event) => {
    event.preventDefault();
    setBackbaninfo(event.target.value);
  };

  const onSave = (year, month, day) => {
    let body = { menus: Backbaninfo };
    axios
      .post(`${ManagerBaseApi}/planner/${year}/${month}/${day}`, body, config)
      .then(() => {
        axios
          .get(
            `/api/user/planner/${format(currentMonth, 'yyyy')}/${format(
              currentMonth,
              'MM'
            )}`,
            config
          )
          .then((res) => setSavedBackbaninfo(res.data));
      });
    setBackbaninfo('');
    handleClose();
    handleSuccessOpen();
  };

  const days = [],
    date = ['일', '월', '화', '수', '목', '금', '토'];
  for (let i = 0; i < 7; i++) {
    days.push(<DaysDiv key={shortid.generate()}>{date[i]}</DaysDiv>);
  }

  const monthStart = startOfMonth(currentMonth),
    monthEnd = endOfMonth(monthStart);
  const startDate = startOfWeek(monthStart),
    endDate = endOfWeek(monthEnd);
  let day = startDate,
    formattedDate = '',
    dayss = [],
    line = [];

  while (day <= endDate) {
    for (let i = 0; i < 7; i++) {
      formattedDate = format(day, 'd').padStart(2, '0').toString();
      const id = format(day, 'yyyyMMdd').toString();
      if (format(monthStart, 'M') != format(day, 'M')) {
        dayss.push(
          <DivDay
            style={{
              opacity: '0',
            }}
            key={shortid.generate()}
          />
        );
      } else {
        dayss.push(
          <DivDay onClick={() => handleClickOpen(id)} key={shortid.generate()}>
            <span>{formattedDate}</span>
            <span>
              {savedBackbaninfo.map((savedbackban) =>
                savedbackban.date === id ? savedbackban.menus : null
              )}
            </span>
          </DivDay>
        );
      }
      day = addDays(day, 1);
    }
    line.push(<DivWeek key={shortid.generate()}>{dayss}</DivWeek>);
    dayss = [];
  }

  // 월 넘기기
  const prevMonth = () => {
    setCurrentMonth(subMonths(currentMonth, 1));
  };
  const nextMonth = () => {
    setCurrentMonth(addMonths(currentMonth, 1));
  };

  return (
    <Wrapper>
      <HeaderW>
        <AiOutlineLeft style={{ ...ArrowCSS }} onClick={prevMonth} />
        <span style={{ fontFamily: 'NotoSans', fontWeight: '600', fontSize: '20px' }}>
          {format(currentMonth, 'yy')}년 {format(currentMonth, 'MM')}월
        </span>
        <AiOutlineRight style={{ ...ArrowCSS }} onClick={nextMonth} />
      </HeaderW>
      <DaysWrapper>{days}</DaysWrapper>
      <DivWrapper>{line}</DivWrapper>

      <Dialog open={open} onClose={handleClose}>
        <DialogTitle sx={{ margin: '0 auto' }}>
          Day {dayId && dayId?.substr(4, 2)}-{dayId && dayId?.substr(6, 2)}
        </DialogTitle>
        <DialogContent>
          <DialogContentText
            align="center"
            sx={{ marginBottom: '20px', color: '#FF385C', fontSize: '14px' }}
          >
            식단 내용을 지우고 싶으시면 빈 내용으로 등록해주시면 됩니다
          </DialogContentText>
          <div>
            <TextField
              id="outlined-multiline-static"
              label="식단 예시"
              multiline
              disabled
              rows={5}
              defaultValue={`백미밥\n소고기미역국\n닭갈비\n잡채\n상추쌈&쌈장`}
            />
            <TextField
              id="outlined-multiline-static"
              label="식단"
              multiline
              value={Backbaninfo}
              onChange={onBackban}
              rows={5}
              placeholder="여기에 입력해주세요."
            />
          </div>
        </DialogContent>
        <DialogActions>
          <Button
            onClick={() =>
              onSave(dayId.slice(0, 4), dayId.slice(4, 6), dayId.slice(6, 8))
            }
          >
            등록
          </Button>
          <Button color="error" onClick={handleClose}>
            닫기
          </Button>
        </DialogActions>
      </Dialog>

      <Snackbar open={success} autoHideDuration={6000} onClose={handleSuccessClose}>
        <Alert onClose={handleSuccessClose} severity="success" sx={{ width: '100%' }}>
          This is a success message!
        </Alert>
      </Snackbar>
    </Wrapper>
  );
}

export default Calander;
