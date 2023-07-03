import * as React from 'react';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import FormControlLabel from '@mui/material/FormControlLabel';
import Checkbox from '@mui/material/Checkbox';
import Link from '@mui/material/Link';
import Paper from '@mui/material/Paper';
import Box from '@mui/material/Box';
import Grid from '@mui/material/Grid';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import Copyright from '../components/Copyright';
import background from '../assets/capstone_background.png';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import Skeleton from '@mui/material/Skeleton';


// TODO remove, this demo shouldn't need to reset the theme.

const defaultTheme = createTheme();

export default function Loginfirst() {
const [name,setname] = React.useState('');
const [info,setInfo] = React.useState('');
const [image,setImage] = React.useState([]);
const navigate = useNavigate();

const onSubmit = (e) => {
    e.preventDefault();
    let body={name,info};
    axios.post('/api/manager/setting',body).then(res=>console.log(res))
}


  return (
    <ThemeProvider theme={defaultTheme}>
      <Grid container component="main" sx={{ height: '100vh' }}>
        <CssBaseline />
        <Grid
          item
          xs={false}
          sm={4}
          md={7}
          sx={{
            backgroundImage: `url(${background})`,
            backgroundRepeat: 'no-repeat',
            backgroundColor: (t) =>
              t.palette.mode === 'light' ? t.palette.grey[50] : t.palette.grey[900],
            backgroundSize: 'cover',
            backgroundPosition: 'top center',
          }}
        />
        <Grid item xs={12} sm={8} md={5} component={Paper} elevation={6} square>
          <Box
            sx={{
              my: 8,
              mx: 4,
              display: 'flex',
              flexDirection: 'column',
              alignItems: 'center',
            }}
          >
            <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
              <LockOutlinedIcon />
            </Avatar>
            <Typography component="h1" variant="h4">
                초기 설정
            </Typography>
            
            <Typography color='error'>
                식당명은 초기 설정 후 변경이 불가능하니, 신중히 입력해주세요.
            </Typography>
            <Box component="form" noValidate onSubmit={onSubmit} sx={{ mt: 1 }}>
              <TextField
                margin="normal"
                required
                fullWidth
                id="name"
                label="식당명"
                name="식당명"
                value={name}
                onChange={e => setname(e.target.value)}
                autoComplete="name"
                autoFocus
              />
              <TextField
                margin="normal"
                required
                fullWidth
                value={info}
                onChange={e => setInfo(e.target.value)}
                name="소개"
                label="식당 소개"
                id="info"
                autoComplete="current-info"
              />

            <Typography component='h1' variant='h6'>식당 이미지</Typography>
            <div style={{display:'flex',alignItems:'center'}}>
            <Skeleton variant="rectangular" width={210} height={118} />
            <input style={{marginLeft:'2vw'}} type="file" accept="image/*" onChange={e => setImage(e.target.files[0])}/>
            </div>
            
              <Button
                type="submit"
                fullWidth
                variant="contained"
                sx={{ mt: 3, mb: 2 }}
              >
                등록
              </Button>
              <Copyright sx={{ mt: 5 }} />
            </Box>
          </Box>
        </Grid>
      </Grid>
    </ThemeProvider>
  );
}