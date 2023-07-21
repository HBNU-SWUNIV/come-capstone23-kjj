import { useKeycloak } from '@react-keycloak/web';
import { Outlet } from 'react-router-dom';

function App() {
  const { initialized } = useKeycloak();

  if (!initialized) {
    return <>Loading...</>;
  }
  // usekeycloak 에서 authenticated 는 idToken 과 token 여부로 결정된다. */
  // () => keycloak.logout()

  return (
    <div>
      <Outlet />
    </div>
  );
}

export default App;
