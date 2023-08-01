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
import { useMatch, useNavigate } from 'react-router-dom';
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
import Circle from '../general/Circle';

const ArrowCSS = { color: '#969696', fontSize: '1.875rem', margin: '0 1.25rem' };

const Wrapper = styled.div`
  display: flex;
  margin-left: -1vw;
  margin-top: -4vh;
  flex-direction: column;
  align-items: center;
`;
const HeaderW = styled.div`
  width: 60vw;
  height: 12vh;
  display: flex;
  justify-content: center;
  align-items: center;
  span {
    color: #383838;
    font-size: 1.563rem;
    font-weight: 600;
  }
`;
const DaysDiv = styled.div`
  width: 9vw;
  height: 7vh;
  display: flex;
  justify-content: center;
  align-items: center;
  border: 0.1px solid #5b5b5b;
`;
const DaysWrapper = styled.div`
  display: flex;
  justify-content: space-between;
  width: 63vw;
`;
const DivDay = styled.div`
  width: 9vw;
  height: 13vh;
  display: flex;
  justify-content: space-between;
  border: 0.1px solid #5b5b5b;
  span:first-child {
    font-size: 0.188rem;
    font-weight: 600;
    margin: 0.313rem 0.313rem;
  }
  span:last-child {
    margin-top: 0.2vh;
    margin-right: 0.2vw;
    font-size: 0.75rem;
    white-space: pre-wrap;
  }
`;
const DivWeek = styled.div`
  display: flex;
  width: 63vw;
  height: 13vh;
`;
const DivWrapper = styled.div`
  width: 63vw;
  flex-direction: column;
`;

function Calander() {
  const config = ConfigWithToken();
  const navigate = useNavigate();
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
      .catch((err) => {
        err.response.status === 401 && navigate('/');
      });
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
              backgroundColor: '#383838',
              opacity: '0.5',
            }}
            key={shortid.generate()}
          >
            <span
              style={{
                fontSize: '13px',
                fontWeight: 600,
                margin: '5px 5px',
              }}
            >
              {formattedDate}
              <Circle $color="red" />
            </span>
          </DivDay>
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
        <span>
          {format(currentMonth, 'yyyy')}. {format(currentMonth, 'MM')}
        </span>
        <AiOutlineRight style={{ ...ArrowCSS }} onClick={nextMonth} />
      </HeaderW>
      <DaysWrapper>{days}</DaysWrapper>
      <DivWrapper>{line}</DivWrapper>

      <Dialog open={open} onClose={handleClose}>
        <DialogTitle>{dayId} - 식단등록</DialogTitle>
        <DialogContent>
          <DialogContentText sx={{ marginBottom: '1vh' }}>
            식단 내용을 지우고 싶으시면 빈 내용으로 등록해주시면 됩니다.
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
