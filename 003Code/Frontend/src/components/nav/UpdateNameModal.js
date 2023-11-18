import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  TextField,
} from '@mui/material';
import styled from 'styled-components';

const UpdateNameModal = (props) => {
  return (
    <Dialog open={props.open} onClose={props.onClose}>
      <UpdateNameModalLayout>
        <DialogTitle sx={DialogTitleStyle}>
          식당 이름 {props.isName ? '수정' : '설정'}
        </DialogTitle>

        <DialogContent>
          <DialogContentText sx={DialogTextStyle}>
            소비자도 쉽게 확인할 수 있는 방법으로 식당 이름을
            {props.isName ? '수정' : '설정'}
            해주세요
          </DialogContentText>

          <TextField
            sx={{ border: '1px solid white', backgroundColor: 'white' }}
            autoFocus
            margin="dense"
            id="nameRef"
            inputRef={props.nameRef}
            placeholder={props.isName ? props.name : ''}
            label={props.isName ? '기존 식당 이름' : '식당 이름'}
            fullWidth
          />
        </DialogContent>
      </UpdateNameModalLayout>

      <DialogActions sx={{ backgroundColor: '#24292e' }}>
        <Button onClick={props.onUpdateMarketName}>등록</Button>
        <Button color="error" onClick={props.onClose}>
          닫기
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default UpdateNameModal;

const UpdateNameModalLayout = styled.div`
  background-color: #24292e;
`;

const DialogTitleStyle = {
  margin: '0 auto',
  color: 'white',
  fontSize: '20px',
  fontWeight: '600',
};
const DialogTextStyle = {
  fontSize: '14px',
  fontWeight: '600',
  marginBottom: '10px',
  color: 'white',
  width: '500px',
};
