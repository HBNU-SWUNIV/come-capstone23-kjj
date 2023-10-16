import { useEffect } from "react";
import axios from "axios";
import { useCookies } from 'react-cookie';
import { getCookie } from './cookieUtility';
import { ConfigWithToken } from './authConfig';

export default function TokenRefresher() {
  const config = ConfigWithToken();
  const [, setCookie] = useCookies(['accesstoken', 'refreshtoken']);

  useEffect(() => {
    const refreshToken = getCookie("refreshtoken");

    const refreshAccessToken = async () => {
      try {
        const response = await axios.post(
          `/api/user/login/refresh`,
          null, // 요청 데이터가 필요 없을 경우 null 또는 {}
          {
            headers: {
              Authorization: `Bearer ${refreshToken}`
            },
          }
        );

        const token = response.headers.authorization;
        const [, newAccessToken] = token.split('Bearer ');
        setCookie('accesstoken', newAccessToken);

        const newRefreshToken = response.headers.refresh_token;
        const [, newRefreshTokenValue] = newRefreshToken.split('Bearer ');
        setCookie('refreshtoken', newRefreshTokenValue);
        console.log(newAccessToken)
      } catch (error) {
        console.error('토큰 재발급 에러 발생:', error);
      }
    };

    // 초기 실행과 25분 간격으로 실행
    // 25 * 60 * 1000 = 25분을 밀리초 단위로 표현
    refreshAccessToken();
    const intervalId = setInterval(refreshAccessToken, 25 * 60 * 1000);

    // 인터벌 초기화
    return () => {
      clearInterval(intervalId);
    };
  }, []);

  return <></>;
}
