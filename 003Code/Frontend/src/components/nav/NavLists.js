import CalendarMonthIcon from '@mui/icons-material/CalendarMonth';
import HomeIcon from '@mui/icons-material/Home';
import LogoutIcon from '@mui/icons-material/Logout';
import RestaurantMenuIcon from '@mui/icons-material/RestaurantMenu';
import RiceBowlIcon from '@mui/icons-material/RiceBowl';
import { Tooltip } from '@mui/material';
import styled from 'styled-components';
import UseNav from '../../hooks/UseNav';
import UseSize from '../../hooks/UseSize';
import NavList from './NavList';
import { flexCenter, flexColumn } from '../../styles/global.style';

function NavLists() {
  const { onLogout } = UseNav();
  const { innerheight } = UseSize();

  const iconPosition = innerheight - 100;

  return (
    <NavListsLayout>
      {listItems.map((item, idx) => (
        <NavList key={idx} {...item} />
      ))}

      <Tooltip title="로그아웃">
        <LogoutBox onClick={onLogout} $height={iconPosition}>
          <LogoutIcon />
        </LogoutBox>
      </Tooltip>
    </NavListsLayout>
  );
}

export default NavLists;

const NavListsLayout = styled.div`
  ${flexColumn};
  box-sizing: border-box;
  padding-top: 30px;
  gap: 20px;
`;

const LogoutBox = styled.div`
  ${flexCenter};
  margin: 0 auto;
  position: absolute;
  top: ${({ $height }) => ($height ? $height + 'px' : '0')};
  right: 0;
  left: 0;
  cursor: pointer;
  background-color: inherit;

  color: gray;
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
