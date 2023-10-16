import { IconButton } from '@mui/material';
import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown';
import KeyboardArrowUpIcon from '@mui/icons-material/KeyboardArrowUp';

const List_Up_Down_Arrow = (props) => {
  return (
    <IconButton
      aria-label="expand row"
      size="small"
      onClick={() => props.onClick(props.open)}
    >
      {props.open ? <KeyboardArrowUpIcon /> : <KeyboardArrowDownIcon />}
    </IconButton>
  );
};

export default List_Up_Down_Arrow;
