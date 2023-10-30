import styled from 'styled-components';
import Statistics from './general/Statistics';
import UseGetCharts from '../hooks/UseGetCharts';
import UseCalculateFood from '../hooks/UseCalculateFood';
import visitorImg from '../image/working.png';
import { useState } from 'react';
import UseUserPop from '../hooks/dashboard_statistics/UseUserPop';
import UsePredictPop from '../hooks/dashboard_statistics/UsePredictPop';

const Wrapper = styled.div`
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  place-items: center;
  grid-gap: 30px;

  width: 100%;
  height: 100%;
`;

const Visitor = () => {
  return <img src={visitorImg} width={'90%'} height={'100%'} />;
};

const StatisticsWrap = () => {
  const [chipRedColor, setChipRedColor] = useState({
    user: false,
    minus: false,
  });
  const { marketCost } = UseGetCharts();
  const { calculatedValue, calculatedWeeksValue } = UseCalculateFood({
    setChipFn: setChipRedColor,
  });
  const { todaypop, comparedpop } = UseUserPop({
    setChipFn: setChipRedColor,
  });
  const { calculatedpredictpop, predictpop } = UsePredictPop();

  const calculatedIngredientsValue = isNaN(calculatedValue) ? 0 : calculatedValue;

  const calculatedCosts = marketCost?.today - marketCost?.yesterday;
  const costs = calculatedCosts < 0 ? '-' + calculatedCosts : '+' + calculatedCosts;

  const statistics_datas = [
    {
      title: '이용자',
      data: `${todaypop}명`,
      date: '금일',
      icon: <Visitor />,
      chip: `${comparedpop}명`,
      chipcolor: chipRedColor.user,
    },
    {
      title: '매출액',
      data: `${marketCost?.today}원`,
      date: '금일',
      chip: `${!isNaN(costs) && costs}원`,
      chipcolor: calculatedCosts < 0,
    },
    {
      title: '식재료 절약양',
      data: `${calculatedIngredientsValue}kg`,
      date: '금일',
      chip: `${calculatedWeeksValue}kg`,
      chipcolor: !chipRedColor.minus,
      isWeek: true,
      tip: '월요일 기준, 전주 월요일과 비교됨',
    },
    {
      title: '예약자',
      data: `${predictpop?.tomorrow}명`,
      date: '익일',
      chip: calculatedpredictpop + '명',
      chipcolor: calculatedpredictpop < 0,
    },
  ];

  return (
    <Wrapper>
      {statistics_datas.map((item, idx) => (
        <Statistics key={idx + 'statisticskey'} {...item} />
      ))}
    </Wrapper>
  );
};

export default StatisticsWrap;
