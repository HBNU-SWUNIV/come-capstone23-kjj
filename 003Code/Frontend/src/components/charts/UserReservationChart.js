import Title from '../general/Title';
import ApexCharts from 'react-apexcharts';
import { c_color } from '../../styles/global';
import Api_charts from '../../api/Api_charts';

const UserReservationChart = () => {
  const { reservationArray } = Api_charts();
  return (
    <>
      <>
        <Title>
          <span style={c_color}>금주 예약자</span>
        </Title>
        <ApexCharts
          type="line"
          series={[
            {
              name: '예약자 수',
              data: reservationArray?.map((a) => a[1]),
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
    </>
  );
};

export default UserReservationChart;
