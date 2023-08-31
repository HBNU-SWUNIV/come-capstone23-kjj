import { createTheme, ThemeProvider } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Container from '@mui/material/Container';
import Grid from '@mui/material/Grid';
import Paper from '@mui/material/Paper';
import Chart from '../components/Main/Chart';
import Statistics from '../components/Main/Statistics';
import Chart2 from '../components/Main/Chart2';
import Drawerheader from '../components/Drawerheader';

export default function Dashboard() {
  return (
    <ThemeProvider theme={defaultTheme}>
      <Box sx={{ display: 'flex' }}>
        <CssBaseline />
        <Drawerheader pages={'홈'} />

        <Box
          component="main"
          sx={{
            backgroundColor: 'white',
            flexGrow: 1,
            minHeight: '100vh',
            overflow: 'scroll',
            boxSizing: 'border-box',
            paddingBottom: 'var(--copyright-height)',
          }}
        >
          <Toolbar />
          <Container maxWidth="xl" sx={{ mt: 4, mb: 4 }}>
            <Grid container spacing={3}>
              <Grid item xs={12} md={8} lg={9}>
                <Paper
                  sx={{
                    height: '100%',
                    minHeight: '30vh',
                    p: 2,
                    display: 'flex',
                    flexDirection: 'column',
                  }}
                >
                  <Chart />
                </Paper>
              </Grid>

              <Grid item xs={12} md={4} lg={3}>
                <Paper
                  sx={{
                    p: 2,
                    display: 'flex',
                    flexDirection: 'column',
                    minHeight: '36vh',
                    height: '100%',
                  }}
                >
                  <Statistics />
                </Paper>
              </Grid>

              <Grid item xs={12}>
                <Paper
                  sx={{
                    height: '100%',
                    minHeight: '30vh',
                    p: 2,
                    display: 'flex',
                    flexDirection: 'column',
                  }}
                >
                  <Chart2 />
                </Paper>
              </Grid>
            </Grid>
          </Container>
        </Box>
      </Box>
    </ThemeProvider>
  );
}

const defaultTheme = createTheme();
