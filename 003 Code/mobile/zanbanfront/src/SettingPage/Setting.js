import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import Guide1 from './Guide1';

function Setting() {
    const buttonStyle = {
        backgroundColor: '#F6EFE8',
        fontSize: '15px',
        width: '300px',
        padding: '8px 16px',
        margin: '10px',
        borderRadius: '10px',
    };

    const [showDialog, setShowDialog] = useState(false);

    const handleWithdrawal = () => {
        setShowDialog(true);
    }

    const handleCancel = () => {
        setShowDialog(false);
    }

    //회원탈퇴시
    const handledelete = () => {
        setShowDialog(false);
        //코드추가
    }

    return (
        <div style={{ display: "flex", flexDirection: "column", alignItems: "center", justifyContent: "center", marginTop: '100px' }}>
            <button style={buttonStyle}>
                <Link to='/Guide1' style={{ color: 'inherit', textDecoration: 'none' }}>이용 가이드</Link>
            </button>
            <button style={buttonStyle}>로그아웃</button>
            <button style={buttonStyle} onClick={handleWithdrawal}>회원탈퇴</button>

            {showDialog && (
                <div style={{ position: 'fixed', top: 0, left: 0, width: '100vw', height: '100vh', backgroundColor: 'rgba(0, 0, 0, 0.5)', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                    <div style={{ backgroundColor: '#fff', padding: '20px', borderRadius: '10px', width: '300px', height: '200px', textAlign: 'center' }}>
                        <h3>정말 탈퇴하시겠습니까?</h3>
                        <p>탈퇴 즉시 계정이 삭제되며,<br />개인정보는 안전하게 파기됩니다.</p>
                        <div style={{ display: 'flex', justifyContent: 'space-around', marginTop: '20px' }}>
                            <button style={{ ...buttonStyle }} onClick={handleCancel}>취소</button>
                            <button style={{ ...buttonStyle, backgroundColor: '#f44336' }} onClick={handleCancel}>회원탈퇴</button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}

export default Setting;
