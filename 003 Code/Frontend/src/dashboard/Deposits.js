import * as React from 'react';
import Link from '@mui/material/Link';
import Typography from '@mui/material/Typography';
import Title from './Title';
import { useState, useEffect } from 'react';
import Divider from '@mui/material/Divider';
import axios from 'axios';
import { ConfigWithToken, ManagerBaseApi } from '../authConfig';

function preventDefault(event) {
  event.preventDefault();
}

export default function Deposits() {
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
      .then((res) => setTodaypop(res.data));
    axios
      .get(`${ManagerBaseApi}/state/predict/user`, config)
      .then((res) => setPredictUsers(res.data));
  }, []);

  return (
    <React.Fragment>
      <Title>금일 이용자 수</Title>
      <Typography component="p" variant="h4">
        {Todaypop}명
      </Typography>
      <Typography color="text.secondary" sx={{ flex: 1 }}>
        on {day} {month}, {year}
      </Typography>
      <Divider />
      <Title>내일 예약자 수</Title>
      <Typography component="p" variant="h4">
        {predictUsers}명
      </Typography>
      <div>
        <Link color="primary" href="#" onClick={preventDefault}>
          예약자 정보 보러가기
        </Link>
      </div>
    </React.Fragment>
  );
}
