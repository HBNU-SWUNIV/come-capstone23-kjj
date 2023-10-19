import * as React from 'react';
import Title from '../general/Title';
import ApexCharts from 'react-apexcharts';
import { useState, useEffect } from 'react';
import { ConfigWithToken, ManagerBaseApi } from '../../auth/authConfig';
import axios from 'axios';
import styled from 'styled-components';
import { c_color } from '../../styles/global';

export default function FoodStatisticsChart() {
  const config = ConfigWithToken();
  const [predictMenus, setPredictMenus] = useState([]);
  const predictMenusArray = Object.entries(predictMenus);

  useEffect(() => {
    axios
      .get(`${ManagerBaseApi}/state/predict/menu`, config)
      .then((res) => setPredictMenus(res.data))
      .catch((err) => console.log(err));
  }, []);

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
            series={predictMenusArray.map((items) => items[1])}
            options={{
              chart: {
                toolbar: { show: false, type: 'pie' },
              },
              labels: predictMenusArray.map((items) => items[0]),
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
