import Title from '../general/Title';
import ApexCharts from 'react-apexcharts';
import { c_color } from './chartTitleColors';
import { useEffect, useState } from 'react';
import axios from 'axios';
import { ConfigWithToken, ManagerBaseApi } from '../../auth/authConfig';

const NeedFoodChart = () => {
  const [predict, setPredict] = useState([]);
  const config = ConfigWithToken();
  const predictArray = Object.entries(predict);
  console.log(predict);
  useEffect(() => {
    axios
      .get(`${ManagerBaseApi}/state/predict/food`, config)
      .then((res) => setPredict(res.data))
      .catch((err) => {
        if (err.response.status === 403) {
        }
      });
  }, []);
  return (
    <>
      <Title>
        <span style={c_color}>익일 필요 식재료</span>
      </Title>
      <ApexCharts
        type="bar"
        series={[
          {
            name: '무게',
            data: predictArray.map((a) => a[1]),
          },
        ]}
        height={270}
        options={{
          chart: {
            toolbar: { show: false },
            stacked: true,
          },
          stroke: {
            width: 1,
            colors: ['#fff'],
          },
          plotOptions: {
            bar: {
              horizontal: false,
            },
          },
          xaxis: {
            categories: predictArray.map((a) => a[0]),
          },
          yaxis: {
            labels: {
              formatter: (val) => {
                return val + 'kg';
              },
            },
          },
          fill: {
            opacity: 1,
          },
          colors: ['#8D95EB'],
        }}
      />
    </>
  );
};

export default NeedFoodChart;
