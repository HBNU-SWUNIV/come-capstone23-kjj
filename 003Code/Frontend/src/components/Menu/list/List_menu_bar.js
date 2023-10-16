import styled from 'styled-components';
import MoreVertIcon from '@mui/icons-material/MoreVert';
import Menu from '@mui/material/Menu';
import List from '@mui/material/List';
import Menu_ListItemButton from './Menu_ListItemButton';

const menuListStyle = {
  width: '170px',
  maxWidth: '180px',
  height: '150px',
  whiteSpace: 'nowrap',
  display: 'flex',
  flexDirection: 'column',
};

const Wrapper = styled.div`
  position: relative;

  &:hover {
    cursor: pointer;
  }
`;

const List_menu_bar = (props) => {
  return (
    <>
      <Wrapper onClick={props.onClick}>
        <MoreVertIcon />
      </Wrapper>

      <Menu anchorEl={props.anchorEl} open={props.open} onClose={props.onClose}>
        <List sx={menuListStyle} component="nav" aria-labelledby="nested-list-subheader">
          {props.listData.map(
            (item) =>
              item.condition && (
                <Menu_ListItemButton
                  key={item.id}
                  isAllData={item.isAllData}
                  onClick={item.onClick}
                  icon={item.icon}
                  text={item.text}
                  data={item.data}
                />
              )
          )}
        </List>
      </Menu>
    </>
  );
};

export default List_menu_bar;
