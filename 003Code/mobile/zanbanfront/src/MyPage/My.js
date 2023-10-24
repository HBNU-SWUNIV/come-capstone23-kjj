import React, { useState, useEffect } from "react";
import { useNavigate, Link } from "react-router-dom";
import axios from "axios";
import { ConfigWithToken, UserBaseApi } from '../auth/authConfig';
import { useCookies } from 'react-cookie';
import { isloginAtom } from '../atom/loginAtom';
import { useRecoilState } from 'recoil';
import Swal from "sweetalert2";

function My() {
    const navigate = useNavigate();
    const [islogin, setIsLogin] = useRecoilState(isloginAtom);
    const [cookies, setCookie] = useCookies(['accesstoken']);
    if (cookies.accesstoken === undefined) {
        setIsLogin(false);
        Swal.fire({
            icon: 'error',
            text: `죄송합니다. 다시 로그인을 진행해주세요.`,
            confirmButtonText: "확인",
        });
        navigate("/login")
    }

    const [orderCount, setOrderCount] = useState("");
    const [storeInfo, setStoreInfo] = useState("");
    const [idnum, setidnum] = useState([]);

    const config = ConfigWithToken();

    const today = new Date();
    const QRyear = today.getFullYear();
    const QRmonth = String(today.getMonth() + 1).padStart(2, '0');
    const QRday = String(today.getDate()).padStart(2, '0');
    const [useInfo, setUseInfo] = useState();
    const [point, setPoint] = useState(0);

    useEffect(() => {
        axios
            .get(`${UserBaseApi}/info`, config)
            .then(res => {
                setidnum(res.data.id);
            })
            .catch((error) => {
                navigate('/errorpage')
            })

        axios
            .get(`${UserBaseApi}/page`, config)
            .then(res => {
                setPoint(res.data.point);
            })

        axios
            .get(`${UserBaseApi}/order/count`, config)
            .then(res => {
                setOrderCount(res.data);
            })
            .catch(error => {
                console.error("주문 횟수 조회 실패", error);
            });

        axios
            .get(`/api/user/store`, config)
            .then(res => {
                setStoreInfo(res.data.name);
            })
            .catch(error => {
                console.error("식당 정보 조회 실패", error);
            });

        axios
            .get(`${UserBaseApi}/order/last`, config)
            .then(res => {
                console.log(res.data);
            })

        axios
            .get(`${UserBaseApi}/order/${QRyear}/${QRmonth}/${QRday}`, config)
            .then(res => {
                setUseInfo(res.data);
                console.log(useInfo);
            })

    }, [])

    //포인트 사용 테스트
    const handlePointButtonClick = () => {
        //value만큼 감소
        axios
            .post(`/api/user/page/point`, { value: -2000 }, config)
            .then((res) => {
                console.log('Axios 요청 성공:', res);
            })
            .catch((error) => {
                console.error('Axios 요청 실패:', error);
            });
        window.location.reload();
    };


    const pointboxStyle = {
        fontSize: '15px',
        width: '80%',
        padding: '8px 16px',
        margin: '10px auto',
        borderRadius: '10px',
        boxShadow: '2px 2px 4px rgba(0, 0, 0, 0.2)',
    };

    const point1boxStyle = {
        display: 'flex',
        flexDirection: 'row',
        height: '30px',
        marginBottom: '10px',
        position: 'relative',
    };

    const ponifont = {
        margin: 0,
        display: 'flex',
        alignItems: 'center',
        fontWeight: 'bold',
    }

    const underPointfont = {
        position: 'absolute',
        bottom: 0,
        right: 0,
        marginRight: '16px',
        marginBottom: '8px',
        fontSize: '30px',
        fontWeight: 'bold',
    }

    const rightponifont = {
        margin: 0,
        position: 'absolute',
        right: 0,
        fontWeight: 'bold',
        fontSize: '25px'
    }

    const threeboxStyle = {
        display: 'flex',
        flexDirection: 'row',
        padding: '8px 16px',
        width: '85%',
        justifyContent: 'center',
        margin: '10px auto',
    }

    const threeboxfont = {
        fontSize: '15px',
        fontWeight: 'bold',
    }

    const leftboxStyle = {
        fontSize: "15px",
        lineHeight: "1.5",
        width: "60%",
        padding: "8px 16px",
        borderRadius: "10px",
        boxShadow: '2px 2px 4px rgba(0, 0, 0, 0.2)',
        position: 'relative',
    };

    const rightboxStyle = {
        fontSize: "15px",
        lineHeight: "1.5",
        width: "70%",
        padding: "8px 16px",
        borderRadius: "10px",
        boxShadow: '2px 2px 4px rgba(0, 0, 0, 0.2)',
        position: 'relative',
    };

    return (
        <div>
            <div style={pointboxStyle}>
                <div style={{ ...point1boxStyle, marginBottom: '20px', borderBottom: '2px dotted' }}>
                    <p style={{ margin: 0, fontSize: '20px', fontWeight: 'bold' }}>{storeInfo}</p>
                    <p style={{ ...rightponifont, fontSize: '20px', fontWeight: 'bold' }}>{idnum}님</p>
                </div>
                <div onClick={handlePointButtonClick} style={point1boxStyle}>
                    <p style={ponifont}>누적 적립 포인트</p>
                    <p style={{ ...rightponifont, color: '#FF6347' }}>{orderCount * 50}P</p>
                </div>
                <div style={point1boxStyle}>
                    <p style={ponifont}>사용 가능 포인트</p>
                    <p style={rightponifont}>{point}P</p>
                </div>
            </div>

            <div style={threeboxStyle}>
                <div style={leftboxStyle}>
                    <p style={{ ...threeboxfont, marginBottom: 0 }}>누적</p>
                    <p style={{ ...threeboxfont, marginTop: 0 }}>이용횟수</p>
                    <p style={underPointfont}>{orderCount}회</p>
                </div>

                <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'end' }}>
                    <div style={{ ...rightboxStyle, marginBottom: '5px', height: '100px' }}>
                        <p><span style={threeboxfont}>누적 이용금액</span></p>
                        <p style={underPointfont}>{orderCount !== null ? orderCount * 6000 + "원" : "이용급액 없음"}</p>

                    </div>

                    <div style={rightboxStyle}>
                        <Link to='/Graph' style={{ color: 'inherit', textDecoration: 'none' }}>
                            <div style={{ display: 'flex', flexDirection: 'row', }}>
                                <p style={{ fontSize: '15px', fontWeight: 'bold' }}>탄소감축의 중요성,</p>
                            </div>
                            <p style={{ marginTop: 0 }}>여러분은 식단미리 통해 음식물 쓰레기 저감 활동에 동참하고 있습니다.</p>
                            <p style={{ right: 0, position: 'absoulte', fontWeight: 'bold' }}>-🌲자세히 보기-</p>
                        </Link>
                    </div>
                </div>
            </div>
        </div>
    );
}


export default My;