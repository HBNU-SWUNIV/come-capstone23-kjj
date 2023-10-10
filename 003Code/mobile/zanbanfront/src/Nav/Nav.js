import React from 'react';
import { NavLink, useLocation } from 'react-router-dom';

function Nav() {
  const Navbar = {
    display: 'flex',
    justifyContent: 'center',
  };
  
  const NavList = {
    display: 'flex',
    listStyle: 'none',
    margin: 0,
    padding: 0,
    width: '100%',
    justifyContent: 'space-between',
    alignItems: 'center',
  };
  
  const NavListItem = {
    flex: 1,
    textAlign: 'center',
  };
  
  const NavLink1 = {
    color: '#a6a4a4',
    display: 'block',
    padding: '1rem',
    textDecoration: 'none',
  };
  
  const Icon = {
    fontSize: '22px',
    marginBottom: '5px',
  };
  
  const Text = {
    fontSize: '10px',
  };
  
  const ActiveLink = {
    color: '#000000',
  };
  
  const Footer = {
    position: 'fixed',
    backgroundColor: 'white',
    borderTop: "1px solid black",
    color: '#fff',
    bottom: 0,
    width: '100vw',
  };

  const location = useLocation();
  const { pathname } = location;

  return (
    <footer style={{...Footer, minWidth: '370px'}}>
      <nav style={Navbar}>
        <ul style={NavList} id="nav-list">
          <li style={NavListItem}>
            <NavLink to="/home" style={{ ...NavLink1, ...((pathname === "/Home" || pathname === "/home" || pathname === "/Home/BestMenu") && ActiveLink) }}>
              <i className="fas fa-home" style={Icon}></i>
              <div style={Text}>홈</div>
            </NavLink>
          </li>

          <li style={NavListItem}>
            <NavLink to="/calendar" style={{ ...NavLink1, ...((pathname === "/calendar" || pathname === "/Calendar") || pathname.startsWith("/calendar/")) && ActiveLink }}>
              <i className="fas fa-calendar" style={Icon}></i>
              <div style={Text}>일정예약</div>
            </NavLink>
          </li>

          <li style={NavListItem}>
            <NavLink to="/my" style={{ ...NavLink1, ...((pathname === "/my" || pathname === "/My" || pathname === "/Graph"  ) && ActiveLink) }}>
              <i className="fas fa-user" style={Icon}></i>
              <div style={Text}>마이</div>
            </NavLink>
          </li>

          <li style={NavListItem}>
            <NavLink to="/setting" style={{ ...NavLink1, ...((pathname === "/setting" || pathname === "/Setting" || pathname === "/Developers") && ActiveLink) }}>
              <i className="fas fa-cog" style={Icon}></i>
              <div style={Text}>설정</div>
            </NavLink>
          </li>
        </ul>
      </nav>
    </footer>
  );
}

export default Nav;
