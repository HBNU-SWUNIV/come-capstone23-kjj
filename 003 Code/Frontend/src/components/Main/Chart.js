import * as React from 'react';
import Title from '../general/Title';
import ApexCharts from 'react-apexcharts';
import { useState, useEffect } from 'react';
import { ConfigWithToken, ManagerBaseApi } from '../../auth/authConfig';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

export default function Chart() {
  const navigate = useNavigate();
  const [predictitems, setPredictItems] = useState([]);
  const predictItemsArray = Object.entries(predictitems);
  const config = ConfigWithToken();

  useEffect(() => {
    axios
      .get(`${ManagerBaseApi}/state/predict/food`, config)
      .then((res) => setPredictItems(res.data))
      .catch((err) => {
        err.response.status === 401 && navigate('/');
      });
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
        height={270}
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
