import MoreVertIcon from '@mui/icons-material/MoreVert';
import List from '@mui/material/List';
import Menu from '@mui/material/Menu';
import styled from 'styled-components';
import MenulistButton from './MenulistButton';

const MenulistMenusbar = (props) => {
  return (
    <>
      <MenulistMenusbarLayout onClick={props.onClick}>
        <MoreVertIcon />
      </MenulistMenusbarLayout>

      <Menu anchorEl={props.anchorEl} open={props.open} onClose={props.onClose}>
        <List sx={menuListStyle} component="nav" aria-labelledby="nested-list-subheader">
          {props.listData.map(
            (item) =>
              item.condition && (
                <MenulistButton
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

export default MenulistMenusbar;

const menuListStyle = {
  width: '170px',
  maxWidth: '180px',
  height: '150px',
  whiteSpace: 'nowrap',
  display: 'flex',
  flexDirection: 'column',
};

const MenulistMenusbarLayout = styled.div`
  position: relative;

  &:hover {
    cursor: pointer;
  }
`;
