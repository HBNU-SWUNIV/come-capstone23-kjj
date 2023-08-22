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
