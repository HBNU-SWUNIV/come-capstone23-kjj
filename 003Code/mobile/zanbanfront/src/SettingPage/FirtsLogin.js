import React, { useState, useEffect } from "react";
import { useNavigate } from 'react-router-dom';
import axios from "axios";
import { ConfigWithToken } from '../auth/authConfig';
import Swal from "sweetalert2";
import ConfettiRain from "./ConfettiRain";

function FirstLogin() {
    const navigate = useNavigate();

    const [loaded, setLoaded] = useState(false);

    useEffect(() => {
        // 꽃가루 애니메이션 페이지가 로딩되면 loaded를 true로 설정
        setLoaded(true);

        setTimeout(() => {
            setLoaded(false);
        }, 10000); // 10초 (10000 밀리초)
    }, []);

    const textstyle = {
        textAlign: 'center',
        color: '#A93528'
    }

    const DialogButtonStyle = {
        backgroundColor: 'white',
        fontSize: '15px',
        width: '80%',
        padding: '8px 16px',
        margin: '10px',
        borderRadius: '10px',
        border: "2px solid black",
        color: 'black',
    };

    const [storeInfo, setStoreInfo] = useState("");
    const [userID, setUserID] = useState("");
    const config = ConfigWithToken();

    useEffect(() => {
        axios
            .get(`/api/user/store`, config)
            .then(res => {
                setStoreInfo(res.data.name);
            })
            .catch(error => {
                console.error("식당 정보 조회 실패", error);
            });
        axios
            .get(`api/user/info`, config)
            .then(res =>
                setUserID(res.data.id))

    }, [])


    return (
        <div>
            {loaded && <ConfettiRain />}
            <div>
                <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
                    <h1 style={{ ...textstyle, marginTop: '50px', marginBottom: 0 }}>{storeInfo}</h1>
                    <h1 style={{ textAlign: 'center', color: 'black', fontSize: '50px' }}>환영합니다😄</h1>

                    <p style={{
                        textAlign: 'center', borderRadius: '10px',
                        border: "2px solid black", paddingTop: '10vh',
                        marginTop: '20px', paddingBottom: '10vh'
                    }}>
                        · 저희 식단미리를 이용해 주셔서 감사합니다.
                        <br />· 회원님의 회원 번호는 {userID}입니다.
                    </p>


                    <div style={{ display: 'flex', justifyContent: 'space-around', marginTop: '5vh', width: '90vw' }}>
                        <button style={{ ...DialogButtonStyle, background: '#f7dfc8' }} onClick={() => navigate('/guide1')}>
                            가이드 <br /> 보러 가기
                        </button>
                        <button style={{ ...DialogButtonStyle }} onClick={() => {
                            Swal.fire({
                                title: '이용 가이드는 언제든 다시 볼 수 있습니다.',
                                text: '설정 ➔ 이용 가이드',
                                icon: 'success',
                                confirmButtonText: '확인',
                            }).then((result) => {
                                if (result.isConfirmed) {
                                    navigate('/home');
                                }
                            });
                        }}>
                            바로 이용하기
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default FirstLogin;