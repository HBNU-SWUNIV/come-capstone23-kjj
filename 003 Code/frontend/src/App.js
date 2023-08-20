import { Outlet } from 'react-router-dom';
import Auth from './router/Auth';

function App() {
  return (
    <Auth>
      <Outlet />
    </Auth>
  );
}

export default App;
