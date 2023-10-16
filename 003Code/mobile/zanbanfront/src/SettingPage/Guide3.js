import React, { useState, useEffect } from "react";
import { FaArrowRight } from 'react-icons/fa';
import { FaArrowLeft } from 'react-icons/fa';
import { Link } from 'react-router-dom';
import axios from "axios";
import { ConfigWithToken } from '../auth/authConfig';
import { motion } from 'framer-motion';
import guide3 from "../img/guide3.gif"


function Guide3() {
    const [storeInfo, setStoreInfo] = useState("");
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

    }, [])

    return (
        <motion.div
            initial={{ x: '100%' }}
            animate={{ x: 0 }}
            exit={{ x: '100%' }}
            transition={{ duration: 0.5, stiffness: 120 }}
            style={{ position: 'relative' }}
        >
            <div>
                <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
                    <h1 style={{ textAlign: 'center', color: '#A93528', marginTop: '50px', marginBottom: 0 }}>{storeInfo}</h1>
                    <h2 style={{ textAlign: 'center', color: '#A93528' }}>날짜별로 예약이 가능해요.</h2>

                    <img style={{ border: 'solid 1px', width: 'auto', height: '50vh', marginTop: '20px' }} src={guide3} alt="QR코드 조회 가이드"></img>

                    <p style={{ textAlign: 'center' }}>
                        · 일정예약 페이지에서 예약할 수 있어요.
                        <br />· 예약 취소를 원하신다면 리스트에서 예약 취소를 선택해주세요.
                        <br />· 메뉴를 선택하고 저장 버튼을 눌러주세요.</p>

                    <div style={{ display: 'flex', justifyContent: 'space-between', width: '90%', marginTop: 'auto' }}>
                        <button>
                            <Link to='/Guide2' style={{ display: 'flex', alignItems: 'center' }}>
                                <FaArrowLeft style={{ marginTop: '5px', marginBottom: '5px' }} />
                            </Link>
                        </button>

                        <button>
                            <Link to='/Guide4' style={{ display: 'flex', alignItems: 'center' }}>
                                <FaArrowRight style={{ marginTop: '5px', marginBottom: '5px' }} />
                            </Link>
                        </button>
                    </div>
                    <p style={{ color: '#A93528' }}>(3/4)</p>
                </div>
            </div>
        </motion.div>
    );
}

export default Guide3;