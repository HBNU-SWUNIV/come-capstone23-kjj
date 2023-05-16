import React, { useState } from 'react';
import { Link } from 'react-router-dom';


const SignUp= () => {
    const [id, setId] = useState('');
    const [password, setPassword] = useState('');
    const [confirmpassword, setConfirmPassword] = useState('');
    const [email, setEmail] = useState('');
    const [phone, setPhone] = useState('');

    const handleSignUp = () => {
        // 로그인 처리 로직 구현
    };

    const inputStyle = {
        width: '250px',
        height: '30px',
        borderRadius: '10px'
    };

    return (
        <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center'}}>
            <div style={{ marginTop: '50px', marginBottom: '50px', color: '#A93528' }}>
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
                <label htmlFor="confirm password">
                    <input type="password" id="confirmpassword" placeholder="Confirm Password" value={confirmpassword} onChange={(e) => setConfirmPassword(e.target.value)}
                        style={inputStyle} />
                </label>
            </div>

            <div style={{ marginBottom: '10px' }}>
                <label htmlFor="E-mail">
                    <input type="text" id="email" placeholder="E-mail" value={email} onChange={(e) => setEmail(e.target.value)}
                        style={inputStyle} />
                </label>
            </div>

            <div style={{ marginBottom: '10px' }}>
                <label htmlFor="password">
                    <input type="text" id="phone" placeholder="Phone" value={phone} onChange={(e) => setPhone(e.target.value)}
                        style={inputStyle} />
                </label>
            </div>

            <div style={{ marginBottom: '10px' }}>
                <Link to="/login">
                    <button onClick={handleSignUp} style={{ backgroundColor: '#A93528', color: 'white', border: 'none', borderRadius: '5px', width: '100px', height: '30px' }}>회원가입</button>
                </Link>
            </div>
        </div>
    );
};

export default SignUp;
