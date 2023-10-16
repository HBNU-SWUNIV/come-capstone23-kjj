import Title from '../general/Title';
import ApexCharts from 'react-apexcharts';
import { c_color } from './chartTitleColors';
import { ConfigWithToken } from '../../auth/authConfig';
import { useQuery } from 'react-query';
import { getReservation } from '../../api/apis';

const ReservationChart = () => {
  const config = ConfigWithToken();
  const { data: reservation, isLoading } = useQuery(['getReservation', config], () =>
    getReservation(config)
  );

  const reservationArray = !isLoading && Object.entries(reservation).slice(1);
  return (
    <>
      {!isLoading && (
        <>
          {' '}
          <Title>
            <span style={c_color}>금주 예약자</span>
          </Title>
          <ApexCharts
            type="line"
            series={[
              {
                name: '예약자 수',
                data: reservationArray.map((a) => a[1]),
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
      )}
    </>
  );
};

export default ReservationChart;
