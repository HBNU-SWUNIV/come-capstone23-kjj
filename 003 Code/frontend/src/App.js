import {Outlet} from 'react-router-dom';
import styled from 'styled-components';
import Navbar from './Components/Navbar';

const Wrapper = styled.div`
display:flex;
flex-direction:row;
`;

function App() {
  return (
    <Wrapper>
    <Navbar/>
    <Outlet/>
    </Wrapper>         
  );
}

export default App;
