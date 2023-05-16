import React, { useState } from 'react';
import { Link } from 'react-router-dom';

const Login = () => {
    const [id, setId] = useState('');
    const [password, setPassword] = useState('');

    const handleLogin = () => {
        // 로그인 처리 로직 구현
    };

    const inputStyle = {
        width: '250px',
        height: '30px',
        borderRadius: '10px'
    };

    return (
        <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center' }}>
            <div style={{ marginTop: '50px', marginBottom: '100px', color: '#A93528' }}>
                <h1>구내식당 예약 시스템</h1>
            </div>

            <div style={{ marginBottom: '10px' }}>
                <label htmlFor="id">
                    <input type="text" id="id" placeholder="Id" value={id} onChange={(e) => setId(e.target.value)}
                        style={inputStyle} />
                </label>
            </div>
            <div style={{ marginBottom: '10px' }}>
                <label htmlFor="password">
                    <input type="password" id="password" placeholder="Password" value={password} onChange={(e) => setPassword(e.target.value)}
                        style={inputStyle} />
                </label>
            </div>

            <div style={{ marginBottom: '10px' }}>
                <Link to='/home'>
                    <button onClick={handleLogin} style={{ backgroundColor: '#A93528', color: 'white', border: 'none', borderRadius: '5px', width: '100px', height: '30px' }}>로그인</button>
                </Link>
            </div>

            <div style={{ position: 'absolute', bottom: '100px', textAlign: 'center', width: '100%', color: '#A93528' }}>
                <Link to="/SignUp" style={{ textDecoration: 'none' }}>
                    <p>회원가입</p>
                </Link>
            </div>
        </div>


    );
};

export default Login;
