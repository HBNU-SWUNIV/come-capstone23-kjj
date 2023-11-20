import { Outlet } from 'react-router-dom';
import { styled } from 'styled-components';
import Auth from '../auth/Auth';
import Copyright from '../components/general/Copyright';
import { flexCenter } from '../styles/global.style';

function App() {
  return (
    <Auth>
      <OutletBox>
        <Outlet />
      </OutletBox>

      <CopyrightBox>
        <Copyright />
      </CopyrightBox>
    </Auth>
  );
}

export default App;

const CopyrightBox = styled.div`
  width: 100%;
  height: 3vh;
  ${flexCenter};

  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
`;

const OutletBox = styled.div`
  height: 100%;
`;
