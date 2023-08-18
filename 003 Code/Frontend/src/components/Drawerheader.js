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
import Main_Listitems from './general/Main_Listitems';
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
import { useState, useEffect, useRef } from 'react';
import Skeleton from '@mui/material/Skeleton';
import Snackbar from '@mui/material/Snackbar';
import MuiAlert from '@mui/material/Alert';
import { ConfigWithToken, ManagerBaseApi } from '../auth/authConfig';
import { useCookies } from 'react-cookie';
import { useSetRecoilState } from 'recoil';
import { useKeycloak } from '@react-keycloak/web';
import { isloginAtom } from '../atom/loginAtom';
import { MdTouchApp } from 'react-icons/md';
import { styled as Cstyled } from 'styled-components';

const marketNameStyle = {
  fontWeight: '600',
  color: 'inherit',
  textDecoration: 'underline',
  textDecorationThickness: '1px',
  whiteSpace: 'nowrap',
  textAlign: 'right',
};
const MenuItemTextStyle = {
  fontFamily: 'Nanum',
  fontWeight: 500,
  margin: '10px 0px',
};
const DialogTitleStyle = {
  margin: '0 auto',
  fontFamily: 'Nanum',
  fontSize: '20px',
  fontWeight: '600',
};
const DialogTextStyle = {
  fontFamily: 'Dongle',
  fontSize: '25px',
};

const SetNameWrapper = Cstyled.div`
  display:flex;
  flex-direction:column;
  align-items:center;
  justify-content:center;
  line-height:25px;
  span{
    font-size:25px;
    font-family:Cutefont;
  }
  span:first-child{
    font-weight:600;
    &:hover{
      cursor:pointer;
    }
  }
  span:last-child{
    font-weight:400;
  }
`;

function Drawerheader(props) {
  const navigate = useNavigate();

  const [isName, setIsName] = useState(false);
  const [success, setSuccess] = useState(false);

  const nameRef = useRef('');
  const InfoRef = useRef('');

  const [name, setName] = useState('');
  const [info, setInfo] = useState('');
  const [image, setImage] = useState('');
  const [newImage, setNewImage] = useState([]);

  const [anchorEl, setAnchorEl] = useState(null);
  const open = Boolean(anchorEl);
  const [open1, setOpen] = useState(true);
  const [onInfo, setOpenModal] = useState(false);
  const [onImage, setOpenModal2] = useState(false);
  const [onName, setOnName] = useState(false);

  const setIsLogin = useSetRecoilState(isloginAtom);

  const { keycloak } = useKeycloak();
  const [cookies, setCookie] = useCookies('accesstoken');
  const config = ConfigWithToken();
  const formdataConfig = {
    headers: {
      'Content-Type': 'multipart/form-data',
      ...config.headers,
    },
  };

  useEffect(() => {
    getMarketDetails();
    getMarketImage();
    if (name !== '') setIsName(true);
    else if (name === '') setIsName(false);
  }, [name, image, info]);

  const menuOpenHandler = (event) => {
    setAnchorEl(event.currentTarget);
  };
  const menuCloseHandler = () => {
    setAnchorEl(null);
  };
  const toggleDrawer = () => {
    setOpen(!open1);
  };
  const handleClickOpenModal = () => {
    setOpenModal(true);
    menuCloseHandler();
  };
  const onOffInfo = () => {
    setOpenModal(false);
  };
  const handleClickOpenModal2 = () => {
    setOpenModal2(true);
    menuCloseHandler();
  };
  const onOffImage = () => {
    setOpenModal2(false);
  };
  const handleSuccessOpen = () => {
    setSuccess(true);
  };
  const handleSuccessClose = (event, reason) => {
    if (reason === 'clickaway') {
      return;
    }
    setSuccess(false);
  };
  const handleOnName = () => {
    setOnName(true);
  };
  const handleOffName = () => {
    setOnName(false);
  };

  const onUpdateMarketName = () => {
    const body = {
      name: nameRef.current.value,
    };
    axios.patch(`${ManagerBaseApi}/store/title`, body, config).then((res) => {
      if (res.status === 200) {
        getMarketDetails();
        handleOffName();
      }
    });
  };
  const onUpdateMarketInfo = () => {
    let body = {
      info: InfoRef.current.value,
    };
    axios.patch(`${ManagerBaseApi}/store/info`, body, config).then((res) => {
      if (res.status === 200) {
        getMarketDetails();
        onOffInfo();
      }
    });
  };
  const onUpdateImage = () => {
    const formdata = new FormData();
    newImage !== null && formdata.append('file', newImage);

    axios({
      method: 'POST',
      url: `${ManagerBaseApi}/image`,
      data: formdata,
      ...formdataConfig,
    })
      .then((res) => {
        if (res.status === 200) getMarketImage();
      })
      .catch((err) => alert('이미지 용량이 너무 큽니다.'));
    handleSuccessOpen();
    onOffImage();
  };

  const getMarketDetails = () => {
    axios
      .get(`${ManagerBaseApi}/setting`, config)
      .then((res) => {
        setInfo(res.data.info);
        setName(res.data.name);
      })
      .catch((err) => {});
  };
  const getMarketImage = () => {
    axios
      .get(`/api/user/store`, config)
      .then((res) => setImage(res.data.image))
      .catch((err) => console.log(err));
  };

  const onLogout = () => {
    if (keycloak.authenticated) {
      keycloak.logout();
    } else {
      setIsLogin(false);
    }
    setCookie('accesstoken', '');
    navigate('/');
  };

  return (
    <>
      <AppBar position="absolute" open={open1}>
        <Toolbar
          sx={{
            pr: '24px',
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
            sx={{
              flexGrow: 1,
              fontFamily: 'SingleDay',
              fontWeight: 500,
              fontSize: '30px',
            }}
          >
            {props?.pages}
          </Typography>

          {!isName && (
            <SetNameWrapper>
              <span className="blink" onClick={handleOnName}>
                <MdTouchApp />
                여기 클릭
              </span>
              <span>식당이름을 설정해주세요.</span>
            </SetNameWrapper>
          )}

          <IconButton
            color="inherit"
            id="fade-button"
            aria-controls={open ? 'fade-menu' : undefined}
            aria-haspopup="true"
            aria-expanded={open ? 'true' : undefined}
            onClick={menuOpenHandler}
          >
            <div
              style={{
                display: 'flex',
                flexDirection: 'column',
                justifyContent: 'center',
                alignItems: 'end',
                padding: '0 20px',
              }}
            >
              <span
                style={{
                  fontSize: '20px',
                  fontWeight: '700',
                  color: 'inherit',
                  fontFamily: 'Nanum',
                  marginBottom: '1px',
                }}
              >
                관리자
              </span>

              {isName && (
                <span
                  style={{
                    ...marketNameStyle,
                    fontSize: '18px',
                    fontFamily: 'Nanum',
                  }}
                >
                  {name}
                </span>
              )}
            </div>
          </IconButton>
        </Toolbar>
      </AppBar>

      <Drawer variant="permanent" open={open1}>
        <Toolbar
          sx={{
            backgroundColor: '#f5f5f5',
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

        <List component="nav" sx={{ backgroundColor: '#f5f5f5', height: '100%' }}>
          <div
            style={{
              width: '100%',
              height: '20vh',
              display: 'flex',
              flexDirection: 'column',
              alignItems: 'center',
              justifyContent: 'center',
            }}
          >
            <img
              style={{
                width: '50%',
                maxWidth: '50%',
              }}
              src={`http://kjj.kjj.r-e.kr:8080/api/image?dir=` + image}
              alt="이미지없음"
            />
            <span
              style={{
                fontSize: '20px',
                fontFamily: 'SingleDay',
                color: '#0a376e',
                fontWeight: '600',
              }}
            >
              식재료 절약단
            </span>
          </div>
          <Main_Listitems />
        </List>

        <Menu
          id="fade-menu"
          MenuListProps={{
            'aria-labelledby': 'fade-button',
          }}
          anchorEl={anchorEl}
          open={open}
          onClose={menuCloseHandler}
          TransitionComponent={Fade}
        >
          <MenuItem sx={MenuItemTextStyle} onClick={handleOnName}>
            식당 이름 {isName ? '수정' : '설정'}
          </MenuItem>
          <MenuItem
            sx={{ ...MenuItemTextStyle, width: '250px' }}
            onClick={handleClickOpenModal}
          >
            식당 소개 메시지 변경
          </MenuItem>
          <MenuItem sx={MenuItemTextStyle} onClick={handleClickOpenModal2}>
            식당 이미지 변경
          </MenuItem>
          <Divider />
          <MenuItem sx={MenuItemTextStyle} onClick={onLogout}>
            로그아웃
          </MenuItem>
        </Menu>

        <Dialog open={onInfo} onClose={onOffInfo}>
          <DialogTitle sx={DialogTitleStyle}>식당 소개 메시지 변경</DialogTitle>
          <DialogContent>
            <DialogContentText sx={{ ...DialogTextStyle, marginBottom: '10px' }}>
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
                inputRef={InfoRef}
                rows={5}
                placeholder={info}
              />
            </div>
          </DialogContent>
          <DialogActions>
            <Button onClick={onUpdateMarketInfo}>등록</Button>
            <Button color="error" onClick={onOffInfo}>
              닫기
            </Button>
          </DialogActions>
        </Dialog>

        <Dialog open={onImage} onClose={onOffImage}>
          <DialogTitle sx={DialogTitleStyle}>식당 이미지 변경하기</DialogTitle>
          <DialogContent>
            <DialogContentText sx={DialogTextStyle}>현재 이미지</DialogContentText>
            <div>
              <div
                style={{
                  display: 'flex',
                  alignItems: 'center',
                }}
              >
                {image !== null ? (
                  <img
                    style={{
                      width: '10vw',
                      minWidth: '10vw',
                    }}
                    src={`http://kjj.kjj.r-e.kr:8080/api/image?dir=` + image}
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
            <Button color="error" onClick={onOffImage}>
              닫기
            </Button>
          </DialogActions>
        </Dialog>

        <Snackbar open={success} autoHideDuration={6000} onClose={handleSuccessClose}>
          <Alert onClose={handleSuccessClose} severity="success" sx={{ width: '100%' }}>
            성공!
          </Alert>
        </Snackbar>
      </Drawer>

      <Dialog open={onName} onClose={handleOffName}>
        <DialogTitle sx={DialogTitleStyle}>
          식당 이름 {isName ? '수정' : '설정'}
        </DialogTitle>
        <DialogContent>
          <DialogContentText sx={DialogTextStyle}>
            소비자도 쉽게 확인할 수 있는 방법으로 식당 이름을 {isName ? '수정' : '설정'}
            해주세요
          </DialogContentText>
          <TextField
            autoFocus
            margin="dense"
            id="nameRef"
            inputRef={nameRef}
            placeholder={isName ? name : ''}
            label={isName ? '기존 식당 이름' : '식당 이름'}
            fullWidth
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={onUpdateMarketName}>등록</Button>
          <Button color="error" onClick={handleOffName}>
            닫기
          </Button>
        </DialogActions>
      </Dialog>
    </>
  );
}

export default Drawerheader;

const screenWidth = window.innerWidth;
const drawerWidth = screenWidth < 450 ? 20 : 220;

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
