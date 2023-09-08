import { BrowserView, MobileView } from 'react-device-detect'
import { Outlet, useLocation } from 'react-router-dom';
import Nav from './Nav/Nav';

function App() {
  const location = useLocation();

  return (
    <div className="App">
      <BrowserView>
        <h1>PC는 지원하지 않습니다.</h1>
        <h1>모바일 환경에서 접속해주세요.</h1>
      </BrowserView>
      <MobileView>
        {!['/', '/login', '/SignUp', '/Guide1', '/Guide2', '/MyUse'].includes(location.pathname) && <Nav />}
        <Outlet />
      </MobileView>
    </div>

  );
}

export default App;
