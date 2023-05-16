import { BrowserView, MobileView, isMobileOnly } from 'react-device-detect'
import { Outlet } from 'react-router-dom';
import Nav from './Nav/Nav';

function App() {

  return (
    <div className="App">
      <BrowserView>
        <h1>PC는 지원하지 않습니다.</h1>
        <h1>모바일 환경에서 접속해주세요.</h1>
      </BrowserView>
      <MobileView>
        {isMobileOnly && <Nav />}
        <Outlet />
      </MobileView>
    </div>

  );
}

export default App;
