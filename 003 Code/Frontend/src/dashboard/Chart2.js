import * as React from 'react';
import Title from './Title';
import ApexCharts from 'react-apexcharts';
import { useState, useEffect } from 'react';
import { ConfigWithToken, ManagerBaseApi } from '../authConfig';
import axios from 'axios';

export default function Chart2() {
  const config = ConfigWithToken();
  const [reservationInfo, setReservationInfo] = useState([]);
  const [predictMenus, setPredictMenus] = useState([]);
  const predictMenusArray = Object.entries(predictMenus);

  useEffect(() => {
    axios
      .get(`${ManagerBaseApi}/state/menu`, config)
      .then((res) => setReservationInfo(res.data));
    axios
      .get(`${ManagerBaseApi}/state/predict/menu`, config)
      .then((res) => setPredictMenus(res.data));
  }, []);

  return (
    <React.Fragment>
      <div style={{ display: 'flex', width: '58%', justifyContent: 'space-between' }}>
        <Title>요즘 인기있는 메뉴</Title>
        <Title>내일 예약 정보</Title>
      </div>
      <div style={{ display: 'flex' }}>
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
        />

        <ApexCharts
          style={{ marginLeft: '10vw', marginTop: '-5vh' }}
          width={500}
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
      </div>
    </React.Fragment>
  );
}
