import React from "react";
import { Link } from 'react-router-dom';

function Fail() {
    
    const centerd = {
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'center',
        alignItems: 'center',
        height: '100vh',
    }

    return (
        <div style={{...centerd}}>
            <h1 style={{color: 'red'}}>결제 실패</h1>
            <p>결제에 실패하였습니다. 다시 진행해주세요.</p>
            <button style={{marginTop: '20vh', height: '5vh', width: '20vw'}}>
                <Link to='/home' style={{ color: "black", textDecoration: "none" }}>닫기</Link>
            </button>
        </div>
    );
}


export default Fail;