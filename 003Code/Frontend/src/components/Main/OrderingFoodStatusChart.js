import Title from '../general/Title';
import ApexCharts from 'react-apexcharts';
import { c_color } from './chartTitleColors';

const OrderingFoodStatusChart = () => {
  return (
    <>
      <Title>
        <span style={c_color}>식재료 발주 현황</span>
      </Title>
      <ApexCharts
        type="bar"
        series={[
          {
            name: '전월 식재료 발주량',
            data: [44, 55, 11, 66, 50, 25],
            group: 'prev',
          },
          {
            name: '금월 식재료 발주량',
            data: [24, 35, 5, 30, 25, 12],
            group: 'current',
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
            categories: ['당근', '양파', '마늘', '돼지고기', '닭고기', '소고기'],
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
          colors: ['#008ffb', '#00e396'],
          legend: {
            position: 'top',
            horizontalAlign: 'right',
          },
        }}
      />
    </>
  );
};

export default OrderingFoodStatusChart;
