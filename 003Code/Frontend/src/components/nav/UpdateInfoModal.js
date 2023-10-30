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

const UpdateInfoModal = (props) => {
  const updateinfomodal_textfield_data = [
    {
      label: '현재 식당 소개 메시지',
      isDisabled: true,
      defaultvalue: props.info,
    },
    {
      label: '식당 소개 메시지 변경',
      inputref: props.InfoRef,
      placeholder: props.info,
    },
  ];
  return (
    <Dialog open={props.open} onClose={props.onClose}>
      <Wrapper>
        <DialogTitle sx={DialogTitleStyle}>식당 소개 메시지 변경</DialogTitle>
        <DialogContent>
          <DialogContentText sx={{ ...DialogTextStyle, marginBottom: '10px' }}>
            소개 메시지를 변경할 수 있습니다.
          </DialogContentText>

          <TextfieldWrapper>
            {updateinfomodal_textfield_data.map((item, idx) => (
              <TextField
                sx={textfieldStyle}
                key={idx + 'updateinfomodalkey'}
                id="outlined-multiline-static"
                label={item.label}
                multiline
                disabled={item.isDisabled}
                rows={5}
                defaultValue={item.defaultvalue}
                inputRef={item.inputref}
                placeholder={item.placeholder}
              />
            ))}
          </TextfieldWrapper>
        </DialogContent>

        <DialogActions>
          <Button onClick={props.onUpdateMarketInfo}>등록</Button>
          <Button color="error" onClick={props.onClose}>
            닫기
          </Button>
        </DialogActions>
      </Wrapper>
    </Dialog>
  );
};

export default UpdateInfoModal;

const Wrapper = styled.div`
  background-color: #24292e;
  color: white;
`;
const TextfieldWrapper = styled.div`
  display: flex;
  gap: 20px;
`;
const textfieldStyle = {
  backgroundColor: 'white',
  border: '1px solid white',
};
const DialogTitleStyle = {
  margin: '0 auto',
  fontSize: '20px',
  fontWeight: '600',
  color: 'white',
};
const DialogTextStyle = {
  fontSize: '15px',
  fontWeight: '600',
  marginBottom: '10px',
  width: '500px',
  color: 'white',
};
