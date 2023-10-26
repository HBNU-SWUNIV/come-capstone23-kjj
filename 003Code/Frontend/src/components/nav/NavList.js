import { ListItemButton, ListItemIcon } from '@mui/material';
import { useNavigate, useLocation } from 'react-router-dom';

const NavList = (props) => {
  const navigate = useNavigate();
  const path = useLocation().pathname;

  const isChecked = props.url == path;

  return (
    <ListItemButton onClick={() => navigate(`${props.url}`)}>
      <ListItemIcon sx={{ ...IconStyle, color: isChecked ? 'white' : 'gray' }}>
        {props.icon}
      </ListItemIcon>
    </ListItemButton>
  );
};

export default NavList;

const IconStyle = {
  marginLeft: '5px',
};
