import { useQuery } from 'react-query';
import { ConfigWithToken } from '../auth/authConfig';
import {
  getPredictFoods,
  getPredictMenus,
  getReservation,
  getTodayPop,
} from '../api/apis';

const UseGetCharts = () => {
  const config = ConfigWithToken();
  const today = new Date();

  const todaydates = {
    today: new Date(),
    year: today.getFullYear(),
    month: today.toLocaleString('en-US', {
      month: 'long',
    }),
    day: today.getDate(),
  };

  // predict-menus
  const { data: predictMenus, isLoading } = useQuery(['getPredict', config], () =>
    getPredictMenus(config)
  );
  const predictMenusArray = Object.entries(!isLoading && predictMenus);

  // predict-foods
  const { data: predictfoods, isLoading: predictfoodsloading } = useQuery(
    ['getpredictfoods', config],
    () => getPredictFoods(config)
  );
  const predictfoodsArray = Object.entries(!predictfoodsloading && predictfoods);

  // reservation-infos
  const { data: reservation, isLoading: reservationloading } = useQuery(
    ['getreservation', config],
    () => getReservation(config)
  );
  const reservationArray = Object.entries(!reservationloading && reservation).slice(1);

  // today-pop
  const { data: todaypop, isLoading: todaypoploading } = useQuery(
    ['gettoday', config],
    () => getTodayPop(config)
  );

  return {
    predictMenusArray,
    predictfoodsArray,
    reservationArray,
    todaypop: !todaypoploading && todaypop,
    todaydates,
  };
};

export default UseGetCharts;
