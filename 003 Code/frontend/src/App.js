import {Outlet} from 'react-router-dom';
import styled from 'styled-components';
import Navbar from './Components/Navbar';
import { useSelector } from 'react-redux';

const Wrapper = styled.div`
display:flex;
`

function App() {
  const User = useSelector(state => state.User);
  console.log(User)
  return (
    <Wrapper>
      {User.testFirstvisit == false ? <Navbar/> : null}
      <Outlet/>
    </Wrapper>         
  );
}

export default App;
