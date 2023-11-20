import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  TextField,
} from '@mui/material';

const TodayMenuInputDialog = (props) => {
  const todaymenu_input_dialog_textfield_datas = [
    {
      label: '오늘의 메뉴 예시',
      isDisabled: true,
      defaultValue: `백미밥\n소고기미역국\n닭갈비\n잡채\n상추쌈&쌈장`,
    },
    {
      label: '오늘의 메뉴',
      value: props.dailyMenuInfo,
      onChange: props.dailyMenusInputHandler,
      placeholder: '여기에 입력해주세요',
    },
  ];

  return (
    <Dialog open={props.open} onClose={props.onClose}>
      <DialogTitle sx={{ margin: '0 auto' }}>
        Day {props.dayId && props.dayId?.substr(4, 2)}-
        {props.dayId && props.dayId?.substr(6, 2)}
      </DialogTitle>

      <DialogContent>
        <DialogContentText align="center" sx={DailyMenuInputDialogContentTextStyle}>
          식단 내용을 지우고 싶으시면 빈 내용으로 등록해주시면 됩니다
        </DialogContentText>

        <>
          {todaymenu_input_dialog_textfield_datas.map((item, idx) => (
            <TextField
              key={idx}
              id="outlined-multiline-static"
              label={item.label}
              multiline
              disabled={item.isDisabled}
              rows={5}
              defaultValue={item.defaultValue}
              value={item.value}
              onChange={item.onChange}
              placeholder={item.placeholder}
            />
          ))}
        </>
      </DialogContent>

      <DialogActions>
        <Button
          onClick={() =>
            props.onSaveDailyMenus(
              props.dayId.slice(0, 4),
              props.dayId.slice(4, 6),
              props.dayId.slice(6, 8)
            )
          }
        >
          등록
        </Button>

        <Button color="error" onClick={props.onClose}>
          닫기
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default TodayMenuInputDialog;

const DailyMenuInputDialogContentTextStyle = {
  marginBottom: '20px',
  color: '#FF385C',
  fontSize: '14px',
};
