import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  TextField,
} from '@mui/material';
import React from 'react';

const UpdateInfoModal = ({ open, onClose, info, InfoRef, onUpdateMarketInfo }) => {
  return (
    <Dialog open={open} onClose={onClose}>
      <DialogTitle sx={DialogTitleStyle}>식당 소개 메시지 변경</DialogTitle>
      <DialogContent>
        <DialogContentText sx={{ ...DialogTextStyle, marginBottom: '10px' }}>
          소개 메시지를 변경할 수 있습니다.
        </DialogContentText>
        <div>
          <TextField
            id="outlined-multiline-static"
            label="현재 식당 소개 메시지"
            multiline
            disabled
            rows={5}
            defaultValue={info}
          />
          <TextField
            sx={{ ml: '2vw' }}
            id="outlined-multiline-static"
            label="식당 소개 메시지 변경"
            multiline
            inputRef={InfoRef}
            rows={5}
            placeholder={info}
          />
        </div>
      </DialogContent>
      <DialogActions>
        <Button onClick={onUpdateMarketInfo}>등록</Button>
        <Button color="error" onClick={onClose}>
          닫기
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default UpdateInfoModal;

const DialogTitleStyle = {
  margin: '0 auto',
  fontFamily: 'Nanum',
  fontSize: '20px',
  fontWeight: '600',
};
const DialogTextStyle = {
  fontFamily: 'Nanum',
  fontSize: '15px',
  fontWeight: '600',
  marginBottom: '10px',
};
