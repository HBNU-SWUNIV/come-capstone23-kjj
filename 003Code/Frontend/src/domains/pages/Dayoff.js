import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Container from '@mui/material/Container';
import CssBaseline from '@mui/material/CssBaseline';
import Snackbar from '@mui/material/Snackbar';
import Stack from '@mui/material/Stack';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import * as React from 'react';
import Nav from '../nav/Nav';
import DayoffCalander from '../../components/dayoff/DayoffCalander';

export default function Dayoff() {
  const [state, setState] = React.useState({
    open: false,
    vertical: 'top',
    horizontal: 'center',
  });
  const { vertical, horizontal, open } = state;

  const handleClick = (newState) => () => {
    setState({ ...newState, open: true });
  };

  const handleClose = () => {
    setState({ ...state, open: false });
  };

  return (
    <ThemeProvider theme={defaultTheme}>
      <Box sx={{ display: 'flex' }}>
        <CssBaseline />
        <Nav pages={'휴일설정'} />

        <Box component="main" sx={boxStyle}>
          <Toolbar />
          <Container maxWidth="xl" sx={containerStyle}>
            <span style={spanStyle}>
              우리 식당에 휴일을 추가적으로 등록할 수 있습니다
            </span>

            <Button
              sx={buttonStyle}
              color="error"
              onClick={handleClick({ vertical: 'top', horizontal: 'center' })}
            >
              주의사항 보기
            </Button>
          </Container>

          <Container sx={{ py: 1 }} maxWidth="xl">
            <DayoffCalander />
          </Container>
        </Box>

        <Snackbar
          anchorOrigin={{ vertical, horizontal }}
          open={open}
          onClose={handleClose}
          message={WarningMessage}
          key={vertical + horizontal}
        />
      </Box>
    </ThemeProvider>
  );
}

const defaultTheme = createTheme();

const WarningMessage =
  '우리 예측 데이터는 오전 10시 30분에 생성됩니다. 따라서 익일 예측 데이터를 확인하려면 전날 영업일의 마감 시간인 오전 10시 30분 이전까지 영업일 설정을 유지해야 합니다. 이를테면, 전날 영업일 마감 시간인 오전 10시 30분 이후에 영업일로 설정하면, 예측 데이터는 메인 페이지에 올바르게 표시되지 않을 수 있습니다.';

const spanStyle = {
  fontFamily: 'NotoSans',
  fontSize: '16px',
  fontWeight: '600',
  color: 'rgb(0, 171, 85)',
};

const boxStyle = {
  display: 'flex',
  flexDirection: 'column',
  backgroundColor: 'white',
  flexGrow: 1,
  minHeight: '100vh',
  width: '100%',
  height: '100%',
  overflow: 'auto',
  boxSizing: 'border-box',
  paddingBottom: 'var(--copyright-height)',
};

const containerStyle = {
  mt: 4,
  display: 'flex',
  width: '100%',
  justifyContent: 'space-between',
  alignItems: 'center',
  position: 'relative',
};

const buttonStyle = {
  width: '100px',
  right: '60px',
  position: 'absolute',
};
