import { useEffect } from 'react';
import { useCookies } from 'react-cookie';
import { useRouteError } from 'react-router-dom';
import styled from 'styled-components';
import { flexCenter, fullScreen } from '../styles/global.style';
import { ConfigWithRefreshToken, reissueToken } from '../utils/utils';

const ErrorBoundary = () => {
  const error = useRouteError();

  const [cookies, setCookie] = useCookies();
  const configWithRefreshtoken = ConfigWithRefreshToken();

  error !== undefined && console.log('errorBoundary catch errors =', error);

  useEffect(() => {
    reissueToken(setCookie, configWithRefreshtoken);
  }, []);

  return (
    <ErrorBoundaryLayout>
      예상치 못한 오류가 발생하였습니다. 새로고침 후 다시 이용해주세요.
    </ErrorBoundaryLayout>
  );
};

export default ErrorBoundary;

const ErrorBoundaryLayout = styled.div`
  ${fullScreen};
  ${flexCenter};

  color: green;
  font-size: 24px;
  font-weight: 600;
`;
