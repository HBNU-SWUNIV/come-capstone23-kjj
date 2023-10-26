import ApexCharts from 'react-apexcharts';
import UseGetCharts from '../../hooks/UseGetCharts';
import { c_color } from '../../styles/global';
import Title from '../general/Title';

const FoodNeccesaryChart = () => {
  const { predictfoodsArray } = UseGetCharts();

  return (
    <>
      <Title>
        <span style={{ ...c_color, fontSize: '16px' }}>익일 필요 식재료</span>
      </Title>
      <ApexCharts
        type="bar"
        series={[
          {
            name: '무게(g)',
            data: predictfoodsArray.map((a) => a[1]),
          },
        ]}
        height={270}
        options={{
          chart: {
            toolbar: { show: false },
            stacked: true,
          },
          stroke: {
            width: 2,
            colors: ['#fff'],
          },
          plotOptions: {
            bar: {
              horizontal: false,
              borderRadius: 10,
              columnWidth: '80%',
            },
          },
          xaxis: {
            labels: {
              rotate: -35,
            },
            categories: predictfoodsArray.map((a) => a[0]),
            tickPlacement: 'on',
          },
          yaxis: {
            labels: {
              formatter: (val) => {
                return val + 'g';
              },
            },
          },
          fill: {
            // opacity: 1,
            type: 'gradient',
            gradient: {
              shade: 'light',
              type: 'horizontal',
              shadeIntensity: 0.25,
              gradientToColors: undefined,
              inverseColors: true,
              opacityFrom: 0.85,
              opacityTo: 0.85,
              stops: [50, 0, 100],
            },
          },
          colors: ['#8D95EB'],
        }}
      />
    </>
  );
};

export default FoodNeccesaryChart;
