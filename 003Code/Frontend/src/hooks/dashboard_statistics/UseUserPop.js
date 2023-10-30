import { useQuery } from 'react-query';
import { getUserPop } from '../../api/apis';
import { ConfigWithToken } from '../../auth/authConfig';
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

const UseUserPop = (props) => {
  const config = ConfigWithToken();
  const date = new Date();
  const getDay = date.getDay();

  const { data: userPop, isLoading } = useQuery(['getuserpop', config], () =>
    getUserPop(config)
  );
  const [todaypop, setTodaypop] = useState(0);
  const [comparedpop, setComparedpop] = useState(0);
  const today = dateObject?.find((item) => item.idx == getDay)?.day;
  const yesterday = dateObject?.find((item) => item.idx == getDay - 1)?.day;

  useEffect(() => {
    if (getDay == 1) {
      setTodaypop(userPop?.[today]);
      setComparedpop(`${userPop?.[today]}`);
    } else if (getDay == 6 || getDay == 0) setTodaypop(0);
    else {
      setTodaypop(userPop?.[today]);
      setComparedpop(`${userPop?.[today] - userPop?.[yesterday]}`);
    }
  }, [isLoading]);

  const isMinus = comparedpop < 0;

  useEffect(() => {
    if (isMinus) {
      props?.setChipFn((prev) => ({
        ...prev,
        user: true,
      }));
    } else {
      props?.setChipFn((prev) => ({
        ...prev,
        user: false,
      }));
    }
  }, [isMinus]);

  return { todaypop, comparedpop: isMinus ? comparedpop : '+' + comparedpop };
};

export default UseUserPop;
