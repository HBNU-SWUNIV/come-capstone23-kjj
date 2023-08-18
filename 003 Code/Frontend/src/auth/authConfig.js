import { useCookies } from 'react-cookie';
export const ManagerBaseApi = '/api/manager';

export function ConfigWithToken() {
  const [cookies] = useCookies(['accesstoken']);
  const config = {
    headers: {
      Authorization: `Bearer ${cookies.accesstoken}`,
    },
  };
  return config;
}

export function ConfigWithRefreshToken() {
  const [cookies] = useCookies(['refreshtoken']);
  const reconfig = {
    headers: {
      Authorization: `Bearer ${cookies.refreshtoken}`,
    },
  };
  return reconfig;
}
