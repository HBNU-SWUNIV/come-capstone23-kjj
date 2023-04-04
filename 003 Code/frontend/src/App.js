import {Outlet} from 'react-router-dom';
import styled from 'styled-components';
import Navbar from './Components/Navbar';
import Search from './Components/Search';

const Wrapper = styled.div`
display:flex;
flex-direction:column;
`;

function App() {
  return (
    <Wrapper>
    <Navbar/>
    <div>
      <Search/>
      <Outlet/>
    </div>
    </Wrapper>         
  );
}

export default App;
