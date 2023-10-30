import Title from '../general/Title';
import ApexCharts from 'react-apexcharts';
import { c_color } from '../../styles/global';
import UseNavApi from '../../hooks/UseNavApi';
import UseCalculateFood from '../../hooks/UseCalculateFood';

const FoodSavingStatusChart = () => {
  const { chartdata } = UseCalculateFood();
  const { marketDetails } = UseNavApi();

  return (
    <>
      <Title>
        <span style={{ fontWeight: '600', fontSize: '16px', ...c_color }}>
          식재료를 이정도나 절약했어요!
        </span>
      </Title>
      <ApexCharts
        series={[
          {
            name: '서비스 기반 식재료 발주량',
            data: Object.values(chartdata?.next),
            type: 'area',
          },
          {
            name: `기존 ${marketDetails?.name} 식재료 발주량`,
            data: Object.values(chartdata?.prev),
            type: 'line',
          },
        ]}
        height={330}
        options={{
          chart: {
            type: 'line',
          },
          stroke: {
            curve: 'smooth',
          },
          fill: {
            type: 'solid',
            opacity: [0.35, 1],
          },
          labels: ['월', '화', '수', '목', '금'],
          tooltip: {
            y: {
              formatter: function (y) {
                if (typeof y !== undefined) {
                  return y.toFixed(0) + 'kg';
                }
                return y;
              },
            },
          },
          yaxis: {
            labels: {
              formatter: (value) => {
                return value.toFixed(0);
              },
            },
          },
        }}
      />
    </>
  );
};

export default FoodSavingStatusChart;
