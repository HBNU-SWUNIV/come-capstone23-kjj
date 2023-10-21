import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  TextField,
} from '@mui/material';

const UpdateInfoModal = (props) => {
  return (
    <Dialog open={props.open} onClose={props.onClose}>
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
            defaultValue={props.info}
          />
          <TextField
            sx={{ ml: '2vw' }}
            id="outlined-multiline-static"
            label="식당 소개 메시지 변경"
            multiline
            inputRef={props.InfoRef}
            rows={5}
            placeholder={props.info}
          />
        </div>
      </DialogContent>

      <DialogActions>
        <Button onClick={props.onUpdateMarketInfo}>등록</Button>
        <Button color="error" onClick={props.onClose}>
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
