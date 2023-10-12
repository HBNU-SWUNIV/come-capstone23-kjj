import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
} from '@mui/material';
import React from 'react';

const DeleteDialog = ({ open, onClose, onDelete }) => {
  return (
    <Dialog
      width="md"
      open={open}
      onClose={onClose}
      aria-labelledby="alert-dialog-title"
      aria-describedby="alert-dialog-description"
    >
      <DialogContent>
        <DialogContentText sx={deleteContentTextStyle} id="alert-dialog-description">
          정말 삭제하시겠습니까?
        </DialogContentText>
      </DialogContent>
      <DialogActions>
        <Button sx={deleteButtonStyle} color="error" onClick={onDelete}>
          네
        </Button>
        <Button sx={deleteButtonStyle} onClick={onClose} autoFocus>
          아니요
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default DeleteDialog;

const deleteContentTextStyle = {
  width: '350px',
  fontSize: '20px',
  fontWeight: 600,
};

const deleteButtonStyle = {
  fontWeight: 500,
  fontSize: '18px',
};
