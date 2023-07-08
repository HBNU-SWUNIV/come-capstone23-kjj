import { Outlet } from "react-router-dom";
import Dashboard from "./dashboard/Dashboard";

function App() {
  return (
    <div className="App">
      <Outlet/>
    </div>
  );
}

export default App;
