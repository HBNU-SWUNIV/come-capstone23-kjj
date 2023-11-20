import ApexCharts from 'react-apexcharts';
import UseCalculateFood from '../../hooks/UseCalculateFood';
import Title from '../general/Title';

const FoodSavingStatusChart = () => {
  const { chartdata } = UseCalculateFood();

  return (
    <>
      <Title>식재료를 이정도나 절약했어요!</Title>
      <ApexCharts
        series={[
          {
            name: '서비스 기반 식재료 발주량',
            data: Object.values(chartdata?.next),
            type: 'area',
          },
          {
            name: `기존 식당 식재료 발주량`,
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
