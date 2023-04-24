import { BrowserView, MobileView} from 'react-device-detect'
import Home from "./HomePage/Home"
import Login from './LoginPage/Login';
import SignUp from './LoginPage/SignUp'
import Setting from './SettingPage/Setting'
import Guide1 from './SettingPage/Guide1'

function App() {

  return (
    <div className="App">
      <BrowserView>
        <h1>PC는 지원하지 않습니다.</h1>
        <h1>모바일에서 접속해주세요.</h1>
      </BrowserView>
      <MobileView>
        <Setting />
      </MobileView>
    </div>
    
  );
}

export default App;
