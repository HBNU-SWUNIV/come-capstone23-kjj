import React, { useState, useEffect } from "react";
import { FaArrowRight } from 'react-icons/fa';
import { FaArrowLeft } from 'react-icons/fa';
import { Link } from 'react-router-dom';
import axios from "axios";
import { ConfigWithToken } from '../auth/authConfig';
import guide2 from "../img/guide2.gif"
import { motion } from 'framer-motion';

function Guide2() {
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
                    <h2 style={{ textAlign: 'center', color: '#A93528' }}>주 이용 메뉴를 설정해주세요.</h2>

                    <img style={{ border: 'solid 1px', width: 'auto', height: '40vh', marginTop: '40px' }} src={guide2} alt="주 메뉴 설정 가이드"></img>

                    <p style={{ textAlign: 'center' }}>
                        · 기본으로 선택할 메뉴를 설정해주세요.
                        <br />· 이용 요일마다 선택하신 메뉴를 자동으로 예약해드려요.
                        <br />· 언제든지 변경할 수 있어요.
                        <br />· 일정예약 페이지에서 매일 그날 먹고싶은 메뉴로 수정할 수 있어요.</p>

                    <div style={{ display: 'flex', justifyContent: 'space-between', width: '90%', marginTop: 'auto' }}>
                        <button>
                            <Link to='/Guide1' style={{ display: 'flex', alignItems: 'center' }}>
                                <FaArrowLeft style={{ marginTop: '5px', marginBottom: '5px' }} />
                            </Link>
                        </button>

                        <button>
                            <Link to='/Guide3' style={{ display: 'flex', alignItems: 'center' }}>
                                <FaArrowRight style={{ marginTop: '5px', marginBottom: '5px' }} />
                            </Link>
                        </button>
                    </div>
                    <p style={{ color: '#A93528' }}>(2/3)</p>
                </div>
            </div>
        </motion.div>
    );
}

export default Guide2;