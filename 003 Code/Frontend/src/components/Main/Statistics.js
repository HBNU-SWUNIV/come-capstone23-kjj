import * as React from 'react';
import Link from '@mui/material/Link';
import Typography from '@mui/material/Typography';
import Title from '../general/Title';
import { useState, useEffect } from 'react';
import Divider from '@mui/material/Divider';
import axios from 'axios';
import { ConfigWithToken, ManagerBaseApi } from '../../auth/authConfig';
import { useNavigate } from 'react-router-dom';
import { styled } from 'styled-components';
function preventDefault(event) {
  event.preventDefault();
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

export default function Statistics() {
  const navigate = useNavigate();
  const [Todaypop, setTodaypop] = useState(0);
  const [predictUsers, setPredictUsers] = useState(0);
  const config = ConfigWithToken();
  const today = new Date();
  const year = today.getFullYear();
  const month = today.toLocaleString('en-US', {
    month: 'long',
  });
  const day = today.getDate();

  React.useEffect(() => {
    axios
      .get(`${ManagerBaseApi}/state/today`, config)
      .then((res) => setTodaypop(res.data))
      .catch((err) => {
        err.response.status === 403 && navigate('/');
      });
    axios
      .get(`${ManagerBaseApi}/state/predict/user`, config)
      .then((res) => setPredictUsers(res.data))
      .catch((err) => console.log(err));
  }, []);

  return (
    <Wrapper>
      <Div>
        <Title>금일 이용자 수</Title>
        <Typography
          sx={{ fontSize: '2.5rem', marginBottom: '-1rem' }}
          component="p"
          variant="h6"
        >
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
        <Title>내일 예약자 수</Title>
        <Typography
          sx={{ fontSize: '2.5rem', marginBottom: '-1rem' }}
          component="p"
          variant="h6"
        >
          {predictUsers}명
        </Typography>
      </Div>
    </Wrapper>
  );
}
