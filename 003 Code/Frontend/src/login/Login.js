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
import { ConfigWithToken, ManagerBaseApi } from '../auth/authConfig';
import { useEffect, useState } from 'react';
import { useRecoilState } from 'recoil';
import { isloginAtom } from '../atom/loginAtom';
import ErrorInform from '../components/general/ErrorInform';
import LoadingCircle from '../components/general/LoadingCircle';

export default function Login() {
  const navigate = useNavigate();
  const [cookies, setCookie] = useCookies();

  const config = ConfigWithToken();

  const { keycloak } = useKeycloak();

  const onKeyCloakLogin = () => {
    keycloak.login();
  };

  const setKeyCloakToken = async () => {
    const response = await axios.post(`/api/user/login/keycloak?token=${keycloak.token}`);
    const Header = response.headers.authorization;
    const [, keycloaktoken] = Header.split('Bearer ');
    setCookie('accesstoken', keycloaktoken);
  };

  useEffect(() => {
    if (keycloak.authenticated) {
      cookies.accesstoken !== '' && navigate('/home');
      cookies.accesstoken === '' && setKeyCloakToken();
    }
    if ((islogin || keycloak.authenticated) && cookies.accesstoken !== '') {
      verifyFirstLogin();
    }
  }, [keycloak.authenticated, cookies.accesstoken]);

  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [islogin, setIsLogin] = useRecoilState(isloginAtom);
  const [loginError, setLoginError] = useState(false);
  const [passwordError, setPasswordError] = useState(false);

  const verifyPassword = () => {
    if (password.length < 8) setPasswordError(true);
    else setPasswordError(false);
  };
  const setLoginToken = async () => {
    let body = { username, password };
    try {
      const response = await axios({
        method: 'POST',
        url: `${ManagerBaseApi}/login/id`,
        data: body,
      });

      const token = response.headers.authorization;
      setCookie('accesstoken', token);
      setIsLogin(true);

      const refreshtoken = response.headers.refresh_token;
      setCookie('refreshtoken', refreshtoken);
    } catch (error) {
      if (error.response.status === 401) setLoginError(true);
    }
  };

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
            <Typography
              component="h1"
              variant="h5"
              sx={{ fontFamily: 'Nanum', fontWeight: '600' }}
            >
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
              {loginError && (
                <ErrorInform message="올바른 ID 또는 Password를 입력하세요." />
              )}
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
                onBlur={verifyPassword}
              />
              {passwordError && (
                <ErrorInform message="비밀번호는 최소 7자 이상이여야 합니다." />
              )}

              <>
                <Button
                  type="submit"
                  fullWidth
                  variant="contained"
                  sx={{
                    mt: 3,
                    mb: 2,
                    fontFamily: 'Nanum',
                    fontWeight: '600',
                  }}
                >
                  로그인
                  {/* <LoadingCircle /> */}
                </Button>
                <Button
                  onClick={onKeyCloakLogin}
                  fullWidth
                  variant="contained"
                  sx={{ mb: 2, fontFamily: 'Nanum', fontWeight: '600' }}
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
