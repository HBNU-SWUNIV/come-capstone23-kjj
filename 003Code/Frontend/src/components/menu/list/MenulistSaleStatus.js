import Chip from '@mui/material/Chip';

const MenulistSaleStatus = (props) => {
  return (
    <>
      {props.condition ? (
        <div style={{ marginRight: '-5px' }}>
          <Chip label="판매중" color="success" />
        </div>
      ) : (
        <Chip label="품 절" color="error" />
      )}
    </>
  );
};

export default MenulistSaleStatus;
