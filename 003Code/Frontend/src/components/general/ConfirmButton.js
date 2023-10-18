import { Button } from '@mui/material';

const ConfirmButton = (props) => {
  return (
    <>
      <Button sx={props.sx} color={props.first_color} onClick={props.first_onClick}>
        {props.first_text}
      </Button>
      <Button sx={props.sx} color={props.last_color} onClick={props.last_onClick}>
        {props.last_text}
      </Button>
    </>
  );
};

export default ConfirmButton;
