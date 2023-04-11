import {Outlet} from 'react-router-dom';
import styled from 'styled-components';
import Navbar from './Components/Navbar';
import { useSelector } from 'react-redux';

const Wrapper = styled.div`
display:flex;
`

function App() {
  const User = useSelector(state => state.User);

  return (
    <Wrapper>
      <Navbar/>
      <Outlet/>
    </Wrapper>         
  );
}

export default App;
