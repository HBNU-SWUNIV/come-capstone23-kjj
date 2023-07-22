import * as React from 'react';
import MenuIcon from '@mui/icons-material/Menu';
import ChevronLeftIcon from '@mui/icons-material/ChevronLeft';
import MuiDrawer from '@mui/material/Drawer';
import MuiAppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import List from '@mui/material/List';
import { styled } from '@mui/material/styles';
import Divider from '@mui/material/Divider';
import IconButton from '@mui/material/IconButton';
import Main_Listitems from '../dashboard/Main_Listitems';
import Typography from '@mui/material/Typography';
import Menu from '@mui/material/Menu';
import MenuItem from '@mui/material/MenuItem';
import Fade from '@mui/material/Fade';
import { useNavigate } from 'react-router-dom';
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import Button from '@mui/material/Button';
import axios from 'axios';
import { useState, useEffect } from 'react';
import Skeleton from '@mui/material/Skeleton';
import Snackbar from '@mui/material/Snackbar';
import MuiAlert from '@mui/material/Alert';
import { ConfigWithToken, ManagerBaseApi } from '../authConfig';
import { useCookies } from 'react-cookie';

const drawerWidth = 240;
const Alert = React.forwardRef(function Alert(props, ref) {
  return <MuiAlert elevation={6} ref={ref} variant="filled" {...props} />;
});

const AppBar = styled(MuiAppBar, {
  shouldForwardProp: (prop) => prop !== 'open',
})(({ theme, open }) => ({
  zIndex: theme.zIndex.drawer + 1,
  transition: theme.transitions.create(['width', 'margin'], {
    easing: theme.transitions.easing.sharp,
    duration: theme.transitions.duration.leavingScreen,
  }),
  ...(open && {
    marginLeft: drawerWidth,
    width: `calc(100% - ${drawerWidth}px)`,
    transition: theme.transitions.create(['width', 'margin'], {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.enteringScreen,
    }),
  }),
}));

const Drawer = styled(MuiDrawer, { shouldForwardProp: (prop) => prop !== 'open' })(
  ({ theme, open }) => ({
    '& .MuiDrawer-paper': {
      position: 'relative',
      whiteSpace: 'nowrap',
      width: drawerWidth,
      transition: theme.transitions.create('width', {
        easing: theme.transitions.easing.sharp,
        duration: theme.transitions.duration.enteringScreen,
      }),
      boxSizing: 'border-box',
      ...(!open && {
        overflowX: 'hidden',
        transition: theme.transitions.create('width', {
          easing: theme.transitions.easing.sharp,
          duration: theme.transitions.duration.leavingScreen,
        }),
        width: theme.spacing(7),
        [theme.breakpoints.up('sm')]: {
          width: theme.spacing(9),
        },
      }),
    },
  })
);

function Drawerheader(props) {
  const [cookies] = useCookies();
  const [success, setSuccess] = useState(false);
  const [info, setMarketInfo] = useState('');
  const [image, setImage] = useState('');
  const [newImage, setNewImage] = useState([]);
  const navigate = useNavigate();
  const [anchorEl, setAnchorEl] = useState(null);
  const open = Boolean(anchorEl);
  const [open1, setOpen] = useState(true);
  const [openModal, setOpenModal] = useState(false);
  const [openModal2, setOpenModal2] = useState(false);
  const config = ConfigWithToken();
  const formdataConfig = {
    headers: {
      'Content-Type': 'multipart/form-data',
      ...config.headers,
    },
  };

  useEffect(() => {
    axios.get('/api/user/store', config).then((res) => setMarketInfo(res.data.info));
    axios
      .get(`${ManagerBaseApi}/setting`, config)
      .then((res) => setImage(res.data.image));
  }, []);

  const handleSuccessOpen = () => {
    setSuccess(true);
  };
  const handleSuccessClose = (event, reason) => {
    if (reason === 'clickaway') {
      return;
    }
    setSuccess(false);
  };
  const onInfo = (e) => {
    setMarketInfo(e);
  };
  const onChangeMarketInfo = () => {
    let body = {
      info,
    };
    axios
      .patch(`${ManagerBaseApi}/store/info`, body, config)
      .then((res) => res.status === 200 && handleCloseModal());
  };
  const handleClick = (event) => {
    setAnchorEl(event.currentTarget);
  };
  const handleClose = () => {
    setAnchorEl(null);
  };
  const toggleDrawer = () => {
    setOpen(!open1);
  };
  const handleClickOpenModal = () => {
    setOpenModal(true);
    handleClose();
  };
  const handleCloseModal = () => {
    setOpenModal(false);
  };
  const handleClickOpenModal2 = () => {
    setOpenModal2(true);
    handleClose();
  };
  const handleCloseModal2 = () => {
    setOpenModal2(false);
  };
  const onUpdateImage = () => {
    const formdata = new FormData();
    newImage !== null && formdata.append('file', newImage);

    axios({
      method: 'POST',
      url: `${ManagerBaseApi}/image`,
      data: formdata,
      ...formdataConfig,
    }).then((res) => {
      if (res.status === 200) {
        axios
          .get(`${ManagerBaseApi}/setting`, config)
          .then((res) => setImage(res.data.image));
      }
    });
    handleSuccessOpen();
    handleCloseModal2();
  };
  return (
    <>
      <AppBar position="absolute" open={open1}>
        <Toolbar
          sx={{
            pr: '24px', // keep right padding when drawer closed
          }}
        >
          <IconButton
            edge="start"
            color="inherit"
            aria-label="open drawer"
            onClick={toggleDrawer}
            sx={{
              marginRight: '36px',
              ...(open1 && { display: 'none' }),
            }}
          >
            <MenuIcon />
          </IconButton>
          <Typography
            component="h1"
            variant="h6"
            color="inherit"
            noWrap
            sx={{ flexGrow: 1 }}
          >
            {props?.pages}
          </Typography>
          <IconButton
            color="inherit"
            id="fade-button"
            aria-controls={open ? 'fade-menu' : undefined}
            aria-haspopup="true"
            aria-expanded={open ? 'true' : undefined}
            onClick={handleClick}
          >
            <div
              style={{
                display: 'flex',
                flexDirection: 'column',
                justifyContent: 'center',
                alignItems: 'center',
              }}
            >
              <span
                style={{
                  marginLeft: '5vw',
                  fontSize: '20px',
                  fontWeight: '500',
                  color: 'inherit',
                }}
              >
                관리자
              </span>
              <div
                style={{
                  display: 'flex',
                  justifyContent: 'flex-end',
                  alignItems: 'center',
                  width: '20vw',
                  height: '3vh',
                  marginLeft: '-7vw',
                }}
              >
                <span
                  style={{
                    fontSize: '18px',
                    fontWeight: '600',
                    color: 'inherit',
                    textDecoration: 'underline',
                    textDecorationThickness: '2px',
                    whiteSpace: 'nowrap',
                    textAlign: 'right',
                  }}
                >
                  한밭대학교 구내식당
                </span>
                <span
                  style={{
                    fontSize: '15px',
                    color: 'inherit',
                    marginLeft: '3px',
                    marginTop: '2px',
                  }}
                >
                  님
                </span>
              </div>
            </div>
          </IconButton>
        </Toolbar>
      </AppBar>

      <Drawer variant="permanent" open={open1}>
        <Toolbar
          sx={{
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'flex-end',
            px: [1],
          }}
        >
          <IconButton onClick={toggleDrawer}>
            <ChevronLeftIcon />
          </IconButton>
        </Toolbar>
        <Divider />

        <List component="nav">
          <div
            style={{
              width: '15vw',
              height: '20vh',
              display: 'flex',
              flexDirection: 'column',
              alignItems: 'center',
              justifyContent: 'center',
              marginTop: '-20px',
            }}
          >
            <img
              style={{
                width: '5vw',
              }}
              src={
                `http://kjj.kjj.r-e.kr:8080/api/image?token=${cookies.accesstoken}?dir=` +
                image
              }
              alt="이미지없음"
            />
            <span
              style={{
                fontSize: '25px',
                color: '#0a376e',
                fontWeight: '500',
              }}
            >
              식재료 절약단
            </span>
          </div>
          {/* <Divider sx={{ my: 1 }} /> */}
          {/* {mainListItems} */}
          <Main_Listitems />
        </List>
        <Menu
          id="fade-menu"
          MenuListProps={{
            'aria-labelledby': 'fade-button',
          }}
          anchorEl={anchorEl}
          open={open}
          onClose={handleClose}
          TransitionComponent={Fade}
        >
          <MenuItem onClick={handleClickOpenModal}>식당 소개메시지</MenuItem>
          <MenuItem onClick={handleClickOpenModal2}>식당 이미지 변경</MenuItem>
          <MenuItem onClick={() => navigate('/')}>로그아웃</MenuItem>
        </Menu>

        <Dialog open={openModal} onClose={handleCloseModal}>
          <DialogTitle>식당 소개 메시지</DialogTitle>
          <DialogContent>
            <DialogContentText sx={{ mb: '2vh' }}>
              소개 메시지를 변경할 수 있습니다.
            </DialogContentText>
            <div>
              <TextField
                id="outlined-multiline-static"
                label="현재 식당 소개 메시지"
                multiline
                disabled
                rows={5}
                defaultValue={info}
              />
              <TextField
                sx={{ ml: '2vw' }}
                id="outlined-multiline-static"
                label="식당 소개 메시지 변경"
                multiline
                value={info || ''}
                onChange={(e) => onInfo(e.target.value)}
                rows={5}
                placeholder={info}
              />
            </div>
          </DialogContent>
          <DialogActions>
            <Button onClick={onChangeMarketInfo}>등록</Button>
            <Button color="error" onClick={handleCloseModal}>
              닫기
            </Button>
          </DialogActions>
        </Dialog>

        <Dialog open={openModal2} onClose={handleCloseModal2}>
          <DialogTitle>식당 이미지 변경</DialogTitle>
          <DialogContent>
            <DialogContentText sx={{ mb: '2vh' }}>현재 이미지</DialogContentText>
            <div>
              <div style={{ display: 'flex', alignItems: 'center' }}>
                {image !== null ? (
                  <img
                    style={{
                      width: '7vw',
                    }}
                    src={
                      `http://kjj.kjj.r-e.kr:8080/api/image?token=${cookies.accesstoken}?dir=` +
                      image
                    }
                    alt="이미지없음"
                  />
                ) : (
                  <Skeleton variant="rectangular" width={210} height={118} />
                )}
                <input
                  style={{ marginLeft: '2vw' }}
                  type="file"
                  accept="image/*"
                  onChange={(e) => setNewImage(e.target.files[0])}
                />
              </div>
            </div>
          </DialogContent>
          <DialogActions>
            <Button onClick={onUpdateImage}>등록</Button>
            <Button color="error" onClick={handleCloseModal2}>
              닫기
            </Button>
          </DialogActions>
        </Dialog>

        <Snackbar open={success} autoHideDuration={6000} onClose={handleSuccessClose}>
          <Alert onClose={handleSuccessClose} severity="success" sx={{ width: '100%' }}>
            This is a success message!
          </Alert>
        </Snackbar>
      </Drawer>
    </>
  );
}

export default Drawerheader;
