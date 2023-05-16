import React, { useState } from "react";

function MyUse() {
    const [isOpen, setIsOpen] = useState(false);

    const listItemStyle = {
        listStyleType: 'none',
        padding: '10px',
        borderRadius: '10px',
        border: '1px solid black',
        marginBottom: '5px',
        lineHeight: '0.5',
    };

    const dialogStyle = {
        position: "fixed",
        top: "40%",
        left: "50%",
        transform: "translate(-50%, -50%)",
        backgroundColor: "white",
        padding: "20px",
        borderRadius: "10px",
        boxShadow: "0px 0px 10px grey",
        height: "60%",
        width: "80%",
    };

    const openDialog = () => {
        setIsOpen(true);
    };

    const closeDialog = () => {
        setIsOpen(false);
    };

    return (
        <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center' }}>
            <div style={{ marginTop: '10px', marginBottom: '20px', color: '#B2A1A1' }}>
                <p>이용내역 상세조회</p>
            </div>

            <ul style={{ paddingLeft: 0, width: '80%' }}>
                <li style={listItemStyle}>
                    <div onClick={openDialog}>
                        <p>백반정식</p>
                        <p>5,000원</p>
                        <button style={{ backgroundColor: "#AD3F32", color: "black" }}>이용완료</button>
                    </div>
                </li>
                <li style={listItemStyle}>
                    <div onClick={openDialog}>
                        <p>백반정식</p>
                        <p>5,000원</p>
                        <button style={{ backgroundColor: '#AD3F32', color: 'black' }}>이용완료</button>
                    </div>
                </li>
                <li style={listItemStyle}>
                    <div onClick={openDialog}>
                        <p>백반정식</p>
                        <p>5,000원</p>
                        <button style={{ backgroundColor: '#AD3F32', color: 'black' }}>이용완료</button>
                    </div>
                </li>
            </ul>

            {isOpen && (
                <div style={dialogStyle}>
                    <p>이용 내역</p>
                    <button onClick={closeDialog}>닫기</button>
                </div>
            )}
        </div>

    );
}

export default MyUse;
