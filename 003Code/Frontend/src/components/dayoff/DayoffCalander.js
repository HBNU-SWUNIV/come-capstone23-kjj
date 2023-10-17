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
import { useRef, useState } from 'react';
import { AiOutlineLeft, AiOutlineRight } from 'react-icons/ai';
import { useMutation, useQuery } from 'react-query';
import shortid from 'shortid';
import styled from 'styled-components';
import { getHoliday, getOffDay } from '../../api/apis';
import { ConfigWithToken, ManagerBaseApi } from '../../auth/authConfig';
import Circle from '../general/Circle';
import DayoffDialog from './DayoffDialog';

function DayoffCalander() {
  const config = ConfigWithToken();
  const [dayId, setDayId] = useState(0);
  const [currentMonth, setCurrentMonth] = useState(new Date());
  const [open, setOpen] = useState(false);
  const monthStart = startOfMonth(currentMonth);
  const monthEnd = endOfMonth(monthStart);
  const startDate = startOfWeek(monthStart);
  const endDate = endOfWeek(monthEnd);
  let day = startDate;
  let formattedDate = '';
  let dayss = [];
  let line = [];
  const days = [];
  const date = ['일', '월', '화', '수', '목', '금', '토'];
  const thisyear = format(currentMonth, 'yyyy');
  const thismonth = format(currentMonth, 'MM');
  const offNameRef = useRef('');

  const handleOpen = (id) => {
    setOpen(true);
    setDayId(id);
  };
  const handleClose = () => {
    setOpen(false);
  };
  const prevMonth = () => {
    setCurrentMonth(subMonths(currentMonth, 1));
  };
  const nextMonth = () => {
    setCurrentMonth(addMonths(currentMonth, 1));
  };

  const { data: offday, refetch: refreshOffday } = useQuery(
    ['getOffDay', config, thisyear, thismonth],
    () => getOffDay(config, thisyear, thismonth)
  );

  const { data: holiday } = useQuery(['getHoliday', thisyear, thismonth], () =>
    getHoliday(thisyear, thismonth)
  );
  const isArray = holiday?.length !== undefined;

  const setOnOffDay = useMutation(
    (data) => {
      const { body, year, month, day } = data;
      axios.post(`${ManagerBaseApi}/store/off/${year}/${month}/${day}`, body, config);
    },
    {
      // network탭에서 폭포 확인해봤을 때, onSuccess랑 위에 post api가 동시에 요청됨.. QueryClient에서 defualt값을 설정해줘야 하나?
      onSuccess: () => {
        setTimeout(() => {
          refreshOffday();
        }, 200);
      },
    }
  );

  const onOffday = (year, month, day) => {
    const body = { off: true, name: offNameRef?.current?.value };
    const data = { body, year, month, day };
    setOnOffDay.mutate(data);
    handleClose();
  };

  const onOnday = (year, month, day) => {
    let body = { off: false };
    const data = { body, year, month, day };
    setOnOffDay.mutate(data);
    handleClose();
  };

  for (let i = 0; i < 7; i++) {
    days.push(<DaysDiv key={shortid.generate()}>{date[i]}</DaysDiv>);
  }

  while (day <= endDate) {
    for (let i = 0; i < 7; i++) {
      formattedDate = format(day, 'd').padStart(2, '0').toString();
      const id = format(day, 'yyyyMMdd').toString();

      if (format(monthStart, 'M') != format(day, 'M')) {
        dayss.push(
          <DivDay key={shortid.generate()}>
            <span style={divDaySpanStyle}>{formattedDate}</span>
          </DivDay>
        );
      } else {
        dayss.push(
          <DivDay onClick={() => handleOpen(id)} key={shortid.generate()}>
            <span>{formattedDate}</span>
            <span style={{ color: customRed }}>
              {!isArray && holiday?.locdate == id && holiday?.dateName}
              {isArray && holiday?.map((hol) => hol?.locdate == id && hol?.dateName)}
              {offday?.filter((offday) => offday.date === id)[0]?.name}
            </span>
            {offday?.filter((offday) => offday.date == id)[0]?.off == true ? (
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

  return (
    <Wrapper>
      <HeaderW>
        <AiOutlineLeft style={{ ...ArrowCSS }} onClick={prevMonth} />
        <span>
          {format(currentMonth, 'yyyy')}.{format(currentMonth, 'MM')}
        </span>
        <AiOutlineRight style={{ ...ArrowCSS }} onClick={nextMonth} />
      </HeaderW>

      <DaysWrapper>{days}</DaysWrapper>
      <DivWrapper>{line}</DivWrapper>

      <DayoffDialog
        open={open}
        onClose={handleClose}
        dayId={dayId}
        offNameRef={offNameRef}
        isArray={isArray}
        holiday={holiday}
        offday={offday}
        onOnday={onOnday}
        onOffday={onOffday}
      />
    </Wrapper>
  );
}

export default DayoffCalander;

const divDaySpanStyle = {
  fontSize: '13px',
  fontWeight: 600,
  margin: '5px 5px',
  color: 'rgba(0,0,0,0.3)',
};

const ArrowCSS = {
  color: '#969696',
  fontSize: '18px',
  margin: '0 20px',
  fontWeight: 600,
  cursor: 'pointer',
};

const customBlue = '#64b5f6';
const customRed = '#f44336';

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
