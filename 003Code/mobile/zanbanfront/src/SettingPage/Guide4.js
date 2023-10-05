import React, { useState, useEffect } from "react";
import { FaArrowRight } from 'react-icons/fa';
import { FaArrowLeft } from 'react-icons/fa';
import { Link } from 'react-router-dom';
import axios from "axios";
import { ConfigWithToken } from '../auth/authConfig';
import { motion } from 'framer-motion';
import guide4 from "../img/guide4.gif"


function Guide4() {
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
                    <h2 style={{ textAlign: 'center', color: '#A93528' }}>발급된 QR코드 정보를 확인해 주세요.</h2>

                    <img style={{ border: 'solid 1px', width: 'auto', height: '40vh', marginTop: '20px' }} src={guide4} alt="QR코드 조회 가이드"></img>

                    <p style={{ textAlign: 'center' }}>
                        · QR코드는 예약 당일 오전 10시 30분에 발급됩니다.
                        <br />· 예약된 정보와 일치하는지 확인해 주세요.
                        <br />· 매장에 방문하여 QR코드를 제시해 주세요.</p>

                    <div style={{ display: 'flex', justifyContent: 'space-between', width: '90%', marginTop: 'auto' }}>
                        <button>
                            <Link to='/Guide3' style={{ display: 'flex', alignItems: 'center' }}>
                                <FaArrowLeft style={{ marginTop: '5px', marginBottom: '5px' }} />
                            </Link>
                        </button>

                        <button>
                            <Link to='/home' style={{ display: 'flex', alignItems: 'center' }}>
                                <FaArrowRight style={{ marginTop: '5px', marginBottom: '5px' }} />
                            </Link>
                        </button>
                    </div>
                    <p style={{ color: '#A93528' }}>(4/4)</p>
                </div>
            </div>
        </motion.div>
    );
}

export default Guide4;