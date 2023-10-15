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
import SelectIngredients from './ingredients/SelectIngredients';

const weightFontStyle = {
  fontWeight: 600,
};

const MenuUpdateDialog = ({
  onClose,
  open,
  selectedImg,
  updateID,
  handleImageChange,
  setImage,
  menuNameRef,
  nameDuplicate,
  menuDetailsRef,
  menuCostRef,
  menuUpdate,
  selectedFood,
  setSelectedFood,
}) => {
  return (
    <Dialog open={open} onClose={onClose}>
      <DialogTitle sx={weightFontStyle}>메뉴 수정</DialogTitle>
      <DialogContent sx={{ display: 'flex', flexDirection: 'column', gap: '1vh' }}>
        {open === true ? (
          <div style={{ display: 'flex', alignItems: 'center' }}>
            {selectedImg ? (
              <img src={selectedImg} width={210} height={120} />
            ) : (
              <img
                width="210"
                height="120"
                src={'http://kjj.kjj.r-e.kr:8080/api/image?dir=' + updateID.image}
              />
            )}
            <input
              style={{ marginLeft: '2vw' }}
              type="file"
              accept="image/*"
              onChange={handleImageChange}
            />
          </div>
        ) : (
          <>
            <DialogContentText sx={weightFontStyle}>
              이미지 파일을 추가하여 이미지를 등록해주세요.
            </DialogContentText>
            <div style={{ display: 'flex', alignItems: 'center' }}>
              <Skeleton variant="rectangular" width={210} height={118} />
              <input
                style={{ marginLeft: '2vw' }}
                type="file"
                accept="image/*"
                onChange={(e) => setImage(e.target.files[0])}
              />
            </div>
          </>
        )}

        <TextField
          required
          inputRef={menuNameRef}
          id="outlined-required"
          placeholder={updateID?.name + ''}
        />

        {nameDuplicate && <ErrorInform message={'중복된 메뉴명이 있습니다'} />}

        <TextField
          inputRef={menuDetailsRef}
          required
          id="outlined-required2"
          placeholder={updateID?.details + ''}
        />
        <TextField
          inputRef={menuCostRef}
          placeholder={updateID?.cost + ''}
          id="outlined-number"
          label="가격"
          type="number"
        />

        <SelectIngredients selectedItem={selectedFood} setFn={setSelectedFood} />
      </DialogContent>
      <DialogActions>
        <Button sx={weightFontStyle} onClick={menuUpdate}>
          수정
        </Button>
        <Button sx={weightFontStyle} color="error" onClick={onClose}>
          닫기
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default MenuUpdateDialog;
