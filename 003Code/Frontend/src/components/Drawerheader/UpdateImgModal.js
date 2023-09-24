import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  Skeleton,
} from '@mui/material';

const UpdateImgModal = ({ open, onClose, image, setNewImage, onUpdateMarketImage }) => {
  return (
    <Dialog open={open} onClose={onClose}>
      <DialogTitle sx={DialogTitleStyle}>식당 이미지 변경하기</DialogTitle>
      <DialogContent>
        <DialogContentText sx={DialogTextStyle}>현재 이미지</DialogContentText>
        <div>
          <div style={imgWrapperStyle}>
            {image !== null ? (
              <img
                style={{
                  width: '10vw',
                  minWidth: '10vw',
                }}
                src={`http://kjj.kjj.r-e.kr:8080/api/image?dir=` + image}
                alt="이미지없음"
              />
            ) : (
              <Skeleton variant="rectangular" width={210} height={118} />
            )}
            <input
              style={{ marginLeft: '2vw' }}
              type="file"
              accept="image/*"
              onChange={(e) => setNewImage(e.target.files[0])}
            />
          </div>
        </div>
      </DialogContent>
      <DialogActions>
        <Button onClick={onUpdateMarketImage}>등록</Button>
        <Button color="error" onClick={onClose}>
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
