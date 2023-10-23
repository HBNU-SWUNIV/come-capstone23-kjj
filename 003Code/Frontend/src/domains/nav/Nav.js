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
import { useState } from 'react';
import { styled as Cstyled, keyframes } from 'styled-components';
import NavLists from '../../components/nav/NavLists';
import UpdateImgModal from '../../components/nav/UpdateImgModal';
import UpdateInfoModal from '../../components/nav/UpdateInfoModal';
import UpdateNameModal from '../../components/nav/UpdateNameModal';
import UserMenuModal from '../../components/nav/UserMenuModal';
import UseNav from '../../hooks/UseNav';
import UseNavApi from '../../hooks/UseNavApi';

function Nav(props) {
  const [draweropen, setDraweropen] = useState(true);
  const handledrawer = () => {
    setDraweropen(!draweropen);
  };

  const {
    onLogout,
    openUpdateModal,
    closeAllUpdateModals,
    updateforms,
    openUsermenuModal,
    closeUsermenuModal,
    usermodalOpen,
    anchorEl,
  } = UseNav();

  const {
    form,
    isName,
    nameRef,
    InfoRef,
    updateMarketName,
    updateMarketInfo,
    updateMarketImage,
    setNewImage,
  } = UseNavApi(closeAllUpdateModals);

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

          <IconButton
            color="inherit"
            id="fade-button"
            aria-controls={usermodalOpen ? 'fade-menu' : undefined}
            aria-haspopup="true"
            aria-expanded={usermodalOpen ? 'true' : undefined}
            onClick={openUsermenuModal}
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
          open={usermodalOpen}
          onClose={closeUsermenuModal}
          Fade={Fade}
          openUpdateModal={openUpdateModal}
          isName={isName}
          onLogout={onLogout}
        />

        <UpdateNameModal
          open={updateforms.name}
          onClose={closeAllUpdateModals}
          isName={isName}
          nameRef={nameRef}
          name={form?.name}
          onUpdateMarketName={updateMarketName}
        />
        <UpdateInfoModal
          open={updateforms.info}
          onClose={closeAllUpdateModals}
          info={form?.info}
          InfoRef={InfoRef}
          onUpdateMarketInfo={updateMarketInfo}
        />
        <UpdateImgModal
          open={updateforms.image}
          onClose={closeAllUpdateModals}
          image={form?.image}
          setNewImage={setNewImage}
          onUpdateMarketImage={updateMarketImage}
        />
      </Drawer>
    </>
  );
}

export default Nav;

// Appbar, Drawer은 mui 부트스트랩 기본 함수들, 건드리지 않음.
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
