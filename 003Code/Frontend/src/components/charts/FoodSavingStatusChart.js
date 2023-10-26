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
        <span style={{ ...c_color, fontWeight: '600', fontSize: '16px' }}>
          식재료를 이정도나 절약했어요!
        </span>
      </Title>
      <ApexCharts
        type="line"
        series={[
          {
            name: '서비스 기반 식재료 발주량',
            data: Object.values(chartdata?.next),
            type: 'column',
          },
          {
            name: `기존 ${marketDetails?.name} 식재료 발주량`,
            data: Object.values(chartdata?.prev),
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
            curve: 'smooth',
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
