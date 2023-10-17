import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  TextField,
} from '@mui/material';

const DailyMenuInputDialogContentTextStyle = {
  marginBottom: '20px',
  color: '#FF385C',
  fontSize: '14px',
};

const TodayMenuInputDialog = ({
  open,
  onClose,
  dayId,
  dailyMenuInfo,
  dailyMenusInputHandler,
  onSaveDailyMenus,
}) => {
  return (
    <Dialog open={open} onClose={onClose}>
      <DialogTitle sx={{ margin: '0 auto' }}>
        Day {dayId && dayId?.substr(4, 2)}-{dayId && dayId?.substr(6, 2)}
      </DialogTitle>
      <DialogContent>
        <DialogContentText align="center" sx={DailyMenuInputDialogContentTextStyle}>
          식단 내용을 지우고 싶으시면 빈 내용으로 등록해주시면 됩니다
        </DialogContentText>
        <div>
          <TextField
            id="outlined-multiline-static"
            label="식단 예시"
            multiline
            disabled
            rows={5}
            defaultValue={`백미밥\n소고기미역국\n닭갈비\n잡채\n상추쌈&쌈장`}
          />
          <TextField
            id="outlined-multiline-static"
            label="식단"
            multiline
            value={dailyMenuInfo}
            onChange={dailyMenusInputHandler}
            rows={5}
            placeholder="여기에 입력해주세요."
          />
        </div>
      </DialogContent>
      <DialogActions>
        <Button
          onClick={() =>
            onSaveDailyMenus(dayId.slice(0, 4), dayId.slice(4, 6), dayId.slice(6, 8))
          }
        >
          등록
        </Button>
        <Button color="error" onClick={onClose}>
          닫기
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default TodayMenuInputDialog;
