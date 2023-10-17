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
import Main_Listitems from '../general/Main_Listitems';
import Typography from '@mui/material/Typography';
import Fade from '@mui/material/Fade';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { useState, useEffect, useRef } from 'react';
import {
  ConfigWithRefreshToken,
  ConfigWithToken,
  ManagerBaseApi,
} from '../../auth/authConfig';
import { useCookies } from 'react-cookie';
import { useRecoilState } from 'recoil';
import { useKeycloak } from '@react-keycloak/web';
import { isloginAtom } from '../../atom/loginAtom';
import { MdTouchApp } from 'react-icons/md';
import { styled as Cstyled, keyframes } from 'styled-components';
import UpdateInfoModal from './UpdateInfoModal';
import UpdateImgModal from './UpdateImgModal';
import UpdateNameModal from './UpdateNameModal';
import UserMenuModal from './UserMenuModal';

function Drawerheader(props) {
  const [islogin, setIsLogin] = useRecoilState(isloginAtom);
  const [cookies, setCookie] = useCookies();
  const navigate = useNavigate();
  const reconfig = ConfigWithRefreshToken();
  const config = ConfigWithToken();
  const formdataConfig = {
    headers: {
      'Content-Type': 'multipart/form-data',
      ...config.headers,
    },
  };
  const { keycloak } = useKeycloak();
  const [isExpired, setIsExpired] = useState(false);
  const isRefreshtoken = cookies.refreshtoken !== '';
  const nameRef = useRef('');
  const InfoRef = useRef('');
  const [name, setName] = useState('');
  const [info, setInfo] = useState('');
  const [image, setImage] = useState('');
  const [isName, setIsName] = useState(false);
  const [newImage, setNewImage] = useState([]);
  const [updateInfo, setUpdateInfo] = useState(false);
  const [updateImage, setUpdateImage] = useState(false);
  const [updateName, setUpdateName] = useState(false);
  const [anchorEl, setAnchorEl] = useState(null);
  const open = Boolean(anchorEl);
  const [open1, setOpen] = useState(true);

  useEffect(() => {
    getMarketDetails();
    getMarketImage();
  }, []);

  useEffect(() => {
    if (name !== '') setIsName(true);
    else if (name === '') setIsName(false);
  }, [name]);

  useEffect(() => {
    if (isExpired && isRefreshtoken && islogin) {
      reIssueToken();
      alert('세션이 만료되었습니다. 새로고침 후 이용해주세요.');
    }
  }, [isExpired]);

  const reIssueToken = async () => {
    try {
      const response = await axios({
        method: 'POST',
        url: '/api/user/login/refresh',
        ...reconfig,
      });
      if (response.headers.authorization !== '') {
        setCookie('accesstoken', response.headers.authorization);
      }
    } catch (err) {
      console.error(err);
    }
  };

  const menuOpenHandler = (event) => {
    setAnchorEl(event.currentTarget);
  };
  const menuCloseHandler = () => {
    setAnchorEl(null);
  };
  const toggleDrawer = () => {
    setOpen(!open1);
  };

  const openUpdateInfoModal = () => {
    setUpdateInfo(true);
    menuCloseHandler();
  };
  const closeUpdateInfoModal = () => {
    setUpdateInfo(false);
  };

  const openUpdateImageModal = () => {
    setUpdateImage(true);
    menuCloseHandler();
  };
  const closeUpdateImageModal = () => {
    setUpdateImage(false);
  };

  const openUpdateNameModal = () => {
    setUpdateName(true);
  };
  const closeUpdateNameModal = () => {
    setUpdateName(false);
  };

  const onUpdateMarketName = () => {
    const body = {
      name: nameRef.current.value,
    };
    axios.patch(`${ManagerBaseApi}/store/title`, body, config).then((res) => {
      if (res.status === 200) {
        getMarketDetails();
        closeUpdateNameModal();
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
        closeUpdateInfoModal();
      }
    });
  };

  const onUpdateMarketImage = () => {
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

    closeUpdateImageModal();
  };

  const getMarketDetails = () => {
    axios
      .get(`${ManagerBaseApi}/setting`, config)
      .then((res) => {
        setInfo(res.data.info);
        setName(res.data.name);
      })
      .catch((err) => {
        if (err.response.status === 403) setIsExpired(true);
      });
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
    setCookie('refreshtoken', '');
    navigate('/');
  };

  return (
    <>
      <AppBar position="absolute" open={open1}>
        <Toolbar sx={headerToolbarStyle}>
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
            sx={pagesNameStyle}
          >
            {props?.pages}
          </Typography>

          {!isName && (
            <SetNameWrapper>
              <span onClick={openUpdateNameModal}>
                클릭
                <MdTouchApp />
              </span>
              <span>식당이름을 설정해주세요</span>
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
            <AppbarUser>
              <AppbarUserTitle>관리자</AppbarUserTitle>

              {isName && (
                <span
                  style={{
                    ...marketNameStyle,
                    fontSize: '18px',
                  }}
                >
                  {name}
                </span>
              )}
            </AppbarUser>
          </IconButton>
        </Toolbar>
      </AppBar>

      <Drawer variant="permanent" open={open1}>
        <Toolbar sx={drawerToolbarStyle}>
          <IconButton onClick={toggleDrawer}>
            <ChevronLeftIcon />
          </IconButton>
        </Toolbar>
        <Divider />

        <List component="nav" sx={listStyle}>
          <ListImageWrapper>
            <ListImage
              src={`http://kjj.kjj.r-e.kr:8080/api/image?dir=` + image}
              alt="이미지 없음"
            />
            <ListImageText>식재료 절약단</ListImageText>
          </ListImageWrapper>
          <Main_Listitems />
        </List>

        <UserMenuModal
          anchorEl={anchorEl}
          open={open}
          onClose={menuCloseHandler}
          Fade={Fade}
          openUpdateImageModal={openUpdateImageModal}
          openUpdateNameModal={openUpdateNameModal}
          openUpdateInfoModal={openUpdateInfoModal}
          isName={isName}
          onLogout={onLogout}
        />

        <UpdateInfoModal
          open={updateInfo}
          onClose={closeUpdateInfoModal}
          info={info}
          InfoRef={InfoRef}
          onUpdateMarketInfo={onUpdateMarketInfo}
        />
        <UpdateImgModal
          open={updateImage}
          onClose={closeUpdateImageModal}
          image={image}
          setNewImage={setNewImage}
          onUpdateMarketImage={onUpdateMarketImage}
        />
        <UpdateNameModal
          open={updateName}
          onClose={closeUpdateNameModal}
          isName={isName}
          nameRef={nameRef}
          name={name}
          onUpdateMarketName={onUpdateMarketName}
        />
      </Drawer>
    </>
  );
}

export default Drawerheader;

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

const screenWidth = window.innerWidth;
const drawerWidth = screenWidth < 450 ? 20 : 220;
const pagesNameStyle = { flexGrow: 1, fontWeight: 600, fontSize: '25px' };
const listStyle = { backgroundColor: '#f5f5f5', height: '100%' };

const headerToolbarStyle = {
  backgroundColor: '#24292e',
  pr: '24px',
};

const drawerToolbarStyle = {
  backgroundColor: '#f5f5f5',
  zIndex: 3,
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'flex-end',
  px: [1],
};

const marketNameStyle = {
  fontWeight: '600',
  color: 'inherit',
  textDecoration: 'underline',
  textDecorationThickness: '1px',
  whiteSpace: 'nowrap',
  textAlign: 'right',
};

const blinkEffects = keyframes`
  50%{
    opacity:0.3;
  }
`;

const SetNameWrapper = Cstyled.div`
  display:flex;
  flex-direction:column;
  align-items:center;
  justify-content:center;
  line-height:25px;

  span{
    display:flex;
    justify-content:center;
    align-items:center;
  }
  span:first-child{
    font-size:16px;
    font-weight:600;
    animation:${blinkEffects} 1s ease infinite;
    &:hover{
      cursor:pointer;
    }
  }
  span:last-child{
    font-size:13px;
    font-weight:600;
  }
`;

const AppbarUser = Cstyled.div`
  display:flex;
  justify-content:center;
  align-items:flex-end;
  padding:0 20px;
  flex-direction:column;

`;

const AppbarUserTitle = Cstyled.span`
  font-size:20px;
  font-weight:700;
  color:inherit;
  margin-bottom:1px;
`;

const ListImageWrapper = Cstyled.div`
  width:100%;
  height:20vh;

  display:flex;
  flex-direction:column;
  justify-content:center;
  align-items:center;
`;

const ListImage = Cstyled.img`
width: 45%;
maxWidth: 45%;
`;

const ListImageText = Cstyled.span`
  font-size:16px;
  color:#0a376e;
  font-weight:600;
`;
