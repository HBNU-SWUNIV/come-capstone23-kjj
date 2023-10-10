import { createTheme, ThemeProvider } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Container from '@mui/material/Container';
import Grid from '@mui/material/Grid';
import Paper from '@mui/material/Paper';
import PredictFoodChart from '../components/Main/PredictFoodChart';
import StatisticsChart from '../components/Main/StatisticsChart';
import MenuStatisticsChart from '../components/Main/MenuStatisticsChart';
import Drawerheader from '../components/Drawerheader/Drawerheader';
import OrderingFoodStatusChart from '../components/Main/OrderingFoodStatusChart';
import RevenueChart from '../components/Main/RevenueChart';

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
    chart: <OrderingFoodStatusChart />,
  },
  {
    id: 1,
    xs: 12,
    md: 4,
    lg: 3,
    chart: <StatisticsChart />,
  },
  {
    id: 2,
    xs: 12,
    md: 6,
    lg: 6,
    chart: <PredictFoodChart />,
  },
  {
    id: 3,
    xs: 12,
    md: 6,
    lg: 6,
    chart: <RevenueChart />,
  },
  {
    id: 4,
    xs: 12,
    chart: <MenuStatisticsChart />,
  },
];

export default function Dashboard() {
  return (
    <ThemeProvider theme={defaultTheme}>
      <Box sx={{ display: 'flex' }}>
        <CssBaseline />
        <Drawerheader pages={'홈'} />

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
  backgroundColor: 'white',
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
