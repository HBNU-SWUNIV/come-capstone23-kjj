import ApexCharts from 'react-apexcharts';
import Api_charts from '../../api/Api_charts';
import { c_color } from '../../styles/global';
import Title from '../general/Title';

const FoodNeccesaryChart = () => {
  const { predictfoodsArray } = Api_charts();

  return (
    <>
      <Title>
        <span style={c_color}>익일 필요 식재료</span>
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
            width: 1,
            colors: ['#fff'],
          },
          plotOptions: {
            bar: {
              horizontal: false,
            },
          },
          xaxis: {
            categories: predictfoodsArray.map((a) => a[0]),
          },
          yaxis: {
            labels: {
              formatter: (val) => {
                return val + 'g';
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

export default FoodNeccesaryChart;
