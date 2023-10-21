import { useQuery } from 'react-query';
import { ConfigWithToken } from '../auth/authConfig';
import { getStatusFood } from './apis';
import { useEffect, useState } from 'react';

const Api_calculate_food = () => {
  const config = ConfigWithToken();
  const { data, isLoading } = useQuery(['getFood', config], () => getStatusFood(config));

  const [chartdata, setChartdata] = useState({
    prev: [],
    next: [],
  });

  const prevSum = chartdata.prev.reduce((acc, cur) => (acc += cur), 0);
  const nextSum = chartdata.next.reduce((acc, cur) => (acc += cur), 0);

  const calculatedValue = prevSum - nextSum;

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

  return { chartdata, calculatedValue };
};

export default Api_calculate_food;
