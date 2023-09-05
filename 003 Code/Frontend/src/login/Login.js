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
import background from '../image/capstone_background.png';
import { useActionData, useNavigate, useNavigation, useSubmit } from 'react-router-dom';
import axios from 'axios';
import { useKeycloak } from '@react-keycloak/web';
import { useCookies } from 'react-cookie';
import Copyright from '../components/general/Copyright';
import { ConfigWithToken, ManagerBaseApi } from '../auth/authConfig';
import { useEffect, useState } from 'react';
import { useRecoilState } from 'recoil';
import { isloginAtom } from '../atom/loginAtom';
import ErrorInform from '../components/general/ErrorInform';
import keycloakimg from '../image/keycloak.png';
import { styled } from 'styled-components';
import LoadingDots from '../components/general/LoadingDots';

export default function Login() {
  const navigation = useNavigation();
  const navigate = useNavigate();
  const actionData = useActionData();
  const [cookies, setCookie] = useCookies();

  const submit = useSubmit();
  const isSubmitting = navigation.state === 'submitting';

  useEffect(() => {
    if (actionData?.ok) {
      const token = actionData.response.headers.authorization;
      setCookie('accesstoken', token);
      setIsLogin(true);

      const refreshtoken = actionData.response.headers.refresh_token;
      setCookie('refreshtoken', refreshtoken);
    } else if (actionData?.ok === false) {
      setLoginError(true);
      verifyPassword();
    }
  }, [actionData]);

  const config = ConfigWithToken();
  const { keycloak } = useKeycloak();
  const onKeyCloakLogin = () => {
    keycloak.login();
  };
  const setKeyCloakToken = async () => {
    const response = await axios.post(`/api/user/login/keycloak?token=${keycloak.token}`);
    const keycloaktoken = response.headers.authorization;
    console.log(keycloaktoken);
    setCookie('accesstoken', keycloaktoken);
  };

  useEffect(() => {
    if (keycloak.authenticated) {
      (cookies.accesstoken == undefined || '') && setKeyCloakToken();
      cookies.accesstoken !== '' && navigate('/home');
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
  const verifyFirstLogin = () => {
    axios
      .get(`${ManagerBaseApi}/setting`, config)
      .then((res) => {
        if (res.data.name === '' && res.data.info === '') navigate('/loginfirst');
        else navigate('/home');
      })
      .catch((err) => console.error(err));
  };
  const onSubmit = (e) => {
    e.preventDefault();
    submit({ username, password }, { method: 'POST' });
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
                    display: 'flex',
                    justifyContent: 'center',
                    alignItems: 'center',
                  }}
                >
                  {isSubmitting ? <LoadingDots /> : '로그인'}
                </Button>

                <HrWrapper>
                  <div />
                  <span>또는</span>
                  <div />
                </HrWrapper>

                <Button
                  onClick={onKeyCloakLogin}
                  fullWidth
                  variant="contained"
                  color="inherit"
                  sx={{
                    mt: 2,
                    mb: 2,
                    fontFamily: 'Nanum',
                    fontWeight: '600',
                    position: 'relative',
                    backgroundColor: 'white',
                    color: 'black',
                  }}
                >
                  <img
                    style={{
                      width: '30px',
                      height: '30px',
                      position: 'absolute',
                      left: 0,
                    }}
                    src={keycloakimg}
                  />
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

const HrWrapper = styled.div`
  padding: 10px 0;

  div {
    width: 45%;
    height: 1px;
    background-color: #717171;
  }

  display: flex;
  justify-content: space-around;
  align-items: center;

  span {
    font-size: 16px;
    color: #717171;
  }
`;

export async function action({ request }) {
  const formData = await request.formData();

  const username = formData.get('username');
  const password = formData.get('password');

  try {
    const response = await axios({
      method: 'POST',
      url: `${ManagerBaseApi}/login/id`,
      data: { username, password },
    });
    return { ok: true, response };
  } catch {
    return { ok: false };
  }
}
