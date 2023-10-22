import {
  Avatar,
  Box,
  Button,
  CssBaseline,
  Grid,
  Paper,
  Skeleton,
  TextField,
  ThemeProvider,
  Typography,
  createTheme,
} from '@mui/material';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import axios from 'axios';
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

import { ConfigWithToken, ManagerBaseApi } from '../../auth/authConfig';
import Copyright from '../../components/general/Copyright';
import ErrorInform from '../../components/general/ErrorInform';
import background from '../../image/capstone_background.png';
import UseValidate from '../../hooks/UseValidate';
import UseInput from '../../hooks/UseInput';

export default function InitialLogin() {
  const { data, handleDatas } = UseInput();
  const { error, validateNull } = UseValidate({ data });
  const [image, setImage] = useState([]);
  const isNotnull = error.get('name') == false && error.get('info') == false;
  const navigate = useNavigate();
  const config = ConfigWithToken();
  const formdataConfig = {
    headers: {
      'Content-Type': 'multipart/form-data',
      ...config.headers,
    },
  };

  const marketdatas = {
    name: data.get('name'),
    info: data.get('info'),
  };

  const onSubmit = (e) => {
    e.preventDefault();
    if (!isNotnull) return;

    try {
      if (image !== null) setMarketImage();
      patchMarketInfos({ name: 'name', data: marketdatas?.name, shortsrc: 'title' });
      patchMarketInfos({ name: 'info', data: marketdatas?.info, shortsrc: 'info' });
      navigate('/home');
    } catch {
      console.log('초기설정 에러');
    }
  };

  const patchMarketInfos = ({ name, data, shortsrc }) => {
    const body = {
      [name]: data,
    };
    axios.patch(`${ManagerBaseApi}/store/${shortsrc}`, body, config);
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

  const initialLoginInputDatas = [
    {
      inputname: 'name',
      value: marketdatas?.name,
      label: '식당 명',
      autofocus: true,
      condition: error.get('name'),
      errormessage: '식당명을 입력해주세요.',
    },
    {
      inputname: 'info',
      value: marketdatas?.info,
      label: '식당 소개',
      condition: error.get('info'),
      errormessage: '식당소개를 입력해주세요.',
    },
  ];

  return (
    <ThemeProvider theme={defaultTheme}>
      <Grid container component="main" sx={{ height: '100vh' }}>
        <CssBaseline />
        <Grid item xs={false} sm={4} md={7} sx={initialloginGridStyle} />
        <Grid item xs={12} sm={8} md={5} component={Paper} elevation={6} square>
          <Box sx={initialloginBoxStyle}>
            <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
              <LockOutlinedIcon />
            </Avatar>
            <Typography sx={{ fontWeight: 600 }} component="h1" variant="h4">
              초기 설정
            </Typography>

            <div onClick={() => navigate('/home')}>
              <Typography sx={nextsetuptextStyle} color="error">
                다음에 등록하기
              </Typography>
            </div>

            <Box component="form" noValidate onSubmit={onSubmit} sx={{ mt: 1 }}>
              {initialLoginInputDatas.map((item) => (
                <React.Fragment key={item.inputname}>
                  <TextField
                    margin="normal"
                    required
                    fullWidth
                    id={item.inputname}
                    label={item.label}
                    name={item.inputname}
                    value={item.value}
                    onBlur={validateNull}
                    onChange={handleDatas}
                    autoComplete={item.inputname}
                    autofocus={item.autofocus}
                  />
                  {item.condition && <ErrorInform message={item.errormessage} />}
                </React.Fragment>
              ))}

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
const initialloginGridStyle = {
  backgroundImage: `url(${background})`,
  backgroundRepeat: 'no-repeat',
  backgroundColor: (t) =>
    t.palette.mode === 'light' ? t.palette.grey[50] : t.palette.grey[900],
  backgroundSize: 'cover',
  backgroundPosition: 'top center',
};

const initialloginBoxStyle = {
  my: 8,
  mx: 4,
  display: 'flex',
  flexDirection: 'column',
  alignItems: 'center',
};
const nextsetuptextStyle = {
  cursor: 'pointer',
  fontSize: '15px',
};
