import * as React from 'react';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import Stack from '@mui/material/Stack';
import Drawerheader from '../components/Drawerheader';
import Copyright from '../components/Copyright';
import Toolbar from '@mui/material/Toolbar';
import Snackbar from '@mui/material/Snackbar';
import Calander2 from '../components/Calander2';
import Button from '@mui/material/Button';

const defaultTheme = createTheme();

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
        <Drawerheader pages={'휴일설정'} />

        <Box
          component="main"
          sx={{
            display: 'flex',
            flexDirection: 'column',
            backgroundColor: (theme) =>
              theme.palette.mode === 'light'
                ? theme.palette.grey[100]
                : theme.palette.grey[900],
            flexGrow: 1,
            height: '100vh',
            overflow: 'auto',
            display: 'flex',
          }}
        >
          <Toolbar />
          <Container
            maxWidth="sm"
            sx={{ mt: 4, mb: 4, display: 'flex', flexDirection: 'column' }}
          >
            <Typography variant="h5" align="center" color="text.secondary" paragraph>
              달력에서 요일을 클릭해서 휴일을 설정해주세요.
            </Typography>
            <Typography
              sx={{ whiteSpace: 'nowrap' }}
              variant="h5"
              align="center"
              color="text.secondary"
              paragraph
            >
              휴일은 달력에 빨간색으로 표시됩니다.
            </Typography>
            <Button
              sx={{ width: '100%' }}
              color="error"
              onClick={handleClick({ vertical: 'top', horizontal: 'center' })}
            >
              주의사항 보기
            </Button>
            <Stack
              sx={{ pt: 4 }}
              direction="row"
              spacing={2}
              justifyContent="center"
            ></Stack>
          </Container>

          <Container sx={{ py: 1 }} maxWidth="md">
            <Calander2 />

            <Copyright sx={{ pt: 4 }} />
          </Container>
        </Box>
        <Snackbar
          anchorOrigin={{ vertical, horizontal }}
          open={open}
          onClose={handleClose}
          message="예측 데이터는 오전 10시 30분에 생성됩니다. 따라서 익일 예측 데이터를 확인하려면 전날 영업일의 마감 시간인 오전 10시 30분 이전까지 영업일 설정을 유지해야 합니다. 이를테면, 전날 영업일 마감 시간인 오전 10시 30분 이후에 영업일로 설정하면, 예측 데이터는 메인 페이지에 올바르게 표시되지 않을 수 있습니다."
          key={vertical + horizontal}
        />
      </Box>
    </ThemeProvider>
  );
}
