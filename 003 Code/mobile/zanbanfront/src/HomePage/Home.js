import React, { useState } from "react";
import sea from "../img/sea.png"
import pig from "../img/pig.png"
import qr from "../img/qr.png"

const Home = () => {
    const infoboxStyle = {
        backgroundColor: '#F6EFE8',
        fontSize: '15px',
        lineHeight: '1.5',
        width: '80%',
        padding: '8px 16px',
        margin: '10px auto',
        borderRadius: '10px',
        border: "2px solid black"
    };

    const qrbox = {
        width: '50%',
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        border: '1px solid black',
        borderRadius: '10px',
    };

    const menulistboxStyle = {
        backgroundColor: '#F6EFE8',
        fontSize: '15px',
        width: '20%',
        lineHeight: '0.5',
        padding: '8px 16px',
        borderRadius: '10px',
        border: "2px solid black",
        textAlign: 'center'
    };

    const menuboxStyle = {
        backgroundColor: "#F6EFE8",
        fontSize: "15px",
        lineHeight: "1.5",
        width: "40%",
        padding: "8px 16px",
        margin: "10px",
        borderRadius: "10px",
        border: "2px solid black"
    };

    const menu1boxStyle = {
        display: 'flex',
        flexDirection: 'row',
        width: '90%',
        justifyContent: 'center',
        margin: '0 auto',
    };

    const [showDialog, setShowDialog] = useState(false);

    const handleqr = () => {
        setShowDialog(true);
    }

    const handleqrCancel = () => {
        setShowDialog(false);
    }

    const qrDialogStyle = {
        backgroundColor: '#F6EFE8',
        fontSize: '15px',
        width: '80%',
        padding: '8px 16px',
        margin: '10px',
        borderRadius: '10px',
    };

    const [showDialog1, setShowDialog1] = useState(false);

    const handlemenu = () => {
        setShowDialog1(true);
    }

    const handlemenuCancel = () => {
        setShowDialog1(false);
    }

    const menuDialogStyle = {
        backgroundColor: '#F6EFE8',
        fontSize: '15px',
        width: '80%',
        padding: '8px 16px',
        margin: '10px',
        borderRadius: '10px',
    };

    return (
        <div>
            <div style={infoboxStyle}>
                <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                    <div>
                        <p><span style={{ fontSize: '20px', fontWeight: 'bold', lineHeight: 2 }}>한밭대학교</span></p>
                        <p style={{ fontWeight: 'bold', lineHeight: 0 }}>ㅇㅇㅇ님</p>
                        <p>안녕하세요</p>
                    </div>

                    <div style={qrbox} onClick={handleqr}>
                        <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
                            <img src={qr} alt="QR코드" style={{ maxWidth: '50%', height: 'auto' }} />
                        </div>
                        <div style={{ marginTop: '10px', textAlign: 'center' }}>
                            <p style={{ lineHeight: 0 }}>23-04-08(수)</p>
                            <p>중식</p>
                            <p style={{ color: 'gray' }}>+크게보기</p>
                        </div>
                    </div>
                </div>

                {showDialog && (
                    <div style={{ position: 'fixed', top: 0, left: 0, width: '100vw', height: '100vh', backgroundColor: 'rgba(0, 0, 0, 0.5)', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                        <div style={{ backgroundColor: '#fff', padding: '20px', borderRadius: '10px', width: '300px', height: '80%', textAlign: 'center' }}>
                            <img src={qr} alt="QR코드" style={{ maxWidth: '50%', height: 'auto', marginTop: '10%' }} />
                            <p>예약자명: ㅇㅇㅇ님<br />예약번호: 123번
                                ,<br />메뉴: 백반 정식</p>
                            <p>예약일시: 2023-04-20 08:40</p>
                            <div style={{ display: 'flex', justifyContent: 'space-around', marginTop: '10%' }}>
                                <button style={{ ...qrDialogStyle }} onClick={handleqrCancel}>확인</button>
                            </div>
                        </div>
                    </div>
                )}

                <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                    <div>
                        <p style={{ fontWeight: 'bold', lineHeight: 0.5 }}>현재 이용일</p>
                        <p style={{ lineHeight: 0 }}>월 수 금</p>
                    </div>
                    <div>
                        <p style={{ fontWeight: 'bold', lineHeight: 0.5 }}>다음 이용 예정일</p>
                        <p style={{ lineHeight: 0 }}>2023-04-10(금)</p>
                    </div>
                    <div>
                        <p style={{ fontWeight: 'bold', lineHeight: 0.5 }}>현재 기본 메뉴</p>
                        <p style={{ lineHeight: 0 }}>백반 정식</p>
                    </div>
                </div>
            </div>

            <div style={{ marginTop: '30px', marginLeft: '10%' }}>
                <p style={{ fontWeight: 'bold' }}>오늘의 식단표에요.</p>
                <div style={menulistboxStyle}>
                    <p>백미밥</p>
                    <p>된장국</p>
                    <p>닭볶음탕</p>
                    <p>군만두</p>
                    <p>잡채</p>
                </div>
            </div>

            <div style={{ marginTop: '30px' }}>
                <p style={{ fontWeight: 'bold', marginLeft: '10%' }}>이용가능 메뉴</p>
                <div style={menu1boxStyle}>
                    <div style={menuboxStyle} onClick={handlemenu}>
                        <img src={sea} alt="메뉴사진" style={{ maxWidth: '100%', height: 'auto' }} />
                        <p style={{ fontSize: '15px', fontWeight: 'bold' }}>해물순두부찌개</p>
                        <p style={{ lineHeight: 0 }}>5000원</p>
                    </div>

                    <div style={menuboxStyle} onClick={handlemenu}>
                        <img src={pig} alt="메뉴사진" style={{ maxWidth: '100%', height: 'auto' }} />
                        <p style={{ fontSize: '15px', fontWeight: 'bold' }}>촌돼지김치찌개</p>
                        <p style={{ lineHeight: 0 }}>6000원</p>
                    </div>
                </div>

                <div style={{ ...menu1boxStyle, marginBottom: '100px' }}>
                    <div style={menuboxStyle} onClick={handlemenu}>
                        <img src={sea} alt="메뉴사진" style={{ maxWidth: '100%', height: 'auto' }} />
                        <p style={{ fontSize: '15px', fontWeight: 'bold' }}>해물순두부찌개</p>
                        <p style={{ lineHeight: 0 }}>5000원</p>
                    </div>

                    <div style={menuboxStyle} onClick={handlemenu}>
                        <img src={pig} alt="메뉴사진" style={{ maxWidth: '100%', height: 'auto' }} />
                        <p style={{ fontSize: '15px', fontWeight: 'bold' }}>촌돼지김치찌개</p>
                        <p style={{ lineHeight: 0 }}>6000원</p>
                    </div>
                </div>
            </div>

            {showDialog1 && (
                <div style={{ position: 'fixed', top: 0, left: 0, width: '100vw', height: '100vh', backgroundColor: 'rgba(0, 0, 0, 0.5)', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                    <div style={{ backgroundColor: '#fff', padding: '20px', borderRadius: '10px', width: '300px', height: '80%', textAlign: 'center' }}>
                        <img src={sea} alt="메뉴사진" style={{ maxWidth: '50%', height: 'auto', marginTop: '10%', border: "1px solid black", borderRadius: '10px', }} />
                        <h1>해물순두부찌개<br/>5000원</h1>
                        <p>따뜻한 순두부찌개입니다.</p>
                        <p>알레르기 정보<br />이 식품은 오징어, 조개류가
                            ,<br />포함돼 있습니다.</p>
                        <p style={{ fontWeight: 'bold', marginTop: '30px' }}>선택하신 메뉴로 등록하시겠습니까?</p>
                        <div style={{ display: 'flex', justifyContent: 'space-around', marginTop: '10%' }}>
                            <button style={{ ...menuDialogStyle }} onClick={handlemenuCancel}>등록</button>
                            <button style={{ ...menuDialogStyle }} onClick={handlemenuCancel}>취소</button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    )
}

export default Home;