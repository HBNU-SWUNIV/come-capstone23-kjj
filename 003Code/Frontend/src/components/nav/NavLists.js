import * as React from 'react';
import HomeIcon from '@mui/icons-material/Home';
import RestaurantMenuIcon from '@mui/icons-material/RestaurantMenu';
import RiceBowlIcon from '@mui/icons-material/RiceBowl';
import NavList from './NavList';
import styled from 'styled-components';
import CalendarMonthIcon from '@mui/icons-material/CalendarMonth';
import LogoutIcon from '@mui/icons-material/Logout';
import { Tooltip } from '@mui/material';
import UseNav from '../../hooks/UseNav';
import UseSize from '../../hooks/UseSize';

const Wrapper = styled.div`
  display: flex;
  flex-direction: column;

  gap: 20px;
  bos-sizing: border-box;
  padding-top: 30px;
`;

const Logout = styled.div`
  background-color: inherit;

  display: flex;
  justify-content: center;
  align-items: center;

  color: gray;
  cursor: pointer;

  position: absolute;

  top: ${({ $height }) => ($height ? $height + 'px' : '0')};
  right: 0;
  left: 0;
  margin: 0 auto;
`;

const listItems = [
  {
    url: '/home',
    icon: <HomeIcon />,
    name: '홈',
  },
  {
    url: '/menu',
    icon: <RestaurantMenuIcon />,
    name: '메뉴',
  },
  {
    url: '/dailymenu',
    icon: <RiceBowlIcon />,
    name: '오늘의 메뉴',
  },
  {
    url: '/dayoff',
    icon: <CalendarMonthIcon />,
    name: '휴일 설정',
  },
];

function NavLists() {
  const { onLogout } = UseNav();
  const { innerheight } = UseSize();

  const iconPosition = innerheight - 100;

  return (
    <Wrapper>
      {listItems.map((item, idx) => (
        <NavList key={idx} {...item} />
      ))}

      <Tooltip title="로그아웃">
        <Logout onClick={onLogout} $height={iconPosition}>
          <LogoutIcon />
        </Logout>
      </Tooltip>
    </Wrapper>
  );
}

export default NavLists;
