import Title from '../general/Title';
import ApexCharts from 'react-apexcharts';
import { c_color } from './chartTitleColors';

const RevenueChart = () => {
  return (
    <>
      <Title>
        <span style={c_color}>금주 매출액</span>
      </Title>
      <ApexCharts
        type="line"
        series={[
          {
            name: '매출액',
            data: [111, 58, 117, 144, 135],
          },
        ]}
        height={300}
        options={{
          chart: {
            toolbar: { show: false },
            zoom: {
              enabled: false,
            },
          },
          dataLabels: {
            enabled: false,
          },
          stroke: {
            curve: 'straight',
          },
          xaxis: {
            categories: ['월', '화', '수', '목', '금'],
          },
        }}
      />
    </>
  );
};

export default RevenueChart;
