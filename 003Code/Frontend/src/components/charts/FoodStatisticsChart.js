import Title from '../general/Title';
import ApexCharts from 'react-apexcharts';
import styled from 'styled-components';
import { c_color } from '../../styles/global';
import UseGetCharts from '../../hooks/UseGetCharts';

export default function FoodStatisticsChart() {
  const { predictMenusArray } = UseGetCharts();

  return (
    <>
      <Wrapper>
        <ChartWrapper>
          <Title>
            <span style={{ ...c_color, fontSize: '16px' }}>익일 예약 메뉴</span>
          </Title>
          <ApexCharts
            width={400}
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
  display: flex;
  justify-content: space-between;
  @media (max-width: 1000px) {
    flex-direction: column;
  }
`;
const ChartWrapper = styled.div`
  width: 40%;
  display: flex;
  flex-direction: column;
`;
