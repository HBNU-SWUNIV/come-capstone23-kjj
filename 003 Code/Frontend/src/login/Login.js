import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import FormControlLabel from '@mui/material/FormControlLabel';
import Checkbox from '@mui/material/Checkbox';
import Paper from '@mui/material/Paper';
import Box from '@mui/material/Box';
import Grid from '@mui/material/Grid';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import background from '../image/capstone_background.png';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { useKeycloak } from '@react-keycloak/web';
import { useCookies } from 'react-cookie';
import Copyright from '../components/general/Copyright';
import {
  ConfigWithRefreshToken,
  ConfigWithToken,
  ManagerBaseApi,
} from '../auth/authConfig';
import { useEffect, useState } from 'react';
import { useSetRecoilState } from 'recoil';
import { isloginAtom } from '../atom/loginAtom';

export default function Login() {
  const navigate = useNavigate();

  const config = ConfigWithToken();
  // const reconfig = ConfigWithRefreshToken();

  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const { keycloak } = useKeycloak();
  const [cookies, setCookie] = useCookies();
  const setIsLogin = useSetRecoilState(isloginAtom);

  const onKeyCloakLogin = () => {
    keycloak.login();
  };

  useEffect(() => {
    if (keycloak.authenticated) {
      cookies.accesstoken !== '' && navigate('/home');
      cookies.accesstoken === '' && setKeyCloakToken();
    }
    if (cookies.accesstoken !== '') {
      verifyFirstLogin();
    }
  }, [keycloak.authenticated, cookies.accesstoken]);

  const setKeyCloakToken = async () => {
    const response = await axios.post(`/api/user/login/keycloak?token=${keycloak.token}`);
    const Header = response.headers.authorization;
    const [, keycloaktoken] = Header.split('Bearer ');
    setCookie('accesstoken', keycloaktoken);
  };

  const setLoginToken = async () => {
    let body = { username, password };
    try {
      const response = await axios({
        method: 'POST',
        url: `${ManagerBaseApi}/login/id`,
        data: body,
      });

      const Header = response.headers.authorization;
      const [, token] = Header.split('Bearer ');
      setCookie('accesstoken', token);
      setIsLogin(true);

      // 리프레쉬 토큰 -> 엑세스 토큰 반환 = 백엔드 구현끝나면 수정 필요.
      // const refresh_header = response.headers.refresh_token;
      // const [, refreshtoken] = refresh_header.split('Bearer ');
      // setCookie('refreshtoken', refreshtoken);
      // setTimeout(() => {
      //   RefreshTokenHandler();
      // }, 5000);
    } catch (error) {
      if (error.response.status === 401) alert('ID 또는 Password가 다릅니다.');
    }
  };

  // const RefreshTokenHandler = async () => {
  //   const res = await axios({
  //     method: 'POST',
  //     url: `/api/user/login/refresh`,
  //     ...reconfig,
  //   });
  // };

  const verifyFirstLogin = () => {
    axios
      .get(`${ManagerBaseApi}/setting`, config)
      .then((res) => {
        if (res.data.name === '' && res.data.info === '') navigate('/loginfirst');
        else navigate('/home');
      })
      .catch((err) => console.error(err));
  };
  const onSubmit = async (e) => {
    e.preventDefault();
    setLoginToken();
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
            <Typography component="h1" variant="h5">
              식재료 절약단
            </Typography>
            <Box component="form" noValidate onSubmit={onSubmit} sx={{ mt: 1 }}>
              <TextField
                margin="normal"
                required
                fullWidth
                id="ID"
                label="ID"
                name="ID"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                autoComplete="ID"
                autoFocus
              />
              <TextField
                margin="normal"
                required
                fullWidth
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                name="password"
                label="Password"
                type="password"
                id="password"
                autoComplete="current-password"
              />
              <FormControlLabel
                control={<Checkbox value="remember" color="primary" />}
                label="Remember me"
              />
              <>
                <Button type="submit" fullWidth variant="contained" sx={{ mt: 3, mb: 2 }}>
                  로그인
                </Button>
                <Button
                  onClick={onKeyCloakLogin}
                  fullWidth
                  variant="contained"
                  sx={{ mb: 2 }}
                >
                  Keycloak 로그인
                </Button>
              </>

              <Copyright sx={{ mt: 5 }} />
            </Box>
          </Box>
        </Grid>
      </Grid>
    </ThemeProvider>
  );
}

const defaultTheme = createTheme();
