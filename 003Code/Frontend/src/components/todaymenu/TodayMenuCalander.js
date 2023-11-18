import axios from 'axios';
import {
  addDays,
  addMonths,
  endOfMonth,
  endOfWeek,
  format,
  startOfMonth,
  startOfWeek,
  subMonths,
} from 'date-fns';
import { useState } from 'react';
import { AiOutlineLeft, AiOutlineRight } from 'react-icons/ai';
import { useMutation, useQuery } from 'react-query';
import shortid from 'shortid';
import styled from 'styled-components';
import { getDailyMenu } from '../../api/apis';
import { ConfigWithToken, ManagerBaseApi } from '../../utils/utils';
import TodayMenuInputDialog from './TodayMenuInputDialog';
import {
  flexCenter,
  flexColumn,
  flexICenter,
  flexJBetween,
} from '../../styles/global.style';

function TodayMenuCalander(props) {
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
    days.push(<DaysRow key={shortid.generate()}>{date[i]}</DaysRow>);
  }

  while (day <= endDate) {
    for (let i = 0; i < 7; i++) {
      formattedDate = format(day, 'd').padStart(2, '0').toString();
      const id = format(day, 'yyyyMMdd').toString();
      if (format(monthStart, 'M') != format(day, 'M')) {
        dayss.push(
          <Day
            style={{
              opacity: '0',
            }}
            key={shortid.generate()}
          />
        );
      } else {
        dayss.push(
          <Day onClick={() => handleClickOpen(id)} key={shortid.generate()}>
            <span>{formattedDate}</span>
            <span>
              {savedDailyMenuInfo?.map((savedbackban) =>
                savedbackban.date === id ? savedbackban.menus : null
              )}
            </span>
          </Day>
        );
      }
      day = addDays(day, 1);
    }
    line.push(<DayRow key={shortid.generate()}>{dayss}</DayRow>);
    dayss = [];
  }

  return (
    <TodayMenuCalanderLayout $isTodayMenu={props.isTodayMenu}>
      <TodayMenuCalanderHeaderRow>
        <AiOutlineLeft style={{ ...arrowStyle }} onClick={prevMonth} />
        <span style={headerWSpanStyle}>
          {format(currentDate, 'yy')}년 {format(currentDate, 'MM')}월
        </span>
        <AiOutlineRight style={{ ...arrowStyle }} onClick={nextMonth} />
      </TodayMenuCalanderHeaderRow>
      <DaysCol>{days}</DaysCol>
      <DayCol>{line}</DayCol>

      <TodayMenuInputDialog
        open={open}
        onClose={handleClose}
        dayId={dayId}
        dailyMenuInfo={dailyMenuInfo}
        dailyMenusInputHandler={dailyMenusInputHandler}
        onSaveDailyMenus={onSaveDailyMenus}
      />
    </TodayMenuCalanderLayout>
  );
}

export default TodayMenuCalander;

const arrowStyle = {
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

const TodayMenuCalanderLayout = styled.div`
  width: 65%;
  ${flexColumn};
  align-items: center;
  opacity: ${({ $isTodayMenu }) => ($isTodayMenu ? '1' : '0.5')};
  pointer-events: ${({ $isTodayMenu }) => ($isTodayMenu ? 'auto' : 'none')};

  @media screen and (max-width: 1200px) {
    width: 95%;
  }
`;
const TodayMenuCalanderHeaderRow = styled.div`
  width: 100%;
  ${flexCenter};
  position: relative;
  margin-bottom: 3%;

  span {
    color: #383838;
    font-size: 1.563rem;
    font-weight: 600;
  }
`;

const DaysRow = styled.div`
  width: 15%;
  ${flexCenter};

  font-size: 15px;
  font-weight: 500;
`;

const DaysCol = styled.div`
  width: 100%;
  ${flexICenter};
  justify-content: space-around;
`;

const Day = styled.div`
  width: 90%;
  height: 12vh;
  ${flexJBetween};
  box-sizing: border-box;
  padding: 10px;
  margin-bottom: 10%;
  border-radius: 20px;
  background-color: #f5f5f5;

  overflow: scroll;
  scrollbar-width: none;
  -ms-overflow-style: none;
  &::-webkit-scrollbar {
    display: none;
  }

  &:hover {
    cursor: pointer;
  }

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

const DayRow = styled.div`
  width: 100%;
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  place-items: center;
`;

const DayCol = styled.div`
  width: 100%;
`;
