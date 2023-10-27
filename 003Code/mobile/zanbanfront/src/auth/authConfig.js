import { useCookies } from 'react-cookie';
export const UserBaseApi = '/api/user';

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
  const [cookies1] = useCookies(['refreshtoken']);
  const config1 = {
    headers: {
      Authorization: `Bearer ${cookies1.refreshtoken}`,
    },
  };
  return config1;
}