import { Dialog, DialogActions, DialogContent, DialogContentText } from '@mui/material';
import ConfirmButton from './ConfirmButton';

const DeleteDialog = (props) => {
  return (
    <Dialog
      width="md"
      open={props.open}
      onClose={props.onClose}
      aria-labelledby="alert-dialog-title"
      aria-describedby="alert-dialog-description"
    >
      <DialogContent>
        <DialogContentText sx={deleteContentTextStyle} id="alert-dialog-description">
          정말 삭제하시겠습니까?
        </DialogContentText>
      </DialogContent>

      <DialogActions>
        <ConfirmButton
          sx={deleteButtonStyle}
          first_color="error"
          first_onClick={props.onDelete}
          last_onClick={props.onClose}
          first_text={'네'}
          last_text={'아니요'}
        />
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
