import CloseIcon from '@mui/icons-material/Close';
import Dialog from '@mui/material/Dialog';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import IconButton from '@mui/material/IconButton';
import { styled } from '@mui/material/styles';
import PropTypes from 'prop-types';
import { styled as cStyled } from 'styled-components';
import Gibeom from '../../image/gi.png';
import Heongmok from '../../image/hy.png';
import Seonghun from '../../image/se.png';
import Developer from './Developer';
import { flexICenter } from '../../styles/global.style';

const Developers = ({ onClose, open }) => {
  return (
    <BootstrapDialog
      onClose={onClose}
      aria-labelledby="customized-dialog-title"
      open={open}
    >
      <DialogContent dividers sx={developerContentStyle}>
        <DeveloperTitle>개발자 정보</DeveloperTitle>

        <DevelopersLayout>
          {developers.map((user, index) => (
            <Developer key={index} {...user} />
          ))}
        </DevelopersLayout>

        <span style={{ fontSize: '16px' }}>Capstone Design 2023 KJJ Team</span>
      </DialogContent>
    </BootstrapDialog>
  );
};

export default Developers;

const developers = [
  {
    name: '기범',
    github: 'https://github.com/kkb4363',
    imgsrc: Gibeom,
    position: 'Web Frontend',
  },
  {
    name: '형목',
    github: 'https://github.com/HyeongMokJeong',
    imgsrc: Heongmok,
    position: 'Web&App Backend',
  },
  {
    name: '성훈',
    github: 'https://github.com/joseonghoon',
    imgsrc: Seonghun,
    position: 'App Frontend',
  },
];

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

const developerContentStyle = {
  display: 'flex',
  flexDirection: 'column',
  alignItems: 'center',
  justifyContent: 'center',
};

const DevelopersLayout = cStyled.div`
width: 600px;
height: 250px;
${flexICenter};
justify-content:space-around;
box-sizing:border-box;
padding:50px 10px;
`;

const DeveloperTitle = cStyled.span`
  font-size:24px;
  font-weight:600px;
`;
