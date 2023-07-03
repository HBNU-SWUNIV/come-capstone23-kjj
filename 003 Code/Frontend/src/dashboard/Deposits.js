import * as React from 'react';
import Link from '@mui/material/Link';
import Typography from '@mui/material/Typography';
import Title from './Title';
import axios from 'axios';
import { useEffect, useState } from 'react';
import Divider from '@mui/material/Divider';

function preventDefault(event) {
  event.preventDefault();
}


export default function Deposits() {
  const [Todaypop, setTodaypop] = useState(0);
  const [predictpop, setPredictpop] = useState(0);

  useEffect(() => {
    axios.get('/api/manager/state/today').then(res => setTodaypop(res.data))
    axios.get(`/api/manager/state/predict/menu`).then(res => console.log(res))
    axios.get(`/api/manager/state/predict/user`).then(res => setPredictpop(res.data))
    })

    const today = new Date();
    const year = today.getFullYear();
    const options = { month: 'long' };
    const month = today.toLocaleString('en-US', options);
    const day = today.getDate();
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
        {predictpop}명
      </Typography>
      <div>
        <Link color="primary" href="#" onClick={preventDefault}>
          예약자 정보 보러가기
        </Link>
      </div>
    </React.Fragment>
  );
}
