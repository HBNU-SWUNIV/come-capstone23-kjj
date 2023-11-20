import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import axios from 'axios';
import { useEffect, useState } from 'react';
import { ConfigWithToken, ManagerBaseApi } from '../../../utils/utils';
import IngredientsTable from '../ingredients/MenuIngredientsTable';

const MenuIngredientsDialog = (props) => {
  const config = ConfigWithToken();
  const [data, setData] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [notSettinged, setNotSettinged] = useState(false);

  useEffect(() => {
    if (props.updateId !== '') {
      axios
        .get(`${ManagerBaseApi}/menu/food/${props.updateId}`, config)
        .then((res) => setData(res.data))
        .then(() => {
          setIsLoading(false);
          setNotSettinged(false);
        })
        .catch((err) => {
          console.log(err);
          setNotSettinged(true);
        });
    }
  }, [props.updateId]);
  const ingredientsArray = !isLoading && Object.entries(data?.food);

  return (
    <Dialog maxWidth={'lg'} open={props.open} onClose={props.onClose}>
      {!notSettinged ? (
        <>
          <DialogTitle>식재료 - {data?.name}</DialogTitle>
          <DialogContent sx={IngredientsContentStyle}>
            <IngredientsTable data={ingredientsArray} />
          </DialogContent>
        </>
      ) : (
        <>
          <DialogTitle>식재료가 등록되지 않았습니다.</DialogTitle>
        </>
      )}

      <DialogActions>
        <Button color="error" onClick={props.onClose}>
          닫기
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default MenuIngredientsDialog;

const IngredientsContentStyle = {
  display: 'flex',
  gap: '1vh',
  height: '40vh',
  minWidth: '45vw',
};
