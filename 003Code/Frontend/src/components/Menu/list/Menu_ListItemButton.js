import { ListItemButton, ListItemIcon, ListItemText } from '@mui/material';

const Menu_ListItemButton = (props) => {
  const { onClick, icon, text, isAllData, data } = props;

  const data_onClick = isAllData ? data : data.id;

  return (
    <ListItemButton onClick={() => onClick(data_onClick)}>
      <ListItemIcon>{icon}</ListItemIcon>
      <ListItemText primary={text} />
    </ListItemButton>
  );
};

export default Menu_ListItemButton;
