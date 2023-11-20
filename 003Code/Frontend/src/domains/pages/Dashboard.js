import Box from '@mui/material/Box';
import Container from '@mui/material/Container';
import CssBaseline from '@mui/material/CssBaseline';
import Grid from '@mui/material/Grid';
import Paper from '@mui/material/Paper';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import Toolbar from '@mui/material/Toolbar';
import FoodSavingStatusChart from '../../components/dashboard/FoodSavingStatusChart';
import FoodStatisticsChart from '../../components/dashboard/FoodStatisticsChart';
import NeedFoods from '../../components/dashboard/NeedFoods';
import StatisticsWrap from '../../components/StatisticsWrap';
import Nav from '../nav/Nav';

const GridItem = (props) => {
  return (
    <Grid item xs={props.xs} md={props.md} lg={props.lg}>
      <Paper sx={paperStyle}>{props.chart}</Paper>
    </Grid>
  );
};

const gridItems = [
  {
    id: 1,
    xs: 12,
    md: 6,
    lg: 5,
    chart: <StatisticsWrap />,
  },
  {
    id: 0,
    xs: 12,
    md: 6,
    lg: 7,
    chart: <FoodSavingStatusChart />,
  },

  {
    id: 2,
    xs: 12,
    md: 6,
    lg: 8,
    chart: <NeedFoods />,
  },
  {
    id: 3,
    xs: 12,
    md: 6,
    lg: 4,
    chart: <FoodStatisticsChart />,
  },
];

export default function Dashboard() {
  return (
    <ThemeProvider theme={defaultTheme}>
      <Box sx={{ display: 'flex' }}>
        <CssBaseline />
        <Nav pages={'홈'} />

        <Box component="main" sx={boxStyle}>
          <Toolbar />

          <Container maxWidth="xl" sx={containerStyle}>
            <Grid container spacing={3}>
              {gridItems.map((item) => (
                <GridItem key={item.id} {...item} />
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
  backgroundColor: 'rgb(245 247 252)',
  display: 'flex',
  flexDirection: 'column',
  alignItems: 'center',
};

const paperStyle = {
  height: '100%',
  minHeight: '30vh',
  p: 2,
  display: 'flex',
  flexDirection: 'column',
};

const containerStyle = {
  mt: 4,
  mb: 4,
};
