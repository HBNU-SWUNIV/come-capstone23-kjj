import * as React from 'react';
import Typography from '@mui/material/Typography';
import Title from '../general/Title';
import { useState, useEffect } from 'react';
import Divider from '@mui/material/Divider';
import axios from 'axios';
import { ConfigWithToken, ManagerBaseApi } from '../../auth/authConfig';
import { styled } from 'styled-components';

export default function Statistics() {
  const [Todaypop, setTodaypop] = useState(0);
  const [predictUsers, setPredictUsers] = useState(0);
  const config = ConfigWithToken();
  const today = new Date();
  const year = today.getFullYear();
  const month = today.toLocaleString('en-US', {
    month: 'long',
  });
  const day = today.getDate();

  useEffect(() => {
    axios
      .get(`${ManagerBaseApi}/state/today`, config)
      .then((res) => setTodaypop(res.data))
      .catch((err) => {});
    axios
      .get(`${ManagerBaseApi}/state/predict/user`, config)
      .then((res) => setPredictUsers(res.data))
      .catch((err) => console.log(err));
  }, []);

  return (
    <Wrapper>
      <Div>
        <Title>
          <StatisticsTitle>금일 이용자 수</StatisticsTitle>
        </Title>
        <Typography sx={StatisticsTextStyle} component="p" variant="h6">
          {Todaypop}명
        </Typography>
      </Div>
      <Typography
        sx={{ position: 'relative', marginTop: '-4rem' }}
        color="text.secondary"
      >
        on {day} {month}, {year}
      </Typography>

      <Divider sx={{ width: '100%', height: '1rem' }} />

      <Div>
        <Title>
          <StatisticsTitle>내일 예약자 수</StatisticsTitle>
        </Title>
        <Typography sx={StatisticsTextStyle} component="p" variant="h6">
          {predictUsers}명
        </Typography>
      </Div>
    </Wrapper>
  );
}

const Wrapper = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  align-items: start;
  height: 100%;
`;

const Div = styled.div`
  display: flex;
  width: 100%;
  justify-content: space-between;
  align-items: center;
`;

const StatisticsTitle = styled.span`
  color: rgb(0, 171, 85);
`;

const StatisticsTextStyle = {
  fontSize: '2.5rem',
  marginBottom: '-1rem',
  whiteSpace: 'nowrap',
};
