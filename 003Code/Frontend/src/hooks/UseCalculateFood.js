import { useEffect, useState } from 'react';
import { useQuery } from 'react-query';
import { getStatusFood } from '../api/apis';
import { ConfigWithToken } from '../utils/utils';
import { dateObject } from './dashboard_statistics/UseUserPop';

const UseCalculateFood = (props) => {
  const config = ConfigWithToken();
  const { data, isLoading } = useQuery(['getFood', config], () => getStatusFood(config));

  const [chartdata, setChartdata] = useState({
    prev: [],
    next: [],
  });

  useEffect(() => {
    if (!isLoading) {
      const dataform = {
        prevdata: [],
        nextdata: [],
      };

      Object.values(data?.entire).forEach((item) => {
        dataform.prevdata.push(item / 1000);
      });

      Object.values(data?.part).forEach((item) => {
        dataform.nextdata.push(item / 1000);
      });

      setChartdata({
        prev: dataform.prevdata,
        next: dataform.nextdata,
      });
    }
  }, [isLoading]);

  const date = new Date();
  const getToday = date.getDay();

  const today = dateObject.filter((item) => item.idx == getToday)[0]?.day;

  const prevValue = !isLoading && data?.entire[today];
  const nextValue = !isLoading && data?.part[today];

  const calculatedValue = (prevValue - nextValue) / 1000;

  // 전주랑 식재료 비교
  const lastweekSum =
    !isLoading && Object.values(data?.entire).reduce((acc, val) => (acc += val), 0);

  const thisweekSum =
    !isLoading && Object.values(data?.part).reduce((acc, cur) => (acc += cur), 0);

  const calculatedWeeksValue = Math.ceil((thisweekSum - lastweekSum) / 1000);
  const isChipfn = props?.setChipFn !== undefined;

  useEffect(() => {
    if (calculatedWeeksValue > 0 && isChipfn)
      props?.setChipFn((prev) => ({
        ...prev,
        minus: false,
      }));
    else
      props?.setChipFn((prev) => ({
        ...prev,
        minus: true,
      }));
  }, [isLoading]);

  return {
    chartdata,
    calculatedValue: !isLoading && calculatedValue.toFixed(0),
    calculatedWeeksValue,
  };
};

export default UseCalculateFood;
