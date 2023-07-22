import * as React from 'react';
import Title from './Title';
import ApexCharts from 'react-apexcharts';
import { useState, useEffect } from 'react';
import { ConfigWithToken, ManagerBaseApi } from '../authConfig';
import axios from 'axios';

export default function Chart() {
  const [predictitems, setPredictItems] = useState([]);
  const predictItemsArray = Object.entries(predictitems);
  const config = ConfigWithToken();

  useEffect(() => {
    axios
      .get(`${ManagerBaseApi}/state/predict/food`, config)
      .then((res) => setPredictItems(res.data));
  }, []);
  return (
    <React.Fragment>
      <Title>내일 식재료 예측</Title>
      <ApexCharts
        type="bar"
        series={[
          {
            name: '',
            data: predictItemsArray.map((items) => items[1]),
          },
        ]}
        width={800}
        height={170}
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
          fill: {
            colors: 'black',
            opacity: 1,
          },
        }}
      />
    </React.Fragment>
  );
}
