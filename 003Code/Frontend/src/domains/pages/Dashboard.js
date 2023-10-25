import Box from '@mui/material/Box';
import Container from '@mui/material/Container';
import CssBaseline from '@mui/material/CssBaseline';
import Grid from '@mui/material/Grid';
import Paper from '@mui/material/Paper';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import Toolbar from '@mui/material/Toolbar';
import Nav from '../nav/Nav';
import FoodStatisticsChart from '../../components/charts/FoodStatisticsChart';
import UserStatisticsChart from '../../components/charts/UserStatisticsChart';
import FoodNeccesaryChart from '../../components/charts/FoodNeccesaryChart';
import UserReservationChart from '../../components/charts/UserReservationChart';
import FoodSavingStatusChart from '../../components/charts/FoodSavingStatusChart';

const GridItem = ({ xs, md, lg, chart }) => {
  return (
    <Grid item xs={xs} md={md} lg={lg}>
      <Paper sx={paperStyle}>{chart}</Paper>
    </Grid>
  );
};

const gridItems = [
  {
    id: 0,
    xs: 12,
    md: 8,
    lg: 9,
    chart: <FoodSavingStatusChart />,
  },
  {
    id: 1,
    xs: 12,
    md: 4,
    lg: 3,
    chart: <UserStatisticsChart />,
  },
  {
    id: 2,
    xs: 12,
    md: 8,
    lg: 8,
    chart: <FoodNeccesaryChart />,
  },
  {
    id: 3,
    xs: 12,
    md: 4,
    lg: 4,
    chart: <FoodStatisticsChart />,
  },
  // {
  //   id: 4,
  //   xs: 12,
  //   md: 6,
  //   lg: 6,
  //   chart: <UserReservationChart />,
  // },
];

export default function Dashboard() {
  return (
    <ThemeProvider theme={defaultTheme}>
      <Box sx={{ display: 'flex' }}>
        <CssBaseline />
        <Nav pages={'홈'} />

        <Box component="main" sx={boxStyle}>
          <Toolbar />
          <Container maxWidth="xl" sx={{ mt: 4, mb: 4 }}>
            <Grid container spacing={3}>
              {gridItems.map((item) => (
                <GridItem
                  key={item.id}
                  xs={item.xs}
                  md={item.md}
                  lg={item.lg}
                  chart={item.chart}
                />
              ))}
            </Grid>
          </Container>
        </Box>
      </Box>
    </ThemeProvider>
  );
}

const defaultTheme = createTheme();

const boxStyle = {
  backgroundColor: '#ffffff',
  flexGrow: 1,
  minHeight: '100vh',
  overflow: 'scroll',
  boxSizing: 'border-box',
  paddingBottom: 'var(--copyright-height)',
};

const paperStyle = {
  height: '100%',
  minHeight: '30vh',
  p: 2,
  display: 'flex',
  flexDirection: 'column',
};
