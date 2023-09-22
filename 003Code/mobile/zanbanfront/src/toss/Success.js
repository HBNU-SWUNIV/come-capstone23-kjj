import React from "react";
import { Link } from 'react-router-dom';

function Success() {

    const centerd = {
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'center',
        alignItems: 'center',
        height: '100vh',
    }

    return (
        <div style={{...centerd}}>
            <h1 style={{color: 'blue'}}>결제 성공</h1>
            <p>결제에 성공하였습니다. 감사합니다.</p>
            <button style={{marginTop: '20vh', height: '5vh', width: '20vw'}}>
                <Link to='/home' style={{ color: "black", textDecoration: "none" }}>닫기</Link>
            </button>
        </div>
    );
}


export default Success;