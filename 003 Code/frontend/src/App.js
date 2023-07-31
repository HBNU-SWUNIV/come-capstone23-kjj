import { useKeycloak } from '@react-keycloak/web';
import { Outlet } from 'react-router-dom';
import Loading from './screens/Loading';

function App() {
  const { initialized, keycloak } = useKeycloak();

  if (!initialized) {
    return <Loading />;
  }

  if (keycloak.authenticated) {
    return (
      <div>
        <Outlet />
      </div>
    );
  }
}

export default App;
