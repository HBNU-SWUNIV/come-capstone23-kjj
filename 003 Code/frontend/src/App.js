import { Outlet } from 'react-router-dom';
import Auth from './router/Auth';
import Copyright from './components/general/Copyright';
import { styled } from 'styled-components';

function App() {
  return (
    <Auth>
      <OutletWrapper>
        <Outlet />
      </OutletWrapper>

      <CopyrightWrapper>
        <Copyright />
      </CopyrightWrapper>
    </Auth>
  );
}

export default App;

const CopyrightWrapper = styled.div`
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;

  height: 5vh;
  width: 100%;

  display: flex;
  justify-content: center;
  align-items: center;

  z-index: 2;
`;

const OutletWrapper = styled.div`
  height: 100%;
`;
