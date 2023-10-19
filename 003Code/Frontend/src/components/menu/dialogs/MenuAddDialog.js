import React, { useState } from 'react';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import Button from '@mui/material/Button';
import MenuUpdateAddInputs from '../MenuUpdateAddInputs';

const weightFontStyle = { fontWeight: 600 };
const dialogContentStyle = { display: 'flex', flexDirection: 'column', gap: '1vh' };
const dialogContentTextStyle = { ...weightFontStyle, fontSize: '15px' };

const MenuAddDialog = (props) => {
  const [validate, setValidate] = useState({
    name: false,
    details: false,
    cost: false,
  });

  const errorHandler = (e) => {
    const { name, value } = e.target;
    setValidate((prev) => ({
      ...prev,
      [name]: value === '',
    }));
  };

  const menu_add_input_datas = [
    {
      id: 'outlined-required',
      inputRef: props.menuNameRef,
      placeholder: '메뉴명',
      inputName: 'name',
      onBlur: errorHandler,
      item_name: '메뉴 명',
      duplicated_error: true,
    },
    {
      id: 'outlined-required2',
      inputRef: props.menuDetailsRef,
      placeholder: '메뉴 소개',
      inputName: 'details',
      onBlur: errorHandler,
      item_name: '메뉴 소개',
    },
    {
      id: 'outlined-number',
      inputRef: props.menuCostRef,
      placeholder: '가격 - 숫자만 입력해주세요',
      inputName: 'cost',
      onBlur: errorHandler,
      item_name: '가격',
      type: 'number',
    },
  ];

  return (
    <Dialog open={props.open} onClose={props.onClose}>
      <DialogTitle sx={weightFontStyle}>메뉴 등록</DialogTitle>
      <DialogContent sx={dialogContentStyle}>
        <DialogContentText sx={dialogContentTextStyle}>
          이미지 파일을 추가하여 이미지를 등록해주세요.
        </DialogContentText>

        <MenuUpdateAddInputs
          selectedImg={props.selectedImg}
          handleImageChange={props.handleImageChange}
          input_data={menu_add_input_datas}
          validateFn={validate}
          duplicateFn={props.nameDuplicate}
        />
      </DialogContent>

      <DialogActions>
        <Button sx={weightFontStyle} onClick={props.onAddMenu}>
          등록
        </Button>
        <Button sx={weightFontStyle} color="error" onClick={props.onClose}>
          닫기
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default MenuAddDialog;
