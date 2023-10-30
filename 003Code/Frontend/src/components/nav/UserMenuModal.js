import { Menu, MenuItem } from '@mui/material';

const UserMenuModal = (props) => {
  const usermenulists = [
    {
      htmlforname: 'name',
      text: props.isName ? '식당 이름 수정' : '식당 이름 설정',
    },
    {
      htmlforname: 'info',
      text: '식당 소개 메시지 변경',
    },
    // {
    //   htmlforname: 'image',
    //   text: '식당 이미지 변경',
    // },
    {
      text: '로그아웃',
      onClick: props.onLogout,
    },
  ];

  return (
    <Menu
      id="fade-menu"
      MenuListProps={{
        'aria-labelledby': 'fade-button',
      }}
      anchorEl={props.anchorEl}
      open={props.open}
      onClose={props.onClose}
      TransitionComponent={props.Fade}
    >
      {usermenulists.map((item) => (
        <label
          key={item.text}
          htmlFor={item.htmlforname}
          onClick={item.onClick ? item.onClick : (e) => props.openUpdateModal(e)}
        >
          <MenuItem sx={MenuItemTextStyle}>{item.text}</MenuItem>
        </label>
      ))}
    </Menu>
  );
};

export default UserMenuModal;

const MenuItemTextStyle = {
  fontWeight: 600,
  fontSize: '14px',
  margin: '10px 0px',
  width: '200px',
};
