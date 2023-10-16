/* eslint-diable */
import { BrowserView, MobileView } from 'react-device-detect'
import { Outlet, useLocation } from 'react-router-dom';
import Nav from './Nav/Nav';
import Auth from './router/Auth';
import { AnimatePresence } from 'framer-motion';
import TokenRefresher from './auth/TokenRefresher';

function App() {
  const location = useLocation();


  return (
    <div className="App" style={{ minWidth: '370px', minHeight: '675px', maxWidth: '800px', maxHeight: '765px' }}>
      <BrowserView>
        <h1>PC는 지원하지 않습니다.</h1>
        <h1>모바일 환경에서 접속해주세요.</h1>
      </BrowserView>
      <MobileView>
        <TokenRefresher />
        <AnimatePresence>
          <Auth>
            {!['/', '/login', '/SignUp', '/FirstLogin', '/Guide1', '/Guide2', '/Guide3', , '/Guide4', '/MyUse', '/checkout'].includes(location.pathname) && <Nav />}
            <Outlet />
          </Auth>
        </AnimatePresence>
      </MobileView>
    </div>

  );
}

export default App;
