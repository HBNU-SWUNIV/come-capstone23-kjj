import * as React from 'react';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import HomeIcon from '@mui/icons-material/Home';
import RestaurantMenuIcon from '@mui/icons-material/RestaurantMenu';
import RiceBowlIcon from '@mui/icons-material/RiceBowl';
import SettingsIcon from '@mui/icons-material/Settings';
import { useNavigate } from 'react-router-dom';

function Main_Listitems() {
  const navigate = useNavigate();
  return (
    <React.Fragment>
      <ListItemButton onClick={() => navigate('/home')}>
        <ListItemIcon>
          <HomeIcon />
        </ListItemIcon>
        <ListItemText primary="홈" />
      </ListItemButton>
      <ListItemButton onClick={() => navigate('/menu')}>
        <ListItemIcon>
          <RestaurantMenuIcon />
        </ListItemIcon>
        <ListItemText primary="메뉴" />
      </ListItemButton>
      <ListItemButton onClick={() => navigate('/dailymenu')}>
        <ListItemIcon>
          <RiceBowlIcon />
        </ListItemIcon>
        <ListItemText primary="오늘의메뉴" />
      </ListItemButton>
      <ListItemButton onClick={() => navigate('/dayoff')}>
        <ListItemIcon>
          <SettingsIcon />
        </ListItemIcon>
        <ListItemText primary="휴일설정" />
      </ListItemButton>
    </React.Fragment>
  );
}

export default Main_Listitems;
