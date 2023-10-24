import { Link } from "react-router-dom";

const ErrorBoundary = () => {

  return <div style={{ marginTop: '20vh', textAlign: 'center'}}>
    <h1 style={{ marginBottom: 0}}>잘못된 접근입니다.</h1>
    <p style={{marginTop: 0}}>지속적인 문제 발생시 관리자에게 문의하세요</p>
    <Link to='/home'>
      <button > 홈으로 돌아가기</button>
    </Link>
  </div>;
};

export default ErrorBoundary;