import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  Skeleton,
  TextField,
} from '@mui/material';
import ErrorInform from '../general/ErrorInform';

const weightFontStyle = {
  fontWeight: 600,
};

const dailymenuContentStyle = {
  display: 'flex',
  flexDirection: 'column',
  gap: '1vh',
};

const TodayMenuAddDialog = ({
  open,
  onClose,
  menuCostRef,
  handleTodayMenuError,
  todayMenuError,
  addTodayMenu,
  selectedImg,
  handleImageChange,
}) => {
  return (
    <Dialog open={open} onClose={onClose}>
      <DialogTitle>오늘의메뉴 등록</DialogTitle>
      <DialogContent sx={dailymenuContentStyle}>
        <DialogContentText sx={weightFontStyle}>
          이미지 파일을 추가하여 이미지를 등록해주세요.
        </DialogContentText>

        <div style={{ display: 'flex', alignItems: 'center' }}>
          {selectedImg ? (
            <img src={selectedImg} width={210} height={118} />
          ) : (
            <Skeleton variant="rectangular" width={210} height={118} />
          )}
          <input
            style={{ marginLeft: '2vw' }}
            type="file"
            accept="image/*"
            onChange={handleImageChange}
          />
        </div>

        <TextField
          disabled
          id="outlined-required"
          label="오늘의 메뉴 이름은 고정입니다."
        />
        <TextField
          disabled
          id="outlined-required2"
          label="오늘의 메뉴 정보는 고정입니다."
        />
        <TextField
          inputRef={menuCostRef}
          id="outlined-number"
          label="가격"
          type="number"
          placeholder="가격"
          onBlur={handleTodayMenuError}
        />
        {todayMenuError && <ErrorInform message="가격은 필수입니다." />}
      </DialogContent>
      <DialogActions>
        <Button sx={weightFontStyle} onClick={addTodayMenu}>
          등록
        </Button>
        <Button sx={weightFontStyle} color="error" onClick={onClose}>
          닫기
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default TodayMenuAddDialog;
