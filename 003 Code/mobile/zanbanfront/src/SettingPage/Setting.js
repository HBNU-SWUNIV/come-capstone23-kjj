import React, { useState, useEffect } from "react";
import { Link, useNavigate } from 'react-router-dom';
import { useSelector } from "react-redux";
import { useKeycloak } from '@react-keycloak/web';
import { useCookies } from 'react-cookie';

function Setting() {
    const { keycloak } = useKeycloak();
    const navigate = useNavigate();
    const [cookies, setCookie] = useCookies(['accesstoken']);

    const handleLogout = async () => {
        if (keycloak.authenticated) {
            navigate('/');
            //setCookie('accesstoken', '');
            await keycloak.logout();
        }
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

    const [user, setUser] = useState("");
    const name = useSelector(state => state.username);
    useEffect(() => {
        setUser(name.username);
    }, [])

    const [showDialog, setShowDialog] = useState(false);

    const handleWithdrawal = () => {
        setShowDialog(true);
    }

    const handleCancel = () => {
        setShowDialog(false);
    }

    const [password, setPassword] = useState("");

    //회원탈퇴시
    const handledelete = () => {
        // if (password === "") {
        //     alert("비밀번호를 입력하세요.");
        //     return;
        // }
        setShowDialog(false);
        navigate('/login');
        // axios
        // .delete(`/api/user/withdraw`)
        // .then(res => {
        //     setOrderCount(res.data);
        // })
    }


    return (
        <div style={{ display: "flex", flexDirection: "column", alignItems: "center", justifyContent: "center", marginTop: '50px' }}>

            <Link to='/Guide1' style={buttonStyle}>이용 가이드</Link>
            <Link to='/setting' style={buttonStyle}>비밀번호 변경</Link>
            <div style={buttonStyle} onClick={handleLogout}>로그아웃</div>
            <Link to='' style={buttonStyle} onClick={handleWithdrawal}>회원탈퇴</Link>

            {showDialog && (
                <div style={{ position: 'fixed', top: 0, left: 0, width: '100vw', height: '100vh', backgroundColor: 'rgba(0, 0, 0, 0.5)', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                    <div style={{ backgroundColor: '#fff', padding: '20px', borderRadius: '10px', width: '300px', height: '200px', textAlign: 'center' }}>
                        <h3>정말 탈퇴하시겠습니까?</h3>
                        <p>탈퇴 즉시 계정이 삭제되며,<br />개인정보는 안전하게 파기됩니다.</p>
                        <div>
                            <input style={{ borderRadius: '5px', textAlign: 'center' }} type="text" id="password" name="password" placeholder="비밀번호를 입력하세요."
                                onChange={(e) => setPassword(e.target.value)} />
                        </div>
                        <div style={{ display: 'flex', justifyContent: 'space-around' }}>
                            <button style={{ ...buttonStyle, backgroundColor: '#f44336' }} onClick={handledelete}>회원탈퇴</button>
                            <button style={{ ...buttonStyle }} onClick={handleCancel}>취소</button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}

export default Setting;
