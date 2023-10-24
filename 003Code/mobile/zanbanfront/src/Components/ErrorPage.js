import { useNavigate } from 'react-router-dom';
import { useKeycloak } from '@react-keycloak/web';
import { useCookies } from 'react-cookie';
import { useRecoilState } from 'recoil';
import { isloginAtom } from '../atom/loginAtom';
import { React } from "react";
import axios from "axios";
import { ConfigWithRefreshToken } from '../auth/authConfig';


function ErrorPage() {
    const { keycloak } = useKeycloak();
    const navigate = useNavigate();
    const [cookies, setCookie] = useCookies(['accesstoken', 'refreshtoken']);
    const [islogin, setIsLogin] = useRecoilState(isloginAtom);

    const config1 = ConfigWithRefreshToken();

    const extensionToken = async () => {
        const refreshToken = config1.headers.Authorization

        try {
            const response = await axios.post(
                `/api/user/login/refresh`,
                null, // 요청 데이터가 필요 없을 경우 null 또는 {}
                {
                    headers: {
                        Authorization: `${refreshToken}`
                    },
                }
            );

            const token = response.headers.authorization;
            const [, newAccessToken] = token.split('Bearer ');
            setCookie('accesstoken', newAccessToken);

            const newRefreshToken = response.headers.refresh_token;
            const [, newRefreshTokenValue] = newRefreshToken.split('Bearer ');
            setCookie('refreshtoken', newRefreshTokenValue);
            console.log('토큰 재발급 성공')
        } catch (error) {
            console.error('토큰 재발급 에러 발생:', error);
        }

        navigate('/home');
    }


    const handleLogout = async () => {
        setIsLogin(false);
        if (keycloak.authenticated) {
            navigate('/');
            await keycloak.logout();
        }
        setCookie('accesstoken', '');
        setCookie('refreshtoken', '');
        navigate('/');
    }

    const buttonStyle = {
        backgroundColor: 'white',
        fontSize: '15px',
        width: '70%',
        padding: '8px 16px',
        margin: '10px',
        borderRadius: '100px',
        textDecoration: 'none',
        border: "2px solid black",
        textAlign: 'center',
        color: 'black',
        boxShadow: '3px 3px 5px rgba(0, 0, 0, 0.3)',
    };

    return (
        <div style={{ marginTop: '20vh', textAlign: 'center', display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
            <h1 style={{ marginBottom: '20px' }}>로그인 토큰이 <br />만료되었습니다.</h1>
            <div style={buttonStyle} onClick={extensionToken}>로그인 유지</div>
            <div style={buttonStyle} onClick={handleLogout}>로그아웃</div>
        </div>
    );
}

export default ErrorPage;