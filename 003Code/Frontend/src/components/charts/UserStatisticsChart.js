import * as React from 'react';
import Typography from '@mui/material/Typography';
import Title from '../general/Title';
import Divider from '@mui/material/Divider';
import { styled } from 'styled-components';
import { c_color } from '../../styles/global';
import Api_calculate_food from '../../api/Api_calculate_food';
import Api_charts from '../../api/Api_charts';

export default function UserStatisticsChart() {
  const { calculatedValue } = Api_calculate_food();
  const { todaypop, todaydates } = Api_charts();

  const userArray = [
    {
      id: 1,
      isAddDate: false,
      isAddBottomDivider: true,
      userPop: Math.ceil(calculatedValue) + 'kg',
      title: '금주 절약된 식재료',
    },
    {
      id: 0,
      isAddDate: true,
      isAddBottomDivider: false,
      userPop: todaypop,
      title: '금일 이용자',
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
            on {todaydates.day} {todaydates.month}, {todaydates.year}
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
