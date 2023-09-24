import { ListItemButton, ListItemIcon, ListItemText } from '@mui/material';
import React from 'react';
import { useNavigate } from 'react-router-dom';

const Main_List_item = (items) => {
  const navigate = useNavigate();
  const { url, icon, name } = items;
  return (
    <ListItemButton onClick={() => navigate(`${url}`)}>
      <ListItemIcon>{icon}</ListItemIcon>
      <ListItemText>
        <span style={ListItemTextStyle}>{name}</span>
      </ListItemText>
    </ListItemButton>
  );
};

export default Main_List_item;

const ListItemTextStyle = {
  fontWeight: '600',
  fontSize: '15px',
};
