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
import { useEffect, useRef, useState } from 'react';
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
import Snackbar from '@mui/material/Snackbar';
import Alert from '@mui/material/Alert';
import { ConfigWithToken, ManagerBaseApi } from '../../auth/authConfig';
import Circle from '../general/Circle';

const holidayServiceKey = `ziROfCzWMmrKIseBzkXs58HpS39GI%2FmxjSEmUeZbKwYuyxnSc2kILXCBXlRpPZ8iam5cqwZqtw6db7CnWG%2FQQQ%3D%3D`;
const holidayBaseApi = `http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getHoliDeInfo?`;
const headSpanStyle = {
  fontFamily: 'NotoSans',
};

const customButtonStyle = {
  fontFamily: 'NotoSans',
  fontWeight: 600,
};
const ArrowCSS = {
  color: '#969696',
  fontSize: '18px',
  margin: '0 20px',
  fontWeight: 600,
};

const customBlue = '#64b5f6';
const customRed = '#f44336';
const customGray = 'rgba(0,0,0,0.2)';

const Wrapper = styled.div`
  width: 100%;

  margin-left: -1vw;
  margin-top: -4vh;

  display: flex;
  flex-direction: column;
  align-items: center;
`;
const HeaderW = styled.div`
  width: 100%;
  height: 12vh;

  display: flex;
  justify-content: center;
  align-items: center;

  span {
    color: ${customBlue};
    font-size: 25px;
    font-weight: 600;
  }
`;
const DaysDiv = styled.div`
  width: 100%;
  height: 5vh;

  padding-left: 3px;

  display: flex;
  justify-content: flex-start;
  align-items: center;

  background-color: rgba(0, 0, 0, 0.1);
`;
const DaysWrapper = styled.div`
  display: flex;
  justify-content: space-between;

  font-family: NotoSans;
  font-weight: 600;

  width: 100%;

  div:first-child {
    color: ${customRed};
  }
  div:last-child {
    color: ${customBlue};
  }
`;
const DivDay = styled.div`
  position: relative;
  width: 15%;
  height: 100%;

  display: flex;
  justify-content: space-between;

  border: 0.1px solid rgba(0, 0, 0, 0.1);

  span {
    font-size: 13px;
    font-weight: 600;
    margin: 5px 6px;
    z-index: 1;
  }
`;
const DivWeek = styled.div`
  display: flex;
  width: 100%;
  height: 13vh;
  div:first-child {
    span {
      color: ${customRed};
    }
  }
  div:last-child {
    span {
      color: ${customBlue};
    }
  }
`;
const DivWrapper = styled.div`
  width: 100%;
  height: 100%;

  border: 1px solid rgba(0, 0, 0, 0.1);
`;

const HeadTextWrapper = styled.div`
  width: 100%;

  display: flex;
  justify-content: flex-start;
  align-items: center;

  span:last-child {
    margin-left: 15px;
  }
`;

const BodyWrapper = styled.div`
  display: flex;
  flex-direction: column;

  border-top: 2px solid black;

  div {
    display: flex;
    align-items: center;

    div:first-child {
      height: 100px;
      width: 150px;

      background-color: #e3f2fd;

      border-right: 1px solid ${customGray};
      border-left: 1px solid ${customGray};

      display: flex;
      justify-content: flex-start;
      align-items: center;

      box-sizing: border-box;
      padding-left: 20px;

      font-weight: 600;
    }
  }
`;

const OffHr = styled.div`
  width: 100%;
  height: 1px;
  background-color: ${customGray};
`;

const OffDay = styled.div`
  width: 120px;
  height: 30px;

  margin-left: 20px;
  border-radius: 10px;

  display: flex;
  justify-content: center;
  align-items: center;

  background-color: ${customGray};
`;

const OffInput = styled.input`
  margin-left: 20px;

  width: 250px;
  height: 30px;

  border: 1px solid ${customGray};
`;

function Calander2() {
  const config = ConfigWithToken();

  const [offday, setOffday] = useState([]);
  const [dayId, setDayId] = useState(0);
  const [currentMonth, setCurrentMonth] = useState(new Date());

  const [success, setSuccess] = useState(false);
  const handleSuccessOpen = () => {
    setSuccess(true);
  };
  const handleSuccessClose = () => {
    setSuccess(false);
  };

  const [open, setOpen] = useState(false);
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
        `/api/user/store/off/${format(currentMonth, 'yyyy')}/${format(
          currentMonth,
          'MM'
        )}`,
        config
      )
      .then((res) => setOffday(res.data))
      .catch((err) => {
        if (err.response.status === 403) {
        } else {
          console.log('휴일 조회 에러:', err);
        }
      });
  }, [currentMonth]);

  const [holiday, setHoliday] = useState([]);
  const isArray = holiday?.length !== undefined;
  const thisyear = format(currentMonth, 'yyyy');
  const thismonth = format(currentMonth, 'MM');
  useEffect(() => {
    axios
      .get(
        `${holidayBaseApi}solYear=${thisyear}&solMonth=${thismonth}&ServiceKey=${holidayServiceKey}`
      )
      .then((res) => {
        setHoliday(res.data.response.body.items.item);
      })
      .catch((err) => console.log('dayoffError', err));
  }, [thisyear, thismonth]);

  const offNameRef = useRef('');
  const onOffday = (year, month, day) => {
    let body = { off: true, name: offNameRef?.current?.value };
    axios
      .post(`${ManagerBaseApi}/store/off/${year}/${month}/${day}`, body, config)
      .then((res) => {
        res.status == 200 &&
          axios
            .get(
              `/api/user/store/off/${format(currentMonth, 'yyyy')}/${format(
                currentMonth,
                'MM'
              )}`,
              config
            )
            .then((re) => setOffday(re.data));
      });
    handleClose();
    handleSuccessOpen();
  };
  const onOnday = (year, month, day) => {
    let body = { off: false };
    axios
      .post(`${ManagerBaseApi}/store/off/${year}/${month}/${day}`, body, config)
      .then((res) => {
        res.status == 200 &&
          axios
            .get(
              `/api/user/store/off/${format(currentMonth, 'yyyy')}/${format(
                currentMonth,
                'MM'
              )}`,
              config
            )
            .then((re) => setOffday(re.data));
      });
    handleClose();
    handleSuccessOpen();
  };

  const days = [];
  const date = ['일', '월', '화', '수', '목', '금', '토'];

  for (let i = 0; i < 7; i++) {
    days.push(<DaysDiv key={shortid.generate()}>{date[i]}</DaysDiv>);
  }

  const monthStart = startOfMonth(currentMonth);
  const monthEnd = endOfMonth(monthStart);
  const startDate = startOfWeek(monthStart);
  const endDate = endOfWeek(monthEnd);

  let day = startDate;
  let formattedDate = '';
  let dayss = [];
  let line = [];

  while (day <= endDate) {
    for (let i = 0; i < 7; i++) {
      formattedDate = format(day, 'd').padStart(2, '0').toString();
      const id = format(day, 'yyyyMMdd').toString();

      if (format(monthStart, 'M') != format(day, 'M')) {
        dayss.push(
          <DivDay key={shortid.generate()}>
            <span
              style={{
                fontSize: '13px',
                fontWeight: 600,
                margin: '5px 5px',
                color: 'rgba(0,0,0,0.3)',
              }}
            >
              {formattedDate}
            </span>
          </DivDay>
        );
      } else {
        dayss.push(
          <DivDay onClick={() => handleClickOpen(id)} key={shortid.generate()}>
            <span>{formattedDate}</span>
            <span style={{ color: customRed }}>
              {!isArray && holiday?.locdate == id && holiday?.dateName}
              {isArray && holiday?.map((hol) => hol?.locdate == id && hol?.dateName)}
              {offday?.filter((offday) => offday.date === id)[0]?.name}
            </span>
            {offday.filter((offday) => offday.date == id)[0]?.off == true ? (
              <Circle color="red" />
            ) : null}
            {!isArray
              ? holiday?.locdate == id && <Circle color="red" />
              : holiday?.map((hol) => hol?.locdate == id && <Circle color="red" />)}
          </DivDay>
        );
      }
      day = addDays(day, 1);
    }
    line.push(<DivWeek key={shortid.generate()}>{dayss}</DivWeek>);
    dayss = [];
  }
  console.log(offday);
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
        <AiOutlineLeft className="pointer" style={{ ...ArrowCSS }} onClick={prevMonth} />
        <span>
          {format(currentMonth, 'yyyy')}.{format(currentMonth, 'MM')}
        </span>
        <AiOutlineRight className="pointer" style={{ ...ArrowCSS }} onClick={nextMonth} />
      </HeaderW>

      <DaysWrapper>{days}</DaysWrapper>
      <DivWrapper>{line}</DivWrapper>

      <Dialog open={open} onClose={handleClose}>
        <DialogTitle sx={{ width: '600px' }}>
          <HeadTextWrapper>
            <span style={{ ...headSpanStyle, fontSize: '22px', fontWeight: 600 }}>
              휴일등록
            </span>
            <span style={{ ...headSpanStyle, fontSize: '16px', color: 'gray' }}>
              우리 식당의 휴일을 지정합니다.
            </span>
          </HeadTextWrapper>
        </DialogTitle>

        <DialogContent>
          <BodyWrapper>
            <div>
              <div>선택일자</div>
              <OffDay>{dayId}</OffDay>
            </div>
            <OffHr />
            <div>
              <div>휴일명</div>
              <OffInput ref={offNameRef} placeholder="휴일 명칭을 입력하세요" />
            </div>
            <OffHr />
          </BodyWrapper>
        </DialogContent>

        <DialogActions
          sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center' }}
        >
          {!isArray
            ? holiday?.locdate == dayId && (
                <span
                  style={{
                    ...customButtonStyle,
                    fontSize: '16px',
                    color: customRed,
                    marginRight: '10px',
                  }}
                >
                  '{holiday?.dateName}' 입니다
                </span>
              )
            : holiday?.map(
                (hol) =>
                  hol?.locdate == dayId && (
                    <span
                      style={{
                        ...customButtonStyle,
                        fontSize: '16px',
                        color: customRed,
                        marginRight: '10px',
                      }}
                    >
                      '{hol?.dateName}' 입니다
                    </span>
                  )
              )}

          {offday.filter((offday) => offday.date == dayId)[0]?.off == true ? (
            <Button
              sx={customButtonStyle}
              onClick={() =>
                onOnday(dayId.slice(0, 4), dayId.slice(4, 6), dayId.slice(6, 8))
              }
            >
              영업일 등록
            </Button>
          ) : (
            <Button
              sx={customButtonStyle}
              disabled={
                (!isArray && holiday?.locdate == dayId) ||
                (isArray && holiday.some((hol) => hol?.locdate == dayId))
              }
              onClick={() =>
                onOffday(dayId.slice(0, 4), dayId.slice(4, 6), dayId.slice(6, 8))
              }
            >
              휴일 등록
            </Button>
          )}

          <Button sx={customButtonStyle} color="error" onClick={handleClose}>
            닫기
          </Button>
        </DialogActions>
      </Dialog>

      <Snackbar open={success} autoHideDuration={6000} onClose={handleSuccessClose}>
        <Alert onClose={handleSuccessClose} severity="success" sx={{ width: '100%' }}>
          성공!
        </Alert>
      </Snackbar>
    </Wrapper>
  );
}

export default Calander2;
