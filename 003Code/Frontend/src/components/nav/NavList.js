import { ListItemButton, ListItemIcon, ListItemText } from '@mui/material';
import React from 'react';
import { useNavigate } from 'react-router-dom';

const NavList = (props) => {
  const navigate = useNavigate();

  return (
    <ListItemButton onClick={() => navigate(`${props.url}`)}>
      <ListItemIcon>{props.icon}</ListItemIcon>
      <ListItemText>
        <span style={ListItemTextStyle}>{props.name}</span>
      </ListItemText>
    </ListItemButton>
  );
};

export default NavList;

const ListItemTextStyle = {
  fontWeight: '600',
  fontSize: '15px',
};
