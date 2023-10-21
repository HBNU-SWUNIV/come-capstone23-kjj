import Title from '../general/Title';
import ApexCharts from 'react-apexcharts';
import { ConfigWithToken } from '../../auth/authConfig';
import { c_color } from '../../styles/global';
import Api_calculate_food from '../../api/Api_calculate_food';
import { useQuery } from 'react-query';
import { getMarketDetails } from '../../api/apis';

const FoodSavingStatusChart = () => {
  const config = ConfigWithToken();
  const { chartdata } = Api_calculate_food();

  const { data: marketinfo, isLoading } = useQuery(['market_name', config], () =>
    getMarketDetails(config)
  );

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
            data: Object.values(chartdata?.next),
            type: 'column',
          },
          {
            name: `기존 ${!isLoading && marketinfo.name} 식재료 발주량`,
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
