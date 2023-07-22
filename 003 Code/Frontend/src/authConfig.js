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
