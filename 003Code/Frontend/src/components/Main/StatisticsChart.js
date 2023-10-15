import * as React from 'react';
import Typography from '@mui/material/Typography';
import Title from '../general/Title';
import { useState, useEffect } from 'react';
import Divider from '@mui/material/Divider';
import axios from 'axios';
import { ConfigWithToken, ManagerBaseApi } from '../../auth/authConfig';
import { styled } from 'styled-components';
import { c_color } from './chartTitleColors';

export default function StatisticsChart() {
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

  const userArray = [
    {
      id: 0,
      isAddDate: true,
      isAddBottomDivider: true,
      userPop: Todaypop,
      title: '금일 이용자',
    },
    {
      id: 1,
      isAddDate: false,
      isAddBottomDivider: false,
      userPop: '90kg',
      title: '금주 절약된 식재료',
    },
  ];

  const ShowUser = ({ isAddDate, isAddBottomDivider, userPop, title }) => {
    return (
      <>
        <Div>
          <Title>
            <span style={c_color}>{title}</span>
          </Title>
          <Typography sx={StatisticsTextStyle} component="p" variant="h6">
            {userPop}
            {isAddDate && '명'}
          </Typography>
        </Div>

        {isAddDate && (
          <Typography
            sx={{ position: 'relative', marginTop: '-4rem' }}
            color="text.secondary"
          >
            on {day} {month}, {year}
          </Typography>
        )}
        {isAddBottomDivider && <Divider sx={{ width: '100%', height: '1rem' }} />}
      </>
    );
  };

  return (
    <Wrapper>
      {userArray.map((item) => (
        <ShowUser
          key={item.id}
          isAddDate={item.isAddDate}
          isAddBottomDivider={item.isAddBottomDivider}
          userPop={item.userPop}
          title={item.title}
        />
      ))}
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

const StatisticsTextStyle = {
  fontSize: '2.5rem',
  marginBottom: '-1rem',
  whiteSpace: 'nowrap',
};
