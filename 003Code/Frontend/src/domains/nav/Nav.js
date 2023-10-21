import ChevronLeftIcon from '@mui/icons-material/ChevronLeft';
import MenuIcon from '@mui/icons-material/Menu';
import MuiAppBar from '@mui/material/AppBar';
import Divider from '@mui/material/Divider';
import MuiDrawer from '@mui/material/Drawer';
import Fade from '@mui/material/Fade';
import IconButton from '@mui/material/IconButton';
import List from '@mui/material/List';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import { styled } from '@mui/material/styles';
import { useKeycloak } from '@react-keycloak/web';
import axios from 'axios';
import { useEffect, useRef, useState } from 'react';
import { useCookies } from 'react-cookie';
import { MdTouchApp } from 'react-icons/md';
import { useNavigate } from 'react-router-dom';
import { useRecoilState } from 'recoil';
import { styled as Cstyled, keyframes } from 'styled-components';
import { isloginAtom } from '../../atom/loginAtom';
import { ConfigWithToken, ManagerBaseApi } from '../../auth/authConfig';
import NavLists from '../../components/nav/NavLists';
import UpdateImgModal from '../../components/nav/UpdateImgModal';
import UpdateInfoModal from '../../components/nav/UpdateInfoModal';
import UpdateNameModal from '../../components/nav/UpdateNameModal';
import UserMenuModal from '../../components/nav/UserMenuModal';
import Api_nav from '../../api/Api_nav';

function Nav(props) {
  const [islogin, setIsLogin] = useRecoilState(isloginAtom);
  const { keycloak } = useKeycloak();
  const [cookies, setCookie] = useCookies();
  const navigate = useNavigate();
  const config = ConfigWithToken();
  const formdataConfig = {
    headers: {
      'Content-Type': 'multipart/form-data',
      ...config.headers,
    },
  };

  const nameRef = useRef('');
  const InfoRef = useRef('');

  const { marketDetails, refetchmarketDetails, isLoading } = Api_nav();

  const [form, setForm] = useState({
    name: '',
    info: '',
    image: '',
  });

  useEffect(() => {
    setForm({
      name: marketDetails?.name,
      info: marketDetails?.info,
      image: marketDetails?.image,
    });
  }, [isLoading, marketDetails]);

  const [isName, setIsName] = useState(false);
  const [newImage, setNewImage] = useState([]);
  const [updateInfo, setUpdateInfo] = useState(false);
  const [updateImage, setUpdateImage] = useState(false);
  const [updateName, setUpdateName] = useState(false);
  const [anchorEl, setAnchorEl] = useState(null);
  const open = Boolean(anchorEl);
  const [draweropen, setDraweropen] = useState(true);

  useEffect(() => {
    if (form?.name !== '') setIsName(true);
    else if (form?.name === '') setIsName(false);
  }, [form?.name]);

  const handledrawer = () => {
    setDraweropen(!draweropen);
  };

  const menuOpenHandler = (e) => {
    setAnchorEl(e.currentTarget);
  };

  const usermenucloseHandler = () => {
    setAnchorEl(null);
  };

  const openUpdateNameModal = () => {
    setUpdateName(true);
  };
  const openUpdateImgModal = () => {
    setUpdateImage(true);
    usermenucloseHandler();
  };
  const openUpdateInfoModal = () => {
    setUpdateInfo(true);
    usermenucloseHandler();
  };
  const closeUpdateInfoModal = () => {
    setUpdateInfo(false);
  };
  const closeUpdateImgModal = () => {
    setUpdateImage(false);
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
        closeUpdateNameModal();
        refetchmarketDetails();
      }
    });
  };

  const onUpdateMarketInfo = () => {
    let body = {
      info: InfoRef.current.value,
    };
    axios.patch(`${ManagerBaseApi}/store/info`, body, config).then((res) => {
      if (res.status === 200) {
        refetchmarketDetails();
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
        if (res.status === 200) refetchmarketDetails();
      })
      .catch((err) => alert('이미지 용량이 너무 큽니다.'));

    closeUpdateImgModal();
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
      <AppBar position="absolute" open={draweropen}>
        <Toolbar sx={headerToolbarStyle}>
          <IconButton
            edge="start"
            color="inherit"
            aria-label="open drawer"
            onClick={handledrawer}
            sx={{
              marginRight: '36px',
              ...(draweropen && { display: 'none' }),
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
                  {form?.name}
                </span>
              )}
            </AppbarUser>
          </IconButton>
        </Toolbar>
      </AppBar>

      <Drawer variant="permanent" open={draweropen}>
        <Toolbar sx={drawerToolbarStyle}>
          <IconButton onClick={handledrawer}>
            <ChevronLeftIcon />
          </IconButton>
        </Toolbar>
        <Divider />

        <List component="nav" sx={listStyle}>
          <ListImageWrapper>
            <ListImage
              src={`http://kjj.kjj.r-e.kr:8080/api/image?dir=` + form?.image}
              alt="이미지 없음"
            />
            <ListImageText>식재료 절약단</ListImageText>
          </ListImageWrapper>
          <NavLists />
        </List>

        <UserMenuModal
          anchorEl={anchorEl}
          open={open}
          onClose={usermenucloseHandler}
          Fade={Fade}
          openUpdateImageModal={openUpdateImgModal}
          openUpdateNameModal={openUpdateNameModal}
          openUpdateInfoModal={openUpdateInfoModal}
          isName={isName}
          onLogout={onLogout}
        />

        <UpdateInfoModal
          open={updateInfo}
          onClose={closeUpdateInfoModal}
          info={form.info}
          InfoRef={InfoRef}
          onUpdateMarketInfo={onUpdateMarketInfo}
        />
        <UpdateImgModal
          open={updateImage}
          onClose={closeUpdateImgModal}
          image={form.image}
          setNewImage={setNewImage}
          onUpdateMarketImage={onUpdateMarketImage}
        />
        <UpdateNameModal
          open={updateName}
          onClose={closeUpdateNameModal}
          isName={isName}
          nameRef={nameRef}
          name={form.name}
          onUpdateMarketName={onUpdateMarketName}
        />
      </Drawer>
    </>
  );
}

export default Nav;

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
