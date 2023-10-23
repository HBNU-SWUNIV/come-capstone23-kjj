import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  Skeleton,
} from '@mui/material';

const UpdateImgModal = (props) => {
  return (
    <Dialog open={props.open} onClose={props.onClose}>
      <DialogTitle sx={DialogTitleStyle}>식당 이미지 변경하기</DialogTitle>
      <DialogContent>
        <DialogContentText sx={DialogTextStyle}>현재 이미지</DialogContentText>
        <div>
          <div style={imgWrapperStyle}>
            {props.image !== null ? (
              <img
                style={{
                  width: '10vw',
                  minWidth: '10vw',
                }}
                src={`http://kjj.kjj.r-e.kr:8080/api/image?dir=` + props.image}
                alt="이미지없음"
              />
            ) : (
              <Skeleton variant="rectangular" width={210} height={118} />
            )}
            <input
              style={{ marginLeft: '2vw' }}
              type="file"
              accept="image/*"
              onChange={(e) => props.setNewImage(e.target.files[0])}
            />
          </div>
        </div>
      </DialogContent>

      <DialogActions>
        <Button onClick={props.onUpdateMarketImage}>등록</Button>
        <Button color="error" onClick={props.onClose}>
          닫기
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default UpdateImgModal;

const imgWrapperStyle = { display: 'flex', alignItems: 'center' };

const DialogTitleStyle = {
  margin: '0 auto',
  fontSize: '20px',
  fontWeight: '600',
};
const DialogTextStyle = {
  fontSize: '15px',
  fontWeight: '600',
  marginBottom: '10px',
};
