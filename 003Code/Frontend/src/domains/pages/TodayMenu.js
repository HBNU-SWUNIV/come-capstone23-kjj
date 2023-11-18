import { Snackbar } from '@mui/material';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Container from '@mui/material/Container';
import CssBaseline from '@mui/material/CssBaseline';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import Toolbar from '@mui/material/Toolbar';
import axios from 'axios';
import { useRef, useState } from 'react';
import { useMutation, useQuery } from 'react-query';
import { styled } from 'styled-components';
import { getTodayMenu } from '../../api/apis';
import TodayMenuAddDialog from '../../components/todaymenu/TodayMenuAddDialog';
import TodayMenuCalander from '../../components/todaymenu/TodayMenuCalander';
import TodayMenuSlider from '../../components/todaymenu/TodayMenuSlider';
import UseErrorHandler from '../../hooks/UseErrorHandler';
import UseImageHandler from '../../hooks/UseImageHandler';
import { flexCenter } from '../../styles/global.style';
import { ConfigWithToken, ManagerBaseApi } from '../../utils/utils';
import Nav from '../nav/Nav';

export default function TodayMenu() {
  const config = ConfigWithToken();
  const formdataConfig = {
    headers: {
      'Content-Type': 'multipart/form-data',
      ...config.headers,
    },
  };
  const { data: isTodayMenu, refetch: refetchIsTodayMenu } = useQuery(
    ['getTodayMenu', config],
    () => getTodayMenu(config)
  );

  const [isLoading, setIsLoading] = useState(false);
  const menuCostRef = useRef('');

  const [image, setImage] = useState([]);
  const [selectedImg, setSelectedImg] = useState(null);

  const [openTodayMenu, setOpenTodayMenu] = useState(false);
  const [todayMenuError, setTodayMenuError] = useState(false);

  const onAddTodayMenu = useMutation(
    (data) =>
      axios({
        method: 'POST',
        url: `${ManagerBaseApi}/menu`,
        data: data,
        ...formdataConfig,
      }),
    {
      onSuccess: () => {
        refetchIsTodayMenu();
        closeTodayMenuModal();
      },
      onError: (err) => {
        if (err.response.status === 400) {
          setTodayMenuError(true);
          setIsLoading(false);
          return;
        }
      },
    }
  );

  const handleImageChange = (event) => {
    UseImageHandler(event, setImage, setSelectedImg);
  };

  const openTodayMenuModal = () => {
    setOpenTodayMenu(true);
  };
  const closeTodayMenuModal = () => {
    setOpenTodayMenu(false);
    setIsLoading(false);
  };

  const handleTodayMenuError = () => {
    UseErrorHandler({
      condition: menuCostRef.current.value !== '',
      setFn: setTodayMenuError,
    });
  };

  const addTodayMenu = () => {
    setIsLoading(true);

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
    onAddTodayMenu.mutate(formdata);
    setImage(null);
  };

  const [state, setState] = useState({
    open: false,
    vertical: 'top',
    horizontal: 'center',
    message: '',
  });

  const { vertical, horizontal, openTip, message } = state;

  const handleTip = (newState) => () => {
    setState({ ...newState, openTip: true });
  };
  const closeTip = () => {
    setState({ ...state, openTip: false });
  };

  const todaymenu_dialog_props = {
    open: openTodayMenu,
    onClose: closeTodayMenuModal,
    setImage: setImage,
    menuCostRef: menuCostRef,
    handleTodayMenuError: handleTodayMenuError,
    todayMenuError: todayMenuError,
    addTodayMenu: addTodayMenu,
    selectedImg: selectedImg,
    handleImageChange: handleImageChange,
    isLoading: isLoading,
  };

  const todaymenu_left_button_and_text_datas = [
    {
      condition: isTodayMenu,
      btnBgColor: 'rgb(0, 171, 85)',
      isDisabled: true,
      text: '오늘의 메뉴는 메뉴 페이지에서 삭제할 수 있습니다',
      tipText: '오늘의 메뉴 추가하는 방법',
      tipMessage: todaymenu_add_guide,
    },
    {
      condition: !isTodayMenu,
      btnOnClick: openTodayMenuModal,
      text: '요일 별 달라지는 메뉴가 있다면 오늘의 메뉴로 등록해보세요',
      tipText: '오늘의 메뉴가 뭔가요?',
      tipMessage: todaymenu_guide,
    },
  ];

  return (
    <ThemeProvider theme={defaultTheme}>
      <Box sx={{ display: 'flex' }}>
        <CssBaseline />
        <Nav pages={'오늘의 메뉴'} />
        <Box component="main" sx={dailymenuBoxStyle}>
          <Toolbar />
          <Container maxWidth="xl" sx={{ mt: 4 }}>
            <TodayMenuLayout $isTodayMenu={isTodayMenu}>
              <TodayMenuLeftItems>
                {todaymenu_left_button_and_text_datas.map(
                  (items) =>
                    items.condition && (
                      <>
                        <Button
                          sx={{ ...ButtonStyle, backgroundColor: items.btnBgColor }}
                          disabled={items.isDisabled}
                          color="success"
                          variant="contained"
                          onClick={items.btnOnClick}
                        >
                          등록하기
                        </Button>

                        <span style={dailymenuButtonSpanStyle}>{items.text}</span>

                        <Button
                          sx={{ width: '150px', whiteSpace: 'nowrap' }}
                          color="error"
                          onClick={handleTip({
                            vertical: 'top',
                            horizontal: 'center',
                            message: items.tipMessage,
                          })}
                        >
                          {items.tipText}
                        </Button>
                      </>
                    )
                )}
                <TodayMenuSlider />
              </TodayMenuLeftItems>
              <TodayMenuCalander isTodayMenu={isTodayMenu} />
            </TodayMenuLayout>
          </Container>

          <TodayMenuAddDialog {...todaymenu_dialog_props} />

          <Snackbar
            anchorOrigin={{ vertical, horizontal }}
            open={openTip}
            onClose={closeTip}
            message={message}
            key={vertical + horizontal}
          />
        </Box>
      </Box>
    </ThemeProvider>
  );
}
const defaultTheme = createTheme();

const dailymenuBoxStyle = {
  display: 'flex',
  flexDirection: 'column',
  backgroundColor: 'white',
  flexGrow: 1,
  height: '100%',
  minHeight: '100vh',
  display: 'flex',
  boxSizing: 'border-box',
  paddingBottom: 'var(--copyright-height)',
};

const dailymenuButtonSpanStyle = {
  fontSize: '16px',
  color: 'rgb(27 27 27)',
  marginBottom: '30px',
};

const ButtonStyle = {
  fontWeight: '600',
  fontSize: '16px',
  marginBottom: '10px',
};

const todaymenu_guide =
  '매일 매일 바뀌는 오늘의메뉴는 다양한 요리사나 조리사가 창의적인 아이디어로 준비하는 식사입니다. 이런 음식은 종종 "오늘의 특별 메뉴" 또는 "일일 특선"과 같은 이름으로 불립니다. 이러한 오늘의 메뉴는 식당의 메뉴 다양성을 높이고 고객들에게 새로운 맛과 경험을 제공하기 위해 마련되는 것이 일반적입니다 이렇게 매일 바뀌는 오늘의 메뉴는 고객들에게 루틴을 깨고 새로운 맛을 경험하게 해주며, 음식점의 업적을 증진시키고 경쟁력을 높이는데 도움을 줄 수 있습니다. 또한, 이렇게 다양한 음식을 맛보는 과정에서 식사를 더욱 즐겁게 함으로써 직원들과 손님들 간의 소통과 만족도도 향상시킬 수 있습니다';

const todaymenu_add_guide = `오른쪽 달력에 요일을 클릭하여 일 별 메뉴를 작성해주세요. 요일을 클릭하면 예시가 나옵니다.`;

const TodayMenuLayout = styled.div`
  display: flex;
  justify-content: space-between;
  margin-top: 0;
`;

const TodayMenuLeftItems = styled.div`
  max-width: 50%;
  height: 100%;
  ${flexCenter};
  flex-direction: column;
  margin-top: 10vh;

  @media screen and (max-width: 1200px) {
    display: none;
  }

  span {
    font-size: 20px;
    white-space: nowrap;
    font-weight: 600;
  }
`;
