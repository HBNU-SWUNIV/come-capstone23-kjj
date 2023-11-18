import MuiAppBar from '@mui/material/AppBar';
import Divider from '@mui/material/Divider';
import MuiDrawer from '@mui/material/Drawer';
import Fade from '@mui/material/Fade';
import IconButton from '@mui/material/IconButton';
import List from '@mui/material/List';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import { styled } from '@mui/material/styles';
import { styled as Cstyled } from 'styled-components';
import NavLists from '../../components/nav/NavLists';
import UpdateImgModal from '../../components/nav/UpdateImgModal';
import UpdateInfoModal from '../../components/nav/UpdateInfoModal';
import UpdateNameModal from '../../components/nav/UpdateNameModal';
import UserMenuModal from '../../components/nav/UserMenuModal';
import UseNav from '../../hooks/UseNav';
import UseNavApi from '../../hooks/UseNavApi';
import favicon from '../../image/favico.png';
import { flexJCenter } from '../../styles/global.style';

function Nav(props) {
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
      <AppBar position="fixed">
        <Toolbar sx={headerToolbarStyle}>
          <IconButton
            edge="start"
            color="inherit"
            aria-label="open drawer"
            sx={{
              marginRight: '36px',
            }}
          >
            <img src={favicon} width={40} />
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

      <Drawer variant="permanent" sx={drawerStyle}>
        <Toolbar sx={drawerToolbarStyle}>
          <img src={favicon} width={40} />
        </Toolbar>
        <Divider />

        <List component="nav" sx={listStyle}>
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
  border: '1px solid rgb(69, 75, 95)',
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
      top: '-10px',
      position: 'relative',
      whiteSpace: 'nowrap',
      width: drawerWidth,
      height: 'calc(100% + 10px)',
      transition: theme.transitions.create('width', {
        easing: theme.transitions.easing.sharp,
        duration: theme.transitions.duration.enteringScreen,
      }),
      border: '1px solid #24292e',
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

const pagesNameStyle = {
  flexGrow: 1,
  fontWeight: 600,
  fontSize: '20px',
};

const listStyle = {
  backgroundColor: 'rgb(69,75,95)',
  height: '100%',
};

const headerToolbarStyle = {
  backgroundColor: '#24292e',
  pr: '24px',
};

const drawerStyle = {
  border: '1px solid rgb(69, 75, 95)',
};

const drawerToolbarStyle = {
  backgroundColor: 'rgb(69, 75, 95)',
  zIndex: 3,
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'space-between',
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

const AppbarUser = Cstyled.div`
  ${flexJCenter};
  flex-direction:column;
  align-items:flex-end;
  padding:0 20px;
`;

const AppbarUserTitle = Cstyled.span`
  margin-bottom:1px;

  font-size:20px;
  font-weight:700;
  color:inherit;
`;
