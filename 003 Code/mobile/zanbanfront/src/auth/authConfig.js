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
