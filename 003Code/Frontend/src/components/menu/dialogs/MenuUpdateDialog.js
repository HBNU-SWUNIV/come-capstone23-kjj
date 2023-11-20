import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import MenuUpdateAddInputs from '../MenuUpdateAddInputs';
import MenuSelectIngredients from '../ingredients/MenuSelectIngredients';

const MenuUpdateDialog = (props) => {
  const menu_update_input_datas = [
    {
      id: 'outlined-required',
      inputRef: props.menuNameRef,
      placeholder: props.updateID?.name + '',
      duplicated_error: true,
    },
    {
      id: 'outlined-required2',
      inputRef: props.menuDetailsRef,
      placeholder: props.updateID?.details + '',
    },
    {
      id: 'outlined-number',
      inputRef: props.menuCostRef,
      placeholder: props.updateID?.cost + '',
      type: 'number',
    },
  ];

  const menuUpdateAddInputsProps = {
    isUpdate: true,
    selectedImg: props.selectedImg,
    handleImageChange: props.handleImageChange,
    input_data: menu_update_input_datas,
    duplicateFn: props.nameDuplicate,
    prev_img: props.updateID?.image,
  };

  return (
    <Dialog open={props.open} onClose={props.onClose}>
      <DialogTitle sx={{ fontWeight: 600 }}>메뉴 수정</DialogTitle>
      <DialogContent sx={dialogContentStyle}>
        <MenuUpdateAddInputs {...menuUpdateAddInputsProps} />

        <MenuSelectIngredients
          selectedItem={props.selectedFood}
          setFn={props.setSelectedFood}
        />
      </DialogContent>

      <DialogActions>
        <Button sx={{ fontWeight: 600 }} onClick={props.onUpdateMenu}>
          수정
        </Button>
        <Button sx={{ fontWeight: 600 }} color="error" onClick={props.onClose}>
          닫기
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default MenuUpdateDialog;

const dialogContentStyle = { display: 'flex', flexDirection: 'column', gap: '1vh' };
