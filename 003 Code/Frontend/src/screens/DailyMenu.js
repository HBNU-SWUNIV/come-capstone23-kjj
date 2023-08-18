import * as React from 'react';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import Copyright from '../components/general/Copyright';
import Toolbar from '@mui/material/Toolbar';
import Calander from '../components/DailyMenu/Calander';
import { styled } from 'styled-components';

const defaultTheme = createTheme();

export const Circle = styled.div`
  width: 0.313rem;
  height: 0.313rem;
  margin-right: 0.313rem;
  border-radius: 0.656rem;
  background-color: #b0b3bc;
`;

const Wrapper = styled.div`
  display: flex;
  justify-content: space-between;
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
    font-size:33px;
    font-family:NotoSans;
    white-space:nowrap;
    font-weight:600;
  }
`;

export default function DailyMenu() {
  return (
    <ThemeProvider theme={defaultTheme}>
      <Box sx={{ display: 'flex' }}>
        <CssBaseline />

        <Box
          component="main"
          sx={{
            display: 'flex',
            flexDirection: 'column',
            backgroundColor: 'white',
            flexGrow: 1,
            height: '100%',
            display: 'flex',
          }}
        >
          <Toolbar />
          <Container maxWidth="xl" sx={{ mt: 4 }}>
            <Wrapper>
              <CalanderText>
                <Typography
                  sx={{ fontFamily: 'SingleDay', fontWeight: '600' }}
                  variant="h2"
                  align="center"
                  color="#0288d1"
                  paragraph
                >
                  오늘의 메뉴는?
                </Typography>
                <span>오른쪽 달력에서 요일을 클릭하여</span>
                <span>요일 별 메뉴를 설정해보세요</span>
              </CalanderText>

              <Calander />
            </Wrapper>
          </Container>

          <Container sx={{ py: 1, pt: 4 }} maxWidth="md">
            <Copyright />
          </Container>
        </Box>
      </Box>
    </ThemeProvider>
  );
}
