import Typography from '@mui/material/Typography';
import { useState } from 'react';
import { styled as cStyled } from 'styled-components';
import { styled } from '@mui/material/styles';
import Dialog from '@mui/material/Dialog';
import DialogTitle from '@mui/material/DialogTitle';
import DialogContent from '@mui/material/DialogContent';
import IconButton from '@mui/material/IconButton';
import CloseIcon from '@mui/icons-material/Close';
import PropTypes from 'prop-types';
import { FaGithub } from 'react-icons/fa';
import Gibeom from '../../image/gi.png';
import Heongmok from '../../image/hy.png';
import Seonghun from '../../image/se.png';
import { Link as RLink } from 'react-router-dom';

const Wrapper = cStyled.div`
  position: relative;
`;

const Info = cStyled.span`
  &:hover {
    cursor: pointer;
  }
  text-decoration: underline;
`;

const DeveloperInfoWrapper = cStyled.div`
  display:flex;
  justify-content:space-around;
  box-sizing:border-box;
  align-items:center;
  padding:50px 10px;
  width: 600px;
  height: 350px;
`;

const User = cStyled.div`
  width:30%;
  min-height:150px;
  display:flex;
  flex-direction:column;
  align-items:center;
  justify-content:center;
  img{
    width:130px;
    height:130px;
    border-radius:50%;
  }
`;

const Contact = cStyled.div`
  width:90%;
  display:flex;
  align-items:center;
  justify-content:space-between;
  a{
    color:black;
  }
`;

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

        <span>Capston Design KJJ Team / 모비젠-송영관 멘토님 / 김차종 교수님</span>
      </Typography>

      <BootstrapDialog
        onClose={handleClose}
        aria-labelledby="customized-dialog-title"
        open={open}
      >
        <DialogContent
          dividers
          sx={{
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
            justifyContent: 'center',
          }}
        >
          <span style={{ fontFamily: 'NotoSans', fontSize: '30px', fontWeight: '600' }}>
            Project
          </span>
          <span style={{ fontFamily: 'Cutefont', fontSize: '30px', fontWeight: '500' }}>
            음식물 쓰레기 감소를 위한 구내식당 사전 예약 시스템
          </span>

          <DeveloperInfoWrapper>
            <User>
              <img src={Gibeom} />
              <Contact>
                <span
                  style={{
                    fontSize: '30px',
                    fontWeight: '600',
                    fontFamily: 'Dongle',
                    marginTop: '2px',
                  }}
                >
                  Gibeom
                </span>
                <span
                  style={{
                    fontSize: '20px',
                  }}
                >
                  <RLink to={'https://github.com/kkb4363'}>
                    <FaGithub />
                  </RLink>
                </span>
              </Contact>
              <span
                style={{
                  marginTop: '-10px',
                  width: '90%',
                  fontFamily: 'Dongle',
                  fontSize: '22px',
                  fontWeight: '400',
                  lineHeight: '15px',
                }}
              >
                Web Frontend
              </span>
            </User>

            <User>
              <img src={Heongmok} />
              <Contact>
                <span
                  style={{
                    fontSize: '30px',
                    fontWeight: '600',
                    fontFamily: 'Dongle',
                    marginTop: '2px',
                  }}
                >
                  Heongmok
                </span>
                <span
                  style={{
                    fontSize: '20px',
                  }}
                >
                  <RLink to="https://github.com/HyeongMokJeong">
                    <FaGithub />
                  </RLink>
                </span>
              </Contact>
              <span
                style={{
                  marginTop: '-10px',
                  width: '90%',
                  fontFamily: 'Dongle',
                  fontSize: '22px',
                  fontWeight: '400',
                  lineHeight: '15px',
                }}
              >
                Web&App Backend
              </span>
            </User>

            <User>
              <img src={Seonghun} />
              <Contact>
                <span
                  style={{
                    fontSize: '30px',
                    fontWeight: '600',
                    fontFamily: 'Dongle',
                    marginTop: '2px',
                  }}
                >
                  Seonghun
                </span>
                <span
                  style={{
                    fontSize: '20px',
                  }}
                >
                  <RLink to="https://github.com/joseonghoon">
                    <FaGithub />
                  </RLink>
                </span>
              </Contact>
              <span
                style={{
                  marginTop: '-10px',
                  width: '90%',
                  fontFamily: 'Dongle',
                  fontSize: '22px',
                  fontWeight: '400',
                  lineHeight: '15px',
                }}
              >
                App Frontend
              </span>
            </User>
          </DeveloperInfoWrapper>

          <span style={{ fontFamily: 'Dongle', fontSize: '20px' }}>
            Capston Design 2023 KJJ Team
          </span>
        </DialogContent>
      </BootstrapDialog>
    </Wrapper>
  );
}

export default Copyright;

const BootstrapDialog = styled(Dialog)(({ theme }) => ({
  '& .MuiDialogContent-root': {
    padding: theme.spacing(2),
  },
  '& .MuiDialogActions-root': {
    padding: theme.spacing(1),
  },
}));

function BootstrapDialogTitle(props) {
  const { children, onClose, ...other } = props;

  return (
    <DialogTitle sx={{ m: 0, p: 2 }} {...other}>
      {children}
      {onClose ? (
        <IconButton
          aria-label="close"
          onClick={onClose}
          sx={{
            position: 'absolute',
            right: 8,
            top: 8,
            color: (theme) => theme.palette.grey[500],
          }}
        >
          <CloseIcon />
        </IconButton>
      ) : null}
    </DialogTitle>
  );
}

BootstrapDialogTitle.propTypes = {
  children: PropTypes.node,
  onClose: PropTypes.func.isRequired,
};
