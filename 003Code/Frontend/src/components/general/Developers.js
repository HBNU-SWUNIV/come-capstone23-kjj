import { styled as cStyled } from 'styled-components';
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
import { styled } from '@mui/material/styles';

const Developers = ({ onClose, open }) => {
  return (
    <BootstrapDialog
      onClose={onClose}
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
        <DeveloperTitle>Developer Information</DeveloperTitle>

        <DeveloperInfoWrapper>
          <User>
            <img src={Gibeom} />
            <Contact>
              <DeveloperName>Gibeom</DeveloperName>
              <DeveloperGithub>
                <RLink to={'https://github.com/kkb4363'} target="_blank">
                  <FaGithub />
                </RLink>
              </DeveloperGithub>
            </Contact>
            <DeveloperInfo>Web Frontend</DeveloperInfo>
          </User>

          <User>
            <img src={Heongmok} />
            <Contact>
              <DeveloperName>Heongmok</DeveloperName>
              <DeveloperGithub>
                <RLink to="https://github.com/HyeongMokJeong" target="_blank">
                  <FaGithub />
                </RLink>
              </DeveloperGithub>
            </Contact>
            <DeveloperInfo>Web&App Backend</DeveloperInfo>
          </User>

          <User>
            <img src={Seonghun} />
            <Contact>
              <DeveloperName>Seonghun</DeveloperName>
              <DeveloperGithub>
                <RLink to="https://github.com/joseonghoon" target="_blank">
                  <FaGithub />
                </RLink>
              </DeveloperGithub>
            </Contact>
            <DeveloperInfo>App Frontend</DeveloperInfo>
          </User>
        </DeveloperInfoWrapper>

        <span style={{ fontSize: '20px' }}>Capston Design 2023 KJJ Team</span>
      </DialogContent>
    </BootstrapDialog>
  );
};

export default Developers;

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

const DeveloperInfoWrapper = cStyled.div`
display:flex;
justify-content:space-around;
box-sizing:border-box;
align-items:center;
padding:50px 10px;
width: 600px;
height: 250px;
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

const DeveloperTitle = cStyled.span`
    font-size:30px;
    font-weight:600px;
`;

const DeveloperName = cStyled.span`
    font-size:20px;
    font-weight:600;
`;

const DeveloperInfo = cStyled.span`
    width:90%;
    font-size:14px;
    font-weight:500;
    margin-top:-10px;
`;

const DeveloperGithub = cStyled.span`
    font-size:24px;
`;
