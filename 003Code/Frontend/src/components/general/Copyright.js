import Typography from '@mui/material/Typography';
import { useState } from 'react';
import { styled as cStyled } from 'styled-components';
import Developers from '../../domains/developerInfos/Developers';

function Copyright(props) {
  const [open, setOpen] = useState(false);
  const handleClickOpen = () => {
    setOpen(true);
  };
  const handleClose = () => {
    setOpen(false);
  };

  return (
    <Wrapper>
      <Typography variant="body2" color="text.secondary" align="center" {...props}>
        {'Copyright © 2023. '}
        <span>식재료 절약단</span>
        <span> · </span>
        <Info onClick={handleClickOpen}>개발자 정보</Info>
        <span> · </span>
        <Developers onClose={handleClose} open={open} />
        <span>Capstone Design KJJ Team / 모비젠-송영관 멘토님 / 김차종 교수님</span>
      </Typography>
    </Wrapper>
  );
}

export default Copyright;

const Wrapper = cStyled.div`
  position: relative;
`;

const Info = cStyled.span`
  &:hover {
    cursor: pointer;
  }
  text-decoration: underline;
`;
