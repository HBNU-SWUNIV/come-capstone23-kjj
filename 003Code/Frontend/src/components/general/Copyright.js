import Typography from '@mui/material/Typography';
import { useState } from 'react';
import { styled as cStyled } from 'styled-components';
import Developers from '../../domains/developerInfos/Developers';
import { flexCenter } from '../../styles/global.style';

function Copyright(props) {
  const [open, setOpen] = useState(false);
  const handleClickOpen = () => {
    setOpen(true);
  };
  const handleClose = () => {
    setOpen(false);
  };

  return (
    <CopyrightLayout>
      <Typography variant="body2" color="text.secondary" align="center" {...props}>
        <span>Copyright © 2023. 식재료 절약단</span>
        <span> · </span>
        <CopyrightInfo onClick={handleClickOpen}>개발자 정보</CopyrightInfo>
        <span> · </span>
        <Developers onClose={handleClose} open={open} />
        <span>Capstone Design KJJ Team / 모비젠-송영관 멘토님 / 김차종 교수님</span>
      </Typography>
    </CopyrightLayout>
  );
}

export default Copyright;

const CopyrightLayout = cStyled.div`
  height:30px;
  ${flexCenter};
  position: relative;

  span{
    font-size:16px;
    font-weight:600;
  }
`;

const CopyrightInfo = cStyled.span`
  &:hover {
    cursor: pointer;
  }
  text-decoration: underline;
`;
