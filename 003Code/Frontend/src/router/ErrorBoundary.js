import { useEffect } from 'react';
import { useRouteError } from 'react-router-dom';
import { useRecoilValue } from 'recoil';
import { isloginAtom } from '../atom/loginAtom';
import { useKeycloak } from '@react-keycloak/web';
import { useCookies } from 'react-cookie';
import { ConfigWithRefreshToken } from '../auth/authConfig';
import { reIssuetoken } from '../api/reissue';
import styled from 'styled-components';

const Wrapper = styled.div`
  width: 100vw;
  height: 100vh;

  display: flex;
  justify-content: center;
  align-items: center;

  color: green;
  font-size: 24px;
  font-weight: 600;
`;

const ErrorBoundary = () => {
  const error = useRouteError();

  const islogin = useRecoilValue(isloginAtom);
  const { keycloak } = useKeycloak();
  const [cookies, setCookie] = useCookies();
  const configWithRefreshtoken = ConfigWithRefreshToken();

  error !== undefined && console.log('errorBoundary catch errors =', error);

  useEffect(() => {
    reIssuetoken(setCookie, configWithRefreshtoken);
  }, []);

  return (
    <Wrapper>예상치 못한 오류가 발생하였습니다. 새로고침 후 다시 이용해주세요.</Wrapper>
  );
};

export default ErrorBoundary;
