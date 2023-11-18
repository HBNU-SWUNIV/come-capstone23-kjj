import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import {
  Avatar,
  Box,
  Button,
  CssBaseline,
  Grid,
  Paper,
  TextField,
  ThemeProvider,
  Typography,
  createTheme,
} from '@mui/material';
import { useKeycloak } from '@react-keycloak/web';
import axios from 'axios';
import React, { useEffect, useState } from 'react';
import { useCookies } from 'react-cookie';
import { useActionData, useNavigate, useNavigation, useSubmit } from 'react-router-dom';
import { useRecoilState } from 'recoil';
import { styled } from 'styled-components';
import { isloginAtom } from '../../atom/loginAtom';
import Copyright from '../../components/general/Copyright';
import ErrorInform from '../../components/general/ErrorInform';
import LoadingDots from '../../components/general/LoadingDots';
import UseInput from '../../hooks/UseInput';
import UseValidate from '../../hooks/UseValidate';
import background from '../../image/capstone_background.png';
import keycloakimg from '../../image/keycloak.png';
import { ConfigWithToken, ManagerBaseApi, validateRules } from '../../utils/utils';
import { flexICenter } from '../../styles/global.style';

export default function Login() {
  const { error, validateWithRules } = UseValidate({ rules: validateRules });
  const { data, handleDatas } = UseInput();
  const [islogined, setIslogined] = useRecoilState(isloginAtom);
  const navigation = useNavigation();
  const navigate = useNavigate();
  const config = ConfigWithToken();
  const actionData = useActionData();
  const { keycloak } = useKeycloak();
  const [cookies, setCookie] = useCookies();
  const submit = useSubmit();

  const [loginError, setLoginError] = useState(false);
  const [keycloakloading, setKeycloakloading] = useState(false);
  const isNotcookies = cookies.accesstoken === '' || cookies.accesstoken === undefined;
  const isSubmitting = navigation.state === 'submitting';

  const setkeycloakToken = async () => {
    setKeycloakloading(true);
    const response = await axios.post(`/api/user/login/keycloak?token=${keycloak.token}`);
    const keycloaktoken = response.headers.authorization;
    setCookie('accesstoken', keycloaktoken);
  };

  const verifyFirstLogin = () => {
    axios
      .get(`${ManagerBaseApi}/store/setting`, config)
      .then((res) => {
        if (res.data.name === '' && res.data.info === '') navigate('/initialLogin');
        else navigate('/home');
      })
      .catch((err) => console.error(err));
  };

  const onLogin = (e) => {
    e.preventDefault();
    submit(
      {
        username: data?.get('username'),
        password: data?.get('password'),
      },
      { method: 'POST' }
    );
  };

  useEffect(() => {
    if (actionData?.ok) {
      const accesstoken = actionData.response.headers.authorization;
      const refreshtoken = actionData.response.headers.refresh_token;
      setCookie('accesstoken', accesstoken);
      setCookie('refreshtoken', refreshtoken);
      setIslogined(true);
    } else if (actionData?.ok === false) setLoginError(true);
  }, [actionData]);

  useEffect(() => {
    if (islogined && !isNotcookies) verifyFirstLogin();
  }, [islogined, cookies.accesstoken]);

  useEffect(() => {
    if (keycloak.authenticated) {
      if (!isNotcookies) verifyFirstLogin();
      else setkeycloakToken();
    }
  }, [keycloak.authenticated, cookies.accesstoken]);

  const loginformdatas = [
    {
      name: 'username',
      autoFocus: true,
      errorCondition: loginError,
      errormessage: '올바른 ID 또는 Password를 입력하세요.',
      onchange: handleDatas,
    },
    {
      name: 'password',
      type: 'password',
      errorCondition: error.get('password'),
      errormessage: error.get('password'),
      onBlur: validateWithRules,
      onchange: handleDatas,
    },
  ];

  return (
    <ThemeProvider theme={defaultTheme}>
      <Grid container component="main" sx={{ height: '100vh' }}>
        <CssBaseline />
        <Grid item xs={false} sm={4} md={7} sx={logingridstyle} />
        <Grid item xs={12} sm={8} md={5} component={Paper} elevation={6} square>
          <Box sx={lockboxstyle}>
            <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
              <LockOutlinedIcon />
            </Avatar>
            <Typography component="h1" variant="h5" sx={{ fontWeight: '600' }}>
              식재료 절약단
            </Typography>

            <Box component="form" noValidate onSubmit={onLogin} sx={{ mt: 1 }}>
              {loginformdatas.map((item, idx) => (
                <React.Fragment key={'loginkey=' + idx}>
                  <TextField
                    margin="normal"
                    required
                    fullWidth
                    id={item.name}
                    label={item.name}
                    name={item.name}
                    type={item.type}
                    onBlur={item.onBlur}
                    onChange={item.onchange}
                    autoComplete={item.name}
                  />
                  {item.errorCondition && <ErrorInform message={item.errormessage} />}
                </React.Fragment>
              ))}

              <>
                <Button type="submit" fullWidth variant="contained" sx={loginbuttonstyle}>
                  {isSubmitting ? <LoadingDots /> : '로그인'}
                </Button>

                <HrWrapper>
                  <div />
                  <span>또는</span>
                  <div />
                </HrWrapper>

                <Button
                  onClick={() => keycloak.login()}
                  fullWidth
                  variant="contained"
                  color="inherit"
                  sx={keycloakbuttonstyle}
                >
                  <img style={keycloakimgstyle} src={keycloakimg} />

                  {keycloakloading ? <LoadingDots isGray={true} /> : 'Keycloak 로그인'}
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

const lockboxstyle = {
  my: 8,
  mx: 4,
  display: 'flex',
  flexDirection: 'column',
  alignItems: 'center',
};

const logingridstyle = {
  backgroundImage: `url(${background})`,
  backgroundRepeat: 'no-repeat',
  backgroundColor: (t) =>
    t.palette.mode === 'light' ? t.palette.grey[50] : t.palette.grey[900],
  backgroundSize: 'cover',
  backgroundPosition: 'top center',
};

const loginbuttonstyle = {
  mt: 3,
  mb: 2,
  fontFamily: 'Nanum',
  fontWeight: '600',
  display: 'flex',
  justifyContent: 'center',
  alignItems: 'center',
};

const keycloakbuttonstyle = {
  mt: 2,
  mb: 2,
  fontWeight: '600',
  position: 'relative',
  backgroundColor: 'white',
  color: 'black',
};

const keycloakimgstyle = {
  width: '30px',
  height: '30px',
  position: 'absolute',
  left: 0,
};

const HrWrapper = styled.div`
  ${flexICenter};
  justify-content: space-around;
  padding: 10px 0;

  div {
    width: 45%;
    height: 1px;
    background-color: #717171;
  }

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
