import { useCookies } from 'react-cookie';

export function ConfigWithToken(): any { // 실제 반환 타입을 지정하는 것이 좋습니다.
  const [cookies] = useCookies(['accesstoken']);
  const config = {
    headers: {
      Authorization: `Bearer ${cookies.accesstoken}`,
    },
  };
  return config;
}

export const UserBaseApi: string = '/api/user';
