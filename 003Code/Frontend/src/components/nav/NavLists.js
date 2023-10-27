import * as React from 'react';
import HomeIcon from '@mui/icons-material/Home';
import RestaurantMenuIcon from '@mui/icons-material/RestaurantMenu';
import RiceBowlIcon from '@mui/icons-material/RiceBowl';
import SettingsIcon from '@mui/icons-material/Settings';
import NavList from './NavList';
import styled from 'styled-components';

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
    icon: <SettingsIcon />,
    name: '휴일 설정',
  },
];

function NavLists() {
  return (
    <Wrapper>
      {listItems.map((item, idx) => (
        <NavList key={idx} {...item} />
      ))}
    </Wrapper>
  );
}

export default NavLists;

const Wrapper = styled.div`
  display: flex;
  flex-direction: column;

  gap: 20px;
  bos-sizing: border-box;
  padding-top: 30px;
`;
