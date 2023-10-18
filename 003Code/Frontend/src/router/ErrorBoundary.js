import { AxiosError } from 'axios';
import { useRouteError } from 'react-router-dom';

const ErrorBoundary = (props) => {
  const error = useRouteError();
  error !== undefined && console.log('errorBoundary catch errors =', error);

  if (error instanceof AxiosError) {
    return <>{error?.response?.data}</>;
  } else if (error == undefined) return props.children;

  return <div>예상치 못한 오류가 발생하였습니다. 새로고침 후 다시 이용해주세요.</div>;
};

export default ErrorBoundary;
