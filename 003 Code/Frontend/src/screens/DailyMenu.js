import * as React from 'react';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import Toolbar from '@mui/material/Toolbar';
import Calander from '../components/DailyMenu/Calander';
import { styled } from 'styled-components';
import Drawerheader from '../components/Drawerheader';
import axios from 'axios';
import { ConfigWithToken, ManagerBaseApi } from '../auth/authConfig';
import { NanumFontStyle } from './Menus';
import {
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  Skeleton,
  Snackbar,
  TextField,
} from '@mui/material';
import Button from '@mui/material/Button';
import MenuSlider from '../components/DailyMenu/MenuSlider';
import ErrorInform from '../components/general/ErrorInform';
import Copyright from '../components/general/Copyright';

export default function DailyMenu() {
  const config = ConfigWithToken();
  const formdataConfig = {
    headers: {
      'Content-Type': 'multipart/form-data',
      ...config.headers,
    },
  };
  const menuCostRef = React.useRef('');
  const [image, setImage] = React.useState([]);
  const [isTodayMenu, setIsTodayMenu] = React.useState(false);
  const [openTodayMenu, setOpenTodayMenu] = React.useState(false);
  const [todayMenuError, setTodayMenuError] = React.useState(false);

  React.useEffect(() => {
    axios
      .get(`${ManagerBaseApi}/menu/planner`, config)
      .then((res) => setIsTodayMenu(res.data))
      .catch((err) => console.log('DailyMenu Error=', err));
  });

  const openTodayMenuModal = () => {
    setOpenTodayMenu(true);
  };
  const closeTodayMenuModal = () => {
    setOpenTodayMenu(false);
  };

  const handleTodayMenuError = () => {
    if (menuCostRef.current.value !== '') setTodayMenuError(false);
    else setTodayMenuError(true);
  };

  const addTodayMenu = () => {
    const formdata = new FormData();
    let body = {
      name: '오늘의 메뉴',
      cost: menuCostRef.current.value,
      details: '매일 바뀌는 메뉴 입니다',
      usePlanner: true,
    };
    const blob = new Blob([JSON.stringify(body)], { type: 'application/json' });
    formdata.append('data', blob);
    formdata.append('file', image);
    axios({
      method: 'POST',
      url: `${ManagerBaseApi}/menu`,
      data: formdata,
      ...formdataConfig,
    })
      .then(() => {
        axios.get(`${ManagerBaseApi}/menu/planner`, config).then((res) => {
          setIsTodayMenu(res.data);
          closeTodayMenuModal();
        });
      })
      .catch((err) => {
        if (err.response.status === 400) {
          setTodayMenuError(true);
          return;
        }
      });
    setImage(null);
  };

  const [state, setState] = React.useState({
    open: false,
    vertical: 'top',
    horizontal: 'center',
  });

  const { vertical, horizontal, openTip } = state;
  const handleTip = (newState) => () => {
    setState({ ...newState, openTip: true });
  };
  const closeTip = () => {
    setState({ ...state, openTip: false });
  };

  return (
    <ThemeProvider theme={defaultTheme}>
      <Box sx={{ display: 'flex' }}>
        <CssBaseline />
        <Drawerheader pages={'오늘의 메뉴'} />
        <Box
          component="main"
          sx={{
            display: 'flex',
            flexDirection: 'column',
            backgroundColor: 'white',
            flexGrow: 1,
            height: '100%',
            minHeight: '100vh',
            display: 'flex',
            boxSizing: 'border-box',
            paddingBottom: 'var(--copyright-height)',
          }}
        >
          <Toolbar />
          <Container maxWidth="xl" sx={{ mt: 4 }}>
            <Wrapper $isTodayMenu={isTodayMenu}>
              <CalanderText>
                {isTodayMenu ? (
                  <>
                    <Button sx={ButtonStyle} disabled color="success" variant="contained">
                      오늘의메뉴가 등록되어있습니다.
                    </Button>
                    <span
                      style={{
                        fontSize: '16px',
                        color: 'rgb(0,0,0,0.3)',
                        marginBottom: '30px',
                      }}
                    >
                      오늘의 메뉴는 메뉴 페이지에서 삭제할 수 있습니다
                    </span>
                  </>
                ) : (
                  <Button
                    sx={{
                      ...ButtonStyle,
                      backgroundColor: 'rgb(0, 171, 85)',
                    }}
                    onClick={openTodayMenuModal}
                    variant="contained"
                    color="success"
                  >
                    등록하기
                  </Button>
                )}

                <Typography
                  sx={{ fontWeight: '600' }}
                  variant="h2"
                  align="center"
                  color="rgb(0, 171, 85)"
                  paragraph
                >
                  오늘의 메뉴는?
                </Typography>
                {!isTodayMenu ? (
                  <>
                    <span>
                      요일 별 달라지는 기본 메뉴가 있다면 오늘의 메뉴로 등록해보세요
                    </span>

                    <Button
                      sx={{ width: '150px' }}
                      color="error"
                      onClick={handleTip({ vertical: 'top', horizontal: 'center' })}
                    >
                      오늘의 메뉴가 뭔가요?
                    </Button>

                    <MenuSlider />
                  </>
                ) : (
                  <>
                    <span>오른쪽 달력에서 요일을 클릭하여</span>
                    <span>요일 별 메뉴를 등록해주세요</span>
                  </>
                )}
              </CalanderText>

              {isTodayMenu && <Calander />}
            </Wrapper>
          </Container>

          <Dialog open={openTodayMenu} onClose={closeTodayMenuModal}>
            <DialogTitle>오늘의메뉴 등록</DialogTitle>
            <DialogContent sx={{ display: 'flex', flexDirection: 'column', gap: '1vh' }}>
              <DialogContentText sx={NanumFontStyle}>
                이미지 파일을 추가하여 이미지를 등록해주세요.
              </DialogContentText>

              <div style={{ display: 'flex', alignItems: 'center' }}>
                <Skeleton variant="rectangular" width={210} height={118} />
                <input
                  style={{ marginLeft: '2vw' }}
                  type="file"
                  accept="image/*"
                  onChange={(e) => setImage(e.target.files[0])}
                />
              </div>

              <TextField
                disabled
                id="outlined-required"
                label="오늘의 메뉴 이름은 고정입니다."
              />
              <TextField
                disabled
                id="outlined-required2"
                label="오늘의 메뉴 정보는 고정입니다."
              />
              <TextField
                inputRef={menuCostRef}
                id="outlined-number"
                label="가격"
                type="number"
                placeholder="가격"
                onBlur={handleTodayMenuError}
              />
              {todayMenuError && <ErrorInform message="가격은 필수입니다." />}
            </DialogContent>
            <DialogActions>
              <Button sx={NanumFontStyle} onClick={addTodayMenu}>
                등록
              </Button>
              <Button sx={NanumFontStyle} color="error" onClick={closeTodayMenuModal}>
                닫기
              </Button>
            </DialogActions>
          </Dialog>

          <Snackbar
            anchorOrigin={{ vertical, horizontal }}
            open={openTip}
            onClose={closeTip}
            message={TodayMenuTip}
            key={vertical + horizontal}
          />
        </Box>
      </Box>
    </ThemeProvider>
  );
}

const ButtonStyle = {
  fontWeight: '600',
  fontSize: '20px',
  marginBottom: '10px',
};

const defaultTheme = createTheme();
const TodayMenuTip =
  '매일 매일 바뀌는 오늘의메뉴는 다양한 요리사나 조리사가 창의적인 아이디어로 준비하는 식사입니다. 이런 음식은 종종 "오늘의 특별 메뉴" 또는 "일일 특선"과 같은 이름으로 불립니다. 이러한 오늘의 메뉴는 식당의 메뉴 다양성을 높이고 고객들에게 새로운 맛과 경험을 제공하기 위해 마련되는 것이 일반적입니다 이렇게 매일 바뀌는 오늘의 메뉴는 고객들에게 루틴을 깨고 새로운 맛을 경험하게 해주며, 음식점의 업적을 증진시키고 경쟁력을 높이는데 도움을 줄 수 있습니다. 또한, 이렇게 다양한 음식을 맛보는 과정에서 식사를 더욱 즐겁게 함으로써 직원들과 손님들 간의 소통과 만족도도 향상시킬 수 있습니다';

export const Circle = styled.div`
  width: 0.313rem;
  height: 0.313rem;
  margin-right: 0.313rem;
  border-radius: 0.656rem;
  background-color: #b0b3bc;
`;

const Wrapper = styled.div`
  display: flex;
  justify-content: ${({ $isTodayMenu }) => ($isTodayMenu ? 'space-between' : 'center')};
  margin-top: ${({ $isTodayMenu }) => ($isTodayMenu ? '0' : '10vh')};
  @media screen and (max-width: 1200px) {
    justify-content: center;
  }
`;

const CalanderText = styled.div`
  @media screen and (max-width:1200px){
    display:none;
  }
  display:flex;
  flex-direction:column;
  align-items:center;
  justify-content:center;
  max-width:5%:
  height:100%;
  span{
    font-size:25px;
    font-family:NotoSans;
    white-space:nowrap;
    font-weight:600;
  }
`;
