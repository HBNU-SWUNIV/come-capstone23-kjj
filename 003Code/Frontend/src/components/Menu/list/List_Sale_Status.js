import Chip from '@mui/material/Chip';

const List_Sale_Status = (props) => {
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

export default List_Sale_Status;
