import React from 'react';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Skeleton from '@mui/material/Skeleton';
import ErrorInform from '../general/ErrorInform';

const weightFontStyle = {
  fontWeight: 600,
};

const MenuAddDialog = ({
  open,
  onClose,
  selectedImg,
  handleImageChange,
  menuNameRef,
  menuNameHandler,
  menuNameError,
  nameDuplicate,
  menuDetailsRef,
  menuInfoHandler,
  menuInfoError,
  menuCostRef,
  menuCostHandler,
  menuCostError,
  menuAdd,
}) => {
  return (
    <Dialog open={open} onClose={onClose}>
      <DialogTitle sx={weightFontStyle}>메뉴 등록</DialogTitle>
      <DialogContent sx={{ display: 'flex', flexDirection: 'column', gap: '1vh' }}>
        <DialogContentText sx={{ ...weightFontStyle, fontSize: '15px' }}>
          이미지 파일을 추가하여 이미지를 등록해주세요.
        </DialogContentText>

        <div style={{ display: 'flex', alignItems: 'center' }}>
          {selectedImg ? (
            <img src={selectedImg} width={210} height={118} />
          ) : (
            <Skeleton variant="rectangular" width={210} height={118} />
          )}
          <input
            style={{ marginLeft: '2vw' }}
            type="file"
            accept="image/*"
            onChange={handleImageChange}
          />
        </div>

        <TextField
          required
          inputRef={menuNameRef}
          id="outlined-required"
          label="required"
          placeholder="메뉴명"
          onBlur={menuNameHandler}
        />
        {menuNameError && <ErrorInform message="메뉴 명은 필수 입력입니다" />}
        {nameDuplicate && <ErrorInform message={'중복된 메뉴명이 있습니다'} />}
        <TextField
          inputRef={menuDetailsRef}
          required
          id="outlined-required2"
          label="required"
          placeholder="메뉴 소개"
          onBlur={menuInfoHandler}
        />
        {menuInfoError && <ErrorInform message={'메뉴 소개는 필수 입력입니다'} />}
        <TextField
          inputRef={menuCostRef}
          id="outlined-number"
          label="가격"
          type="number"
          required
          placeholder="가격 - 숫자만 입력해주세요."
          onBlur={menuCostHandler}
        />
        {menuCostError && <ErrorInform message="가격은 필수 입력입니다." />}
      </DialogContent>
      <DialogActions>
        <Button sx={weightFontStyle} onClick={menuAdd}>
          등록
        </Button>
        <Button sx={weightFontStyle} color="error" onClick={onClose}>
          닫기
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default MenuAddDialog;