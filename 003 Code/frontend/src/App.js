import {Outlet} from 'react-router-dom';
import styled from 'styled-components';
import Navbar from './Components/Navbar';
import { useLocation } from 'react-router-dom';

const Wrapper = styled.div`
display:flex;
`

function App() {
  // 페이지 별 navbar 활성화 여부 구현
  const location = useLocation();
  
  return (
    <Wrapper>
      {location.pathname === '/' ? null : <Navbar/>}
      <Outlet/>
    </Wrapper>         
  );
}

export default App;
