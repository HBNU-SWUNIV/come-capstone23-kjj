import React, { useState, useEffect } from "react";
import { Link } from 'react-router-dom';
import axios from "axios";
import { ConfigWithToken, UserBaseApi } from '../auth/authConfig';

function Success() {
    const config = ConfigWithToken();
    const [useDays, setUseDays] = useState([]);

    const today = new Date();
    const QRyear = today.getFullYear();
    const QRmonth = String(today.getMonth() + 1).padStart(2, '0');
    const QRday = String(today.getDate()).padStart(2, '0');
    const [point, setPoint] = useState(0);
    //const orderID = useDays.id;

    const pointChecked = localStorage.getItem('pointChecked');
    const isPointChecked = pointChecked === 'true';

    useEffect(() => {
        // 결제 완료 여부 설정
        axios.get(`${UserBaseApi}/order/${QRyear}/${QRmonth}/${QRday}`, config)
            .then(res => {
                setUseDays(res.data);
                console.log('이용일 성공');

                // 이용일 조회 이후에 post를 비동기적으로 실행
                return axios.post(`${UserBaseApi}/order/${res.data.id}/payment`, { "payment": true }, config);
            })
            .then(res => {
                console.log("결제완료 전달 성공");
            })
            .catch(error => {
                console.error("에러 발생", error);
            });

        axios
            .get(`${UserBaseApi}/page`, config)
            .then((res) => {
                setPoint(res.data.point);
            })
            .catch((error) => {
                console.error('유저 포인트 조회 실패', error);
            });
    }, [])

    if (isPointChecked) {
        axios
            .post(`/api/user/page/point`, { value: point }, config)
            .then((res) => {
                console.log('포인트 사용 성공:', res);
            })
            .catch((error) => {
                console.error('포인트 사용 실패:', error);
            });
    }


    const centerd = {
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'center',
        alignItems: 'center',
        height: '100vh',
    }

    return (
        <div style={{ ...centerd }}>
            <h1 style={{ color: 'blue' }}>결제 성공</h1>
            <p>결제에 성공하였습니다. 감사합니다.</p>
            <button style={{ marginTop: '20vh', height: '5vh', width: '20vw' }}>
                <Link to='/home' style={{ color: "black", textDecoration: "none" }}>닫기</Link>
            </button>
        </div>
    );
}


export default Success;