import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import Paper from '@mui/material/Paper';
import Box from '@mui/material/Box';
import Grid from '@mui/material/Grid';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import Copyright from '../components/general/Copyright';
import background from '../image/capstone_background.png';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import Skeleton from '@mui/material/Skeleton';
import { useState } from 'react';
import { ConfigWithToken, ManagerBaseApi } from '../auth/authConfig';
import ErrorInform from '../components/general/ErrorInform';

export default function Loginfirst() {
  const navigate = useNavigate();
  const config = ConfigWithToken();
  const formdataConfig = {
    headers: {
      'Content-Type': 'multipart/form-data',
      ...config.headers,
    },
  };
  const [name, setname] = useState('');
  const [info, setInfo] = useState('');
  const [image, setImage] = useState([]);
  const [nameError, setNameError] = useState(false);
  const [infoError, setInfoError] = useState(false);

  const onSubmit = (e) => {
    e.preventDefault();
    if (name === '' || info === '') return;
    try {
      if (image !== null) setMarketImage();
      setMarketName();
      setMarketInfo();
      navigate('/home');
    } catch {
      console.log('초기설정 에러');
    }
  };

  const handleError = () => {
    name === '' ? setNameError(true) : setNameError(false);
    info === '' ? setInfoError(true) : setInfoError(false);
  };

  const setMarketName = () => {
    const body = {
      name,
    };
    axios.patch(`${ManagerBaseApi}/store/title`, body, config);
  };

  const setMarketInfo = () => {
    const body = {
      info,
    };
    axios.patch(`${ManagerBaseApi}/store/info`, body, config);
  };

  const setMarketImage = () => {
    const formdata = new FormData();
    image != null && formdata.append('file', image);
    if (image.length !== 0) {
      axios({
        method: 'POST',
        url: `${ManagerBaseApi}/image`,
        data: formdata,
        ...formdataConfig,
      });
    }
  };

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
            <Typography sx={{ fontWeight: 600 }} component="h1" variant="h4">
              초기 설정
            </Typography>

            <div onClick={() => navigate('/home')}>
              <Typography
                sx={{
                  cursor: 'pointer',

                  fontSize: '15px',
                }}
                color="error"
              >
                다음에 등록하기
              </Typography>
            </div>
            <Box component="form" noValidate onSubmit={onSubmit} sx={{ mt: 1 }}>
              <TextField
                margin="normal"
                required
                fullWidth
                id="name"
                label="식당명"
                name="식당명"
                value={name}
                onBlur={handleError}
                onChange={(e) => setname(e.target.value)}
                autoComplete="name"
                autoFocus
              />
              {nameError && <ErrorInform message="식당명을 입력해주세요." />}
              <TextField
                margin="normal"
                required
                fullWidth
                value={info}
                onBlur={handleError}
                onChange={(e) => setInfo(e.target.value)}
                name="소개"
                label="식당 소개"
                id="info"
                autoComplete="current-info"
              />
              {infoError && <ErrorInform message="식당 소개를 입력해주세요." />}

              <Typography
                sx={{ fontWeight: 500, marginTop: '20px' }}
                component="h1"
                variant="h6"
              >
                이미지(선택)
              </Typography>
              <div style={{ display: 'flex', alignItems: 'center' }}>
                <Skeleton variant="rectangular" width={210} height={118} />
                <input
                  style={{ marginLeft: '2vw' }}
                  type="file"
                  accept="image/*"
                  onChange={(e) => setImage(e.target.files[0])}
                />
              </div>

              <Button
                type="submit"
                fullWidth
                variant="contained"
                sx={{ mt: 3, mb: 2, fontWeight: 600 }}
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

const defaultTheme = createTheme();
