import Title from '../general/Title';
import ApexCharts from 'react-apexcharts';
import { ConfigWithToken, ManagerBaseApi } from '../../auth/authConfig';
import { useEffect, useState } from 'react';
import axios from 'axios';
import { c_color } from '../../styles/global';

const FoodSavingStatusChart = () => {
  const config = ConfigWithToken();
  const [marketname, setMarketname] = useState('');

  useEffect(() => {
    axios
      .get(`${ManagerBaseApi}/setting`, config)
      .then((res) => {
        setMarketname(res.data.name);
      })
      .catch((err) => {
        if (err.response.status === 403) console.log(err);
      });
  }, []);

  return (
    <>
      <Title>
        <span style={c_color}>식재료를 이정도나 절약했어요!</span>
      </Title>
      <ApexCharts
        type="line"
        series={[
          {
            name: '서비스 기반 식재료 발주량',
            data: [24, 35, 5, 30, 12],
            type: 'column',
          },
          {
            name: `기존 ${marketname} 식재료 발주량`,
            data: [44, 55, 11, 66, 25],
            type: 'line',
          },
        ]}
        height={270}
        options={{
          chart: {
            type: 'line',
            toolbar: { show: false },
            stacked: false,
          },
          zoom: {
            enabled: false,
          },
          stroke: {
            width: [1, 4],
            colors: ['#fff', '#FEB019'],
          },
          dataLabels: {
            enabled: true,
            enabledOnSeries: [1],
          },
          xaxis: {
            categories: ['월', '화', '수', '목', '금'],
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
          colors: ['#008FFB', '#FEB019'],
          legend: {
            position: 'top',
            horizontalAlign: 'right',
          },
        }}
      />
    </>
  );
};

export default FoodSavingStatusChart;
