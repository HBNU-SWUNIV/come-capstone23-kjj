import { useEffect, useState } from 'react';

export const dateObject = [
  {
    idx: 1,
    day: 'monday',
  },
  {
    idx: 2,
    day: 'tuesday',
  },
  {
    idx: 3,
    day: 'wednesday',
  },
  {
    idx: 4,
    day: 'thursday',
  },
  {
    idx: 5,
    day: 'friday',
  },
];

const UseCompareReservation = (props) => {
  const { setChipFn, data } = props;
  const date = new Date();

  // useCustomhook - useEffect, setFn 무한 루프 문제 방지(자물쇠)
  const [doNotInfinite, setDonotInfinite] = useState(false);
  const isData = data.length !== 0;

  // getDay()함수 => 토6, 일0, 월1 반환
  const getToday = date.getDay();

  const [reservationsComparedToYesterday, setreservationsComparedToYesterday] =
    useState(0);

  // 토, 일, 월요일은 전일이랑 비교 불가
  const hasYesterdayData = getToday !== 0 && getToday !== 1 && getToday !== 6;

  // 예약자 수 전일, 금일 비교
  useEffect(() => {
    if (hasYesterdayData && isData) {
      if (!doNotInfinite) {
        const todayidx = dateObject.filter((item) => item.idx == getToday)[0].idx;

        const today = dateObject.find((item) => item.idx == todayidx).day;
        const yesterday = dateObject.find((item) => item.idx == todayidx - 1).day;

        const yesterdayValue = data?.find((item) => item[0] == yesterday)?.[1];

        const todayValue = data?.find((item) => item[0] == today)?.[1];

        if (todayValue !== undefined && yesterdayValue !== undefined) {
          todayValue - yesterdayValue < 0
            ? setChipFn((prev) => ({
                ...prev,
                reservation: true,
              }))
            : setChipFn((prev) => ({
                ...prev,
                reservation: false,
              }));
          setreservationsComparedToYesterday(todayValue - yesterdayValue);
        }
      }
      setDonotInfinite(true);
    }
  }, []);
  return { reservationsComparedToYesterday };
};

export default UseCompareReservation;
