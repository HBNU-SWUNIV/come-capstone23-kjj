import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import axios from 'axios';
import { useDispatch } from 'react-redux';
import { useKeycloak } from '@react-keycloak/web';
import { useCookies } from 'react-cookie';
import { useRecoilState } from 'recoil';
import { isloginAtom } from '../atom/loginAtom';
import Swal from "sweetalert2";
import { motion } from 'framer-motion';
import { ConfigWithToken, UserBaseApi } from '../auth/authConfig';


const Login = () => {
    const config = ConfigWithToken();

    const [username, setusername] = useState('');
    const [password, setPassword] = useState('');
    const [loginDate, setLoginDate] = useState('');

    const dispatch = useDispatch();
    const navigate = useNavigate();
    //로그인 여부 확인(리다이엑트)
    const [islogin, setIsLogin] = useRecoilState(isloginAtom);

    const { keycloak } = useKeycloak();
    const [cookies, setCookie] = useCookies(['accesstoken']);

    //키클락 로그인
    const handleSSOLogin = async () => {
        keycloak.login();
    }

    const setKeyCloakToken = async () => {
        try {
            const response = await axios.post(`/api/user/login/keycloak?token=${keycloak.token}`)
            console.log('응답 데이터:', response.data);
            const keycloaktoken = response.headers.authorization;
            const [, accesstoken] = keycloaktoken.split('Bearer ');
            setCookie('accesstoken', accesstoken);
            const token = response.headers.refresh_token;
            const [, refreshtoken] = token.split('Bearer ');
            setCookie('refreshtoken', refreshtoken);
            setIsLogin(true);
            if(response.data.loginDate === null){
                navigate('/FirstLogin')
            }
            else{navigate('/home')}
        } catch (error) {
            console.error('키클락 토큰 생성 에러 발생:', error);
        }
    };

    useEffect(() => {
        if (keycloak.authenticated) {
            if (!cookies.accesstoken) {
                setKeyCloakToken();
            } else {
                if(loginDate === null){
                    navigate('/FirstLogin')
                }
                else{navigate('/home')}
            }
        }
    }, [keycloak.authenticated, cookies.accesstoken]);


    //일반 로그인
    const handleLogin = async () => {
        let body = { username, password};
        try {
            const response = await axios({
                method: 'POST',
                url: `/api/user/login/id`,
                data: body,
            });
            console.log('응답 데이터:', response.data);
            setLoginDate(response.data.loginDate);

            const token = response.headers.authorization;
            const [, accesstoken] = token.split('Bearer ');
            setCookie('accesstoken', accesstoken);
            const token1 = response.headers.refresh_token;
            const [, refreshtoken] = token1.split('Bearer ');
            setCookie('refreshtoken', refreshtoken);
            setIsLogin(true);
            if(response.data.loginDate === null){
                navigate('/FirstLogin')
            }
            else{navigate('/home')}

        } catch (error) {
            console.log(error);
            if (error.response.status === 401) {
                Swal.fire({
                    icon: 'warning',
                    text: `ID와 비밀번호를 확인하세요.`,
                    confirmButtonText: "확인",
                })
            }
        }
    };


    //엔터키 로그인
    const handleOnKeyPress = e => {
        if (e.key === 'Enter') {
            handleLogin(); // Enter 입력이 되면 로그인 이벤트 실행
        }
    };

    const inputStyle = {
        width: '250px',
        height: '30px',
        borderRadius: '10px',

    };

    return (
        <motion.div
            initial={{ y: '-100%' }}
            animate={{ y: 0 }}
            exit={{ y: '-100%' }}
            transition={{ duration: 0.5, stiffness: 120 }}
        >
            <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center' }}>
                <div style={{ marginTop: '30px', marginBottom: '100px', color: '#A93528' }}>
                    <h1 style={{ marginBottom: 0 }}>식단미리</h1>
                    <p style={{ display: 'flex', justifyContent: 'center', marginTop: 0, fontSize: '12px' }}>구내식당 메뉴예약 시스템</p>
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
                                style={inputStyle} onKeyPress={handleOnKeyPress} />
                        </label>
                    </div>
                </div>

                <div style={{ display: 'flex', marginTop: '20px' }}>
                    <div style={{ margin: '10px' }}>
                        <Link to=''>
                            <button onClick={handleLogin} style={{ backgroundColor: '#A93528', color: 'white', border: 'none', borderRadius: '5px', width: '100px', height: '30px' }}>로그인</button>
                        </Link>
                    </div>

                    <div style={{ margin: '10px' }}>
                        <button onClick={handleSSOLogin} style={{ backgroundColor: '#635a59', color: 'white', border: 'none', borderRadius: '5px', width: '100px', height: '30px' }}>SSO로그인</button>
                    </div>
                </div>

                <div style={{ marginTop: '40%', textAlign: 'center', width: '100%', color: '#A93528' }}>
                    <p><Link to="/SignUp" style={{ color: '#A93528', textDecoration: 'none' }}>
                        회원가입
                    </Link></p>
                </div>
            </div>
        </motion.div>
    );
};

export default Login;
