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
import LoadingDots from '../general/LoadingDots';

const TodayMenuAddDialog = (props) => {
  const todaymenu_add_dialog_textfiled_datas = [
    {
      isDisabled: true,
      id: 'outlined-required',
      label: '오늘의 메뉴 이름은 고정입니다',
    },
    {
      isDisabled: true,
      id: 'outlined-required-two',
      label: '오늘의 메뉴 정보는 고정입니다.',
    },
    {
      id: 'outlined-number',
      label: '가격',
      type: 'number',
      placeholder: '가격',
      onblur: props.handleTodayMenuError,
      errorCondition: props.todayMenuError,
      inputref: props.menuCostRef,
    },
  ];

  return (
    <Dialog open={props.open} onClose={props.onClose}>
      <DialogTitle>오늘의메뉴 등록</DialogTitle>

      <DialogContent sx={dailymenuContentStyle}>
        <DialogContentText sx={{ fontWeight: 600 }}>
          이미지 파일을 추가하여 이미지를 등록해주세요.
        </DialogContentText>

        <div style={{ display: 'flex', alignItems: 'center' }}>
          {props.selectedImg ? (
            <img src={props.selectedImg} width={210} height={118} />
          ) : (
            <Skeleton variant="rectangular" width={210} height={118} />
          )}
          <input
            style={{ marginLeft: '2vw' }}
            type="file"
            accept="image/*"
            onChange={props.handleImageChange}
          />
        </div>

        {todaymenu_add_dialog_textfiled_datas.map((item) => (
          <>
            <TextField
              disabled={item.isDisabled}
              id={item.id}
              label={item.label}
              type={item.type}
              placeholder={item.placeholder}
              onBlur={item.onblur}
              inputRef={item.inputref}
            />
            {item.errorCondition && <ErrorInform message="가격은 필수입니다." />}
          </>
        ))}
      </DialogContent>

      <DialogActions>
        <Button sx={{ fontWeight: 600 }} onClick={props.addTodayMenu}>
          {props.isLoading ? <LoadingDots isGray={true} /> : '등록'}
        </Button>

        <Button sx={{ fontWeight: 600 }} color="error" onClick={props.onClose}>
          닫기
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default TodayMenuAddDialog;

const dailymenuContentStyle = {
  display: 'flex',
  flexDirection: 'column',
  gap: '1vh',
};
