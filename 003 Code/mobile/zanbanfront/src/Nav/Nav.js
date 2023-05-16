import React from 'react';
import { Link } from 'react-router-dom';
import './Nav.css';

function Nav() {
    return (
        <footer>
            <nav className="navbar">
                <ul id="nav-list">
                    <li>
                        <Link to="/home">
                            <div className="icon">
                                <i className="fas fa-home"></i>
                            </div>
                            <div className="text">홈</div>
                        </Link>
                    </li>
                    <li>
                        <Link to="/calendar">
                            <div className="icon">
                                <i className="fas fa-calendar"></i>
                            </div>
                            <div className="text">일정</div>
                        </Link>
                    </li>
                    <li>
                        <Link to="/my">
                            <div className="icon">
                                <i className="fas fa-user"></i>
                            </div>
                            <div className="text">마이</div>
                        </Link>
                    </li>
                    <li>
                        <Link to="/setting">
                            <div className="icon">
                                <i className="fas fa-cog"></i>
                            </div>
                            <div className="text">설정</div>
                        </Link>
                    </li>
                </ul>
            </nav>
        </footer>
    );
}

export default Nav;
