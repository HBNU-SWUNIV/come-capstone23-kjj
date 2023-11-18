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
import { ConfigWithToken, ManagerBaseApi } from '../../utils/utils';
import Circle from '../general/Circle';
import DayoffDialog from './DayoffDialog';
import {
  flexCenter,
  flexColumn,
  flexICenter,
  flexJBetween,
  fullSize,
} from '../../styles/global.style';

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
    days.push(<Days key={shortid.generate()}>{date[i]}</Days>);
  }

  while (day <= endDate) {
    for (let i = 0; i < 7; i++) {
      formattedDate = format(day, 'd').padStart(2, '0').toString();
      const id = format(day, 'yyyyMMdd').toString();

      if (format(monthStart, 'M') != format(day, 'M')) {
        dayss.push(
          <Day key={shortid.generate()}>
            <span style={divDaySpanStyle}>{formattedDate}</span>
          </Day>
        );
      } else {
        dayss.push(
          <Day onClick={() => handleOpen(id)} key={shortid.generate()}>
            <span>{formattedDate}</span>
            <span style={{ color: (props) => props.theme.colors.red }}>
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
          </Day>
        );
      }
      day = addDays(day, 1);
    }
    line.push(<DayRow key={shortid.generate()}>{dayss}</DayRow>);
    dayss = [];
  }

  const DayOffDialogProps = {
    open: open,
    onClose: handleClose,
    dayId: dayId,
    offNameRef: offNameRef,
    isArray: isArray,
    holiday: holiday,
    offday: offday,
    onOnday: onOnday,
    onOffday: onOffday,
  };

  return (
    <DayoffCalanderLayout>
      <DayoffCalanderHeaderRow>
        <AiOutlineLeft style={{ ...arrowStyle }} onClick={prevMonth} />
        <span>
          {format(currentMonth, 'yyyy')}.{format(currentMonth, 'MM')}
        </span>
        <AiOutlineRight style={{ ...arrowStyle }} onClick={nextMonth} />
      </DayoffCalanderHeaderRow>

      <DaysRow>{days}</DaysRow>
      <DayCol>{line}</DayCol>

      <DayoffDialog {...DayOffDialogProps} />
    </DayoffCalanderLayout>
  );
}

export default DayoffCalander;

const divDaySpanStyle = {
  margin: '5px 5px',

  fontSize: '13px',
  fontWeight: 600,
  color: 'rgba(0,0,0,0.3)',
};

const arrowStyle = {
  margin: '0 20px',
  cursor: 'pointer',

  fontSize: '18px',
  fontWeight: 600,
  color: '#969696',
};

const DayoffCalanderLayout = styled.div`
  width: 100%;
  ${flexColumn};
  align-items: center;
  margin-left: -1vw;
  margin-top: -4vh;
`;

const DayoffCalanderHeaderRow = styled.div`
  width: 100%;
  height: 12vh;
  ${flexCenter};

  span {
    color: ${(props) => props.theme.colors.blue};
    font-size: 25px;
    font-weight: 600;
  }
`;
const Days = styled.div`
  width: 100%;
  height: 5vh;
  ${flexICenter};
  justify-content: flex-start;
  padding-left: 3px;
  background-color: rgba(0, 0, 0, 0.1);
`;

const DaysRow = styled.div`
  width: 100%;
  ${flexJBetween};

  font-weight: 600;

  div:first-child {
    color: ${(props) => props.theme.colors.red};
  }
  div:last-child {
    color: ${(props) => props.theme.colors.blue};
  }
`;

const Day = styled.div`
  width: 15%;
  height: 100%;
  position: relative;
  ${flexJBetween};
  border: 0.1px solid rgba(0, 0, 0, 0.1);

  span {
    margin: 5px 6px;
    z-index: 1;

    font-size: 13px;
    font-weight: 600;
  }
`;

const DayRow = styled.div`
  width: 100%;
  height: 13vh;
  display: flex;

  div:first-child {
    span {
      color: ${(props) => props.theme.colors.red};
    }
  }
  div:last-child {
    span {
      color: ${(props) => props.theme.colors.blue};
    }
  }
`;

const DayCol = styled.div`
  ${fullSize};
  border: 1px solid rgba(0, 0, 0, 0.1);
`;
