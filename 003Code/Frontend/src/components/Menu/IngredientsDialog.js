import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import Input from '@mui/material/Input';
import Button from '@mui/material/Button';

const IngredientsContentStyle = {
  display: 'flex',
  flexDirection: 'column',
  gap: '1vh',
  overflow: 'auto',
  height: '40vh',
  minWidth: '45vw',
};

const IngredientsContentTextStyle = {
  marginTop: '-10px',
  marginBottom: '20px',
  fontSize: '12px',
};

const IngredientsDialog = ({
  open,
  onClose,
  ingredientsInputFields,
  handleRemoveFields,
  handleAddFields,
  addIngredients,
  ingredientsID,
  handleInputChange,
}) => {
  return (
    <Dialog open={open} onClose={onClose}>
      <DialogTitle>식재료 등록</DialogTitle>
      <DialogContent sx={IngredientsContentStyle}>
        <DialogContentText>식재료무게는 KG단위로 등록해주세요.</DialogContentText>
        <DialogContentText sx={IngredientsContentTextStyle}>
          식재료 정보를 수정하면 이전 정보는 덮어쓰입니다.
        </DialogContentText>
        {ingredientsInputFields.map((field, index) => (
          <div key={index}>
            <Input
              name="key"
              value={field.key}
              onChange={(e) => handleInputChange(index, e)}
              placeholder="식재료 이름"
            />
            <Input
              name="value"
              value={field.value}
              onChange={(e) => handleInputChange(index, e)}
              placeholder="식재료 무게"
              type="number"
            />
            <Button onClick={() => handleRemoveFields(index)}>삭제</Button>
          </div>
        ))}
      </DialogContent>
      <DialogActions>
        <Button onClick={handleAddFields}>식재료 추가하기</Button>
        <Button onClick={() => addIngredients(ingredientsID)}>등록</Button>
        <Button color="error" onClick={onClose}>
          닫기
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default IngredientsDialog;
