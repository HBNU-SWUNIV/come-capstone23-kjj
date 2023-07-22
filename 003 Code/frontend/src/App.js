import { useKeycloak } from '@react-keycloak/web';
import { Outlet } from 'react-router-dom';

function App() {
  const { initialized } = useKeycloak();

  if (!initialized) {
    return <>Loading...</>;
  }

  return (
    <div>
      <Outlet />
    </div>
  );
}

export default App;
