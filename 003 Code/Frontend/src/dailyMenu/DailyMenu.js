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
import MuiAlert from '@mui/material/Alert';
import Calander from '../components/Calander';
const Alert = React.forwardRef(function Alert(props, ref) {
  return <MuiAlert elevation={6} ref={ref} variant="filled" {...props} />;
});


const cards = [1, 2, 3, 4, 5, 6, 7, 8, 9];

// TODO remove, this demo shouldn't need to reset the theme.
const defaultTheme = createTheme();

export default function DailyMenu() {


  return (
    <ThemeProvider theme={defaultTheme}>
      <Box sx={{display:'flex'}}>
      <CssBaseline />
      <Drawerheader pages={"일품메뉴"}/>
      
        <Box
        component="main"
          sx={{
            display:'flex',
            flexDirection:'column',
            backgroundColor: (theme) =>
              theme.palette.mode === 'light'
                ? theme.palette.grey[100]
                : theme.palette.grey[900],
                flexGrow: 1,
                height: '100vh',
                overflow: 'auto',
                display:'flex',
          }}
        >
          <Toolbar/>
          <Container maxWidth="sm" sx={{mt:4, mb:4}}>
            <Typography variant="h5" align="center" color="text.secondary" paragraph>
            달력에서 요일을 클릭해서 식단을 작성해주세요.
            </Typography>
            <Typography sx={{whiteSpace:'nowrap'}} variant="h5" align="center" color="text.secondary" paragraph>
            달력에 등록된 식단은 소비자가 확인할 수 있습니다.
            </Typography>
            <Stack
              sx={{ pt: 4 }}
              direction="row"
              spacing={2}
              justifyContent="center"
            >
            </Stack>
          </Container>

          <Container sx={{ py: 1 }} maxWidth="md">
          <Calander/>


          <Copyright sx={{ pt: 4 }} />
        </Container>
        </Box>
    </Box>

    

    </ThemeProvider>
  );
}

