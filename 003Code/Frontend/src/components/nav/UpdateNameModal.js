import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  TextField,
} from '@mui/material';

const UpdateNameModal = (props) => {
  return (
    <Dialog open={props.open} onClose={props.onClose}>
      <DialogTitle sx={DialogTitleStyle}>
        식당 이름 {props.isName ? '수정' : '설정'}
      </DialogTitle>
      <DialogContent>
        <DialogContentText sx={DialogTextStyle}>
          소비자도 쉽게 확인할 수 있는 방법으로 식당 이름을{' '}
          {props.isName ? '수정' : '설정'}
          해주세요
        </DialogContentText>
        <TextField
          autoFocus
          margin="dense"
          id="nameRef"
          inputRef={props.nameRef}
          placeholder={props.isName ? props.name : ''}
          label={props.isName ? '기존 식당 이름' : '식당 이름'}
          fullWidth
        />
      </DialogContent>

      <DialogActions>
        <Button onClick={props.onUpdateMarketName}>등록</Button>
        <Button color="error" onClick={props.onClose}>
          닫기
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default UpdateNameModal;

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
