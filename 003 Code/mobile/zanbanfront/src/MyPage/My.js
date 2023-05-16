import React from "react";
import { Link } from 'react-router-dom';

function My() {
    const pointboxStyle = {
        backgroundColor: '#F6EFE8',
        fontSize: '15px',
        lineHeight: '1.5',
        width: '80%',
        padding: '8px 16px',
        margin: '10px auto',
        borderRadius: '10px',
        border: "2px solid black"
    };

    const leftboxStyle = {
        backgroundColor: "#F6EFE8",
        fontSize: "15px",
        lineHeight: "1.5",
        width: "40%",
        padding: "8px 16px",
        margin: "10px",
        borderRadius: "10px",
        border: "2px solid black"
    };

    const rightboxStyle = {
        backgroundColor: "#F6EFE8",
        fontSize: "15px",
        lineHeight: "1.5",
        width: "70%",
        padding: "8px 16px",
        margin: "10px",
        borderRadius: "10px",
        border: "2px solid black"
    };

    const buttonStyle = {
        backgroundColor: '#F6EFE8',
        fontSize: '15px',
        width: '80%',
        padding: '8px 16px',
        margin: '10px',
        borderRadius: '10px',
    };

    return (
        <div>
            <div style={pointboxStyle}>
                <p><span style={{ fontSize: '20px', fontWeight: 'bold' }}>ㅇㅇㅇ님 한밭대학교</span></p>
                <p>누적 포인트</p>
                <p>사용 가능 포인트</p>
            </div>

            <div style={{ display: 'flex', flexDirection: 'row', width: '80%', justifyContent: 'center', margin: '0 auto' }}>
                <div style={leftboxStyle}>
                    <p><span style={{ fontSize: '15px', fontWeight: 'bold' }}>이번 달 이용횟수</span></p>
                    <p>?회</p>
                </div>

                <div style={{ display: 'flex', flexDirection: 'column' }}>
                    <div style={rightboxStyle}>
                        <p><span style={{ fontSize: '15px', fontWeight: 'bold' }}>이용금액</span></p>
                        <p>?원</p>
                    </div>

                    <div style={rightboxStyle}>
                        <Link to='/Graph' style={{ color: 'inherit', textDecoration: 'none' }}>
                            <p><span style={{ fontSize: '15px', fontWeight: 'bold' }}>지난달,</span></p>
                            <p>잔반제로를 통해 ?의 음씩 쓰레기가 절감됐어요</p>
                        </Link>
                    </div>
                </div>
            </div>

            <div style={{ display: 'flex', justifyContent: 'center' }}>
                <button style={buttonStyle}>
                    <Link to='/MyUse' style={{ color: 'inherit', textDecoration: 'none' }}>이용내역 상세조회</Link>
                </button>
            </div>

        </div>
    );
}


export default My;