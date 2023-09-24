import * as React from 'react';
import HomeIcon from '@mui/icons-material/Home';
import RestaurantMenuIcon from '@mui/icons-material/RestaurantMenu';
import RiceBowlIcon from '@mui/icons-material/RiceBowl';
import SettingsIcon from '@mui/icons-material/Settings';
import Main_List_item from './Main_List_item';

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

function Main_Listitems() {
  return (
    <>
      {listItems.map((item, idx) => (
        <Main_List_item key={idx} {...item} />
      ))}
    </>
  );
}

export default Main_Listitems;
