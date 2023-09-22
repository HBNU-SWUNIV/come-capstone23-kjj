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
        <ListItemText>
          <span style={ListItemTextStyle}>홈</span>
        </ListItemText>
      </ListItemButton>
      <ListItemButton onClick={() => navigate('/menu')}>
        <ListItemIcon>
          <RestaurantMenuIcon />
        </ListItemIcon>
        <ListItemText>
          <span style={ListItemTextStyle}>메뉴</span>
        </ListItemText>{' '}
      </ListItemButton>
      <ListItemButton onClick={() => navigate('/dailymenu')}>
        <ListItemIcon>
          <RiceBowlIcon />
        </ListItemIcon>
        <ListItemText>
          <span style={ListItemTextStyle}>오늘의 메뉴</span>
        </ListItemText>
      </ListItemButton>
      <ListItemButton onClick={() => navigate('/dayoff')}>
        <ListItemIcon>
          <SettingsIcon />
        </ListItemIcon>
        <ListItemText>
          <span style={ListItemTextStyle}>휴일 설정</span>
        </ListItemText>{' '}
      </ListItemButton>
    </React.Fragment>
  );
}

export default Main_Listitems;

const ListItemTextStyle = {
  fontWeight: '600',
  fontSize: '15px',
};
