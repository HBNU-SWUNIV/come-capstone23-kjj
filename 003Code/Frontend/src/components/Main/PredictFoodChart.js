import * as React from 'react';
import Title from '../general/Title';
import ApexCharts from 'react-apexcharts';
import { useState, useEffect } from 'react';
import { ConfigWithToken, ManagerBaseApi } from '../../auth/authConfig';
import axios from 'axios';
import { c_color } from './chartTitleColors';

export default function PredictFoodChart() {
  const [predictitems, setPredictItems] = useState([]);
  const config = ConfigWithToken();

  const predictItemsArray = Object.entries(predictitems);

  useEffect(() => {
    axios
      .get(`${ManagerBaseApi}/state/predict/food`, config)
      .then((res) => setPredictItems(res.data))
      .catch((err) => {
        if (err.response.status === 403) {
        }
      });
  }, []);

  return (
    <>
      <Title>
        <span style={c_color}>익월 필요 식재료 양</span>
      </Title>
      <ApexCharts
        type="bar"
        series={[
          {
            name: '',
            data: predictItemsArray.map((items) => items[1]),
          },
        ]}
        height={300}
        options={{
          chart: {
            toolbar: { show: false },
          },
          stroke: {
            curve: 'smooth',
            width: 1,
          },
          xaxis: {
            type: 'category',
            categories: predictItemsArray.map((items) => items[0]),
          },
          yaxis: {
            labels: {
              formatter: (val) => {
                return val + 'kg';
              },
            },
          },
          fill: {
            colors: '#fea897',
            opacity: 1,
          },
          colors: ['#fea897'],
        }}
      />
    </>
  );
}
