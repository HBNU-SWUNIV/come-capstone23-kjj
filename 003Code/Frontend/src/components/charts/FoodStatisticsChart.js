import * as React from 'react';
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
            <span style={c_color}>익일 예약 메뉴</span>
          </Title>
          <ApexCharts
            width={400}
            height={400}
            type="pie"
            series={predictMenusArray?.map((items) => items[1])}
            options={{
              chart: {
                toolbar: { show: false, type: 'pie' },
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
