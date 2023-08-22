import { useCookies } from 'react-cookie';
export const ManagerBaseApi = '/api/manager';

export function ConfigWithToken() {
  const [cookies] = useCookies();
  const config = {
    headers: {
      Authorization: cookies.accesstoken,
    },
  };
  return config;
}

export function ConfigWithRefreshToken() {
  const [cookies] = useCookies();
  const reconfig = {
    headers: {
      Authorization: cookies.refreshtoken,
    },
  };
  return reconfig;
}
