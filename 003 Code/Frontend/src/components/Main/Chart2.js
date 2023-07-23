import * as React from 'react';
import Title from '../general/Title';
import ApexCharts from 'react-apexcharts';
import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { ConfigWithToken, ManagerBaseApi } from '../../auth/authConfig';
import axios from 'axios';
import styled from 'styled-components';

const Wrapper = styled.div`
  display: flex;
  @media (max-width: 1000px) {
    flex-direction: column;
  }
`;
const ChartWrapper = styled.div`
  width: 50%;
  display: flex;
  flex-direction: column;
`;

export default function Chart2() {
  const navigate = useNavigate();
  const config = ConfigWithToken();
  const [reservationInfo, setReservationInfo] = useState([]);
  const [predictMenus, setPredictMenus] = useState([]);
  const predictMenusArray = Object.entries(predictMenus);

  useEffect(() => {
    axios
      .get(`${ManagerBaseApi}/state/menu`, config)
      .then((res) => setReservationInfo(res.data))
      .catch((err) => {
        err.response.status === 401 && navigate('/');
      });
    axios
      .get(`${ManagerBaseApi}/state/predict/menu`, config)
      .then((res) => setPredictMenus(res.data));
  }, []);

  return (
    <React.Fragment>
      <Wrapper>
        <ChartWrapper>
          <Title>요즘 인기있는 메뉴</Title>
          <ApexCharts
            type="pie"
            series={reservationInfo.map((menu) => menu.count)}
            options={{
              chart: {
                type: 'pie',
                toolbar: { show: false },
              },
              labels: reservationInfo.map((menu) => menu.name),
            }}
            width={400}
            height={400}
            style={{ margin: '3vh' }}
          />
        </ChartWrapper>
        <ChartWrapper>
          <Title>내일 예약 정보</Title>
          <ApexCharts
            style={{ marginTop: '-3vh' }}
            width={400}
            height={300}
            type="bar"
            series={[
              {
                name: '',
                data: predictMenusArray.map((items) => items[1]),
              },
            ]}
            options={{
              chart: {
                toolbar: { show: false },
              },
              plotOptions: {
                bar: {
                  borderRadius: 4,
                  horizontal: true,
                },
              },
              stroke: {
                curve: 'smooth',
                width: 1,
              },
              xaxis: {
                type: 'category',
                categories: predictMenusArray.map((items) => items[0]),
              },
              fill: {
                colors: 'green',
                opacity: 1,
              },
            }}
          />
        </ChartWrapper>
      </Wrapper>
    </React.Fragment>
  );
}
