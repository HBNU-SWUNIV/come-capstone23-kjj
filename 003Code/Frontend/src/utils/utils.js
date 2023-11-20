import axios from 'axios';
import { useCookies } from 'react-cookie';

export const ManagerBaseApi = '/api/manager';

export const reissueToken = async (setCookie, config) => {
  const expiration = new Date();
  expiration.setTime(expiration.getTime() + 14 * 60 * 1000);

  try {
    const res = await axios({
      method: 'POST',
      url: '/api/user/login/refresh',
      ...config,
    });

    if (res.headers.authorization !== '')
      setCookie('accesstoken', res.headers.authorization, {
        expires: expiration,
      });
  } catch (err) {
    console.log('reissue error 발생!', err);
  }
};

export const validateRules = {
  password: (value) => {
    if (!value) return '비밀번호를 입력해주세요';
    else if (value.length < 4) return '비밀번호는 최소 4자리 이상 입력해주세요';
    return '';
  },
};

export const ConfigWithToken = () => {
  const [cookies] = useCookies();
  const config = {
    headers: {
      Authorization: cookies.accesstoken,
    },
  };
  return config;
};

export const ConfigWithRefreshToken = () => {
  const [cookies] = useCookies();
  const reconfig = {
    headers: {
      Authorization: cookies.refreshtoken,
    },
  };
  return reconfig;
};
