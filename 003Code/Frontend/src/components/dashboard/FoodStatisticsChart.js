import ApexCharts from 'react-apexcharts';
import styled from 'styled-components';
import UseGetCharts from '../../hooks/UseGetCharts';
import Title from '../general/Title';
import { flexColumn, flexJBetween } from '../../styles/global.style';

export default function FoodStatisticsChart() {
  const { predictMenusArray } = UseGetCharts();

  return (
    <>
      <Wrapper>
        <ChartWrapper>
          <Title>익일 예약 메뉴</Title>
          <ApexCharts
            width={450}
            height={400}
            type="donut"
            series={predictMenusArray?.map((items) => items[1])}
            options={{
              fill: {
                type: 'gradient',
              },
              plotOptions: {
                pie: {
                  startAngle: -90,
                  endAngle: 270,
                },
              },
              legend: {
                position: 'bottom',
              },
              chart: {
                toolbar: { show: false, type: 'donut' },
              },
              labels: predictMenusArray?.map((items) => items[0]),
            }}
          />
        </ChartWrapper>
      </Wrapper>
    </>
  );
}

const Wrapper = styled.div`
  ${flexJBetween};
  @media (max-width: 1000px) {
    flex-direction: column;
  }
`;
const ChartWrapper = styled.div`
  width: 40%;
  ${flexColumn};
`;
