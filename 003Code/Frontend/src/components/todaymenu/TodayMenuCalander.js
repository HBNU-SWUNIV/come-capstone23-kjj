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
import { useState } from 'react';
import styled from 'styled-components';
import { AiOutlineLeft, AiOutlineRight } from 'react-icons/ai';
import shortid from 'shortid';
import axios from 'axios';
import { ConfigWithToken, ManagerBaseApi } from '../../auth/authConfig';
import { useMutation, useQuery } from 'react-query';
import { getDailyMenu } from '../../api/apis';
import TodayMenuInputDialog from './TodayMenuInputDialog';

function TodayMenuCalander() {
  const config = ConfigWithToken();
  const [dailyMenuInfo, setDailyMenuInfo] = useState('');
  const [dayId, setDayId] = useState(0);
  const [open, setOpen] = useState(false);
  const [currentDate, setCurrentDate] = useState(new Date());
  const currentYear = format(currentDate, 'yyyy');
  const currentMonth = format(currentDate, 'MM');
  const days = [];
  const date = ['일', '월', '화', '수', '목', '금', '토'];
  const monthStart = startOfMonth(currentDate);
  const monthEnd = endOfMonth(monthStart);
  const startDate = startOfWeek(monthStart);
  const endDate = endOfWeek(monthEnd);
  let day = startDate;
  let formattedDate = '';
  let dayss = [];
  let line = [];

  const handleClickOpen = (id) => {
    setOpen(true);
    setDayId(id);
  };
  const handleClose = () => {
    setOpen(false);
  };
  const prevMonth = () => {
    setCurrentDate(subMonths(currentDate, 1));
  };
  const nextMonth = () => {
    setCurrentDate(addMonths(currentDate, 1));
  };
  const dailyMenusInputHandler = (event) => {
    event.preventDefault();
    setDailyMenuInfo(event.target.value);
  };
  const onSaveDailyMenus = (year, month, day) => {
    const date = { year, month, day };
    saveDailyMenus.mutate(date);
  };

  const { data: savedDailyMenuInfo, refetch: refreshSavedDailyMenuInfo } = useQuery(
    ['getIsTodayMenu', currentYear, currentMonth],
    () => getDailyMenu(config, currentYear, currentMonth)
  );
  const saveDailyMenus = useMutation(
    (date) => {
      const body = { menus: dailyMenuInfo };
      axios.post(
        `${ManagerBaseApi}/planner/${date.year}/${date.month}/${date.day}`,
        body,
        config
      );
    },
    {
      onSuccess: () => {
        setTimeout(() => {
          refreshSavedDailyMenuInfo();
          setDailyMenuInfo('');
          handleClose();
        }, 200);
      },
      onError: (err) => {
        console.error('savedDailyMenu_Error = ', err);
      },
    }
  );

  for (let i = 0; i < 7; i++) {
    days.push(<DaysDiv key={shortid.generate()}>{date[i]}</DaysDiv>);
  }

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
              {savedDailyMenuInfo?.map((savedbackban) =>
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

  return (
    <Wrapper>
      <HeaderW>
        <AiOutlineLeft style={{ ...ArrowCSS }} onClick={prevMonth} />
        <span style={headerWSpanStyle}>
          {format(currentDate, 'yy')}년 {format(currentDate, 'MM')}월
        </span>
        <AiOutlineRight style={{ ...ArrowCSS }} onClick={nextMonth} />
      </HeaderW>
      <DaysWrapper>{days}</DaysWrapper>
      <DivWrapper>{line}</DivWrapper>

      <TodayMenuInputDialog
        open={open}
        onClose={handleClose}
        dayId={dayId}
        dailyMenuInfo={dailyMenuInfo}
        dailyMenusInputHandler={dailyMenusInputHandler}
        onSaveDailyMenus={onSaveDailyMenus}
      />
    </Wrapper>
  );
}

export default TodayMenuCalander;

const ArrowCSS = {
  color: 'black',
  fontSize: '1.2rem',
  margin: '0 1.25rem',
  marginBottom: '3px',
  cursor: 'pointer',
};

const headerWSpanStyle = {
  fontWeight: '600',
  fontSize: '20px',
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
    font-size: 0.7rem;
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
