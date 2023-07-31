import React from 'react';
import Backdrop from '@mui/material/Backdrop';
import CircularProgress from '@mui/material/CircularProgress';

const Loading = () => {
  return (
    <Backdrop
      sx={{ backgroundColor: '#fff', zIndex: (theme) => theme.zIndex.drawer + 1 }}
      open={true}
    >
      <CircularProgress color="success" />
    </Backdrop>
  );
};
export default Loading;
