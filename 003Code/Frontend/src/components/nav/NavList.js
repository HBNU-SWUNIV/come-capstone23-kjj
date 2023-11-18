import { ListItemButton, ListItemIcon, Tooltip } from '@mui/material';
import { useNavigate, useLocation } from 'react-router-dom';

const NavList = (props) => {
  const navigate = useNavigate();
  const path = useLocation().pathname;

  const isChecked = props.url == path;

  return (
    <ListItemButton onClick={() => navigate(`${props.url}`)}>
      <Tooltip title={props.name}>
        <ListItemIcon sx={{ ...IconStyle, color: isChecked ? 'white' : 'gray' }}>
          {props.icon}
        </ListItemIcon>
      </Tooltip>
    </ListItemButton>
  );
};

export default NavList;

const IconStyle = {
  marginLeft: '5px',
};
