import { Divider, Menu, MenuItem } from '@mui/material';
import React from 'react';

const UserMenuModal = ({
  anchorEl,
  open,
  onClose,
  Fade,
  openUpdateNameModal,
  isName,
  openUpdateInfoModal,
  openUpdateImageModal,
  onLogout,
}) => {
  return (
    <Menu
      id="fade-menu"
      MenuListProps={{
        'aria-labelledby': 'fade-button',
      }}
      anchorEl={anchorEl}
      open={open}
      onClose={onClose}
      TransitionComponent={Fade}
    >
      <MenuItem sx={MenuItemTextStyle} onClick={openUpdateNameModal}>
        식당 이름 {isName ? '수정' : '설정'}
      </MenuItem>
      <MenuItem
        sx={{ ...MenuItemTextStyle, width: '250px' }}
        onClick={openUpdateInfoModal}
      >
        식당 소개 메시지 변경
      </MenuItem>
      <MenuItem sx={MenuItemTextStyle} onClick={openUpdateImageModal}>
        식당 이미지 변경
      </MenuItem>
      <Divider />
      <MenuItem sx={MenuItemTextStyle} onClick={onLogout}>
        로그아웃
      </MenuItem>
    </Menu>
  );
};

export default UserMenuModal;

const MenuItemTextStyle = {
  fontFamily: 'Nanum',
  fontWeight: 500,
  margin: '10px 0px',
};
