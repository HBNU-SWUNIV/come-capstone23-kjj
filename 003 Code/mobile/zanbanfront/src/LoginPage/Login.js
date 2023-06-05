import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import axios from 'axios';
import { R_login } from '../store';
import { useDispatch } from 'react-redux';

const Login = () => {
    const [username, setusername] = useState('');
    const [password, setPassword] = useState('');
    const dispatch = useDispatch();
    const navigate = useNavigate();

    const handleLogin = async () => {
        let body = { username, password };
        try {
            const res = await axios.post(`/api/user/login`, body);
            const token = res.data.token; // 서버에서 반환하는 토큰 정보
            axios.defaults.headers.common['Authorization'] = `Bearer ${token}`; // 요청 헤더에 토큰 추가
            const { username, id } = res.data;
            //console.log('Response:', res.data);
            if (res.status === 200) {
                //dispatch(R_login({ username }));
                dispatch(R_login({ username, id }));
                navigate('/home');

            }
        } catch (error) {
            alert("ID와 패스워드를 확인하세요.");
        }
    };

    const inputStyle = {
        width: '250px',
        height: '30px',
        borderRadius: '10px',

    };


    return (
        <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center' }}>
            <div style={{ marginTop: '30px', marginBottom: '100px', color: '#A93528' }}>
                <h1 style={{ marginBottom: 0 }}>식재료 절약단</h1>
                <p style={{ display: 'flex', justifyContent: 'center', marginTop: 0, fontSize: '12px' }}>구내식당 예약 시스템</p>
            </div>

            <div>
                <p style={{ fontWeight: 'bold', margin: 0 }}>아이디</p>
                <div style={{ padding: '5px', marginBottom: '10px' }}>
                    <label htmlFor="id">
                        <input type="text" id="id" placeholder="아이디" value={username} onChange={(e) => setusername(e.target.value)}
                            style={inputStyle} />
                    </label>
                </div>
            </div>


            <div>
                <p style={{ fontWeight: 'bold', margin: 0 }}>비밀번호</p>
                <div style={{ padding: '5px', marginBottom: '10px' }}>
                    <label htmlFor="password">
                        <input type="password" id="password" placeholder="비밀번호" value={password} onChange={(e) => setPassword(e.target.value)}
                            style={inputStyle} />
                    </label>
                </div>
            </div>

            <div style={{ margin: '10px' }}>
                <Link to=''>
                    <button onClick={handleLogin} style={{ backgroundColor: '#A93528', color: 'white', border: 'none', borderRadius: '5px', width: '100px', height: '30px' }}>로그인</button>
                </Link>
            </div>

            <div style={{ marginTop: '40%', textAlign: 'center', width: '100%', color: '#A93528' }}>
                <p><Link to="/SignUp" style={{ textDecoration: 'none' }}>
                    회원가입
                </Link></p>
            </div>
        </div>


    );
};

export default Login;
