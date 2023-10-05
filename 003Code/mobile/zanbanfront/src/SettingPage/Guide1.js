import React, { useState, useEffect } from "react";
import { FaArrowRight } from 'react-icons/fa';
import { Link } from 'react-router-dom';
import axios from "axios";
import { ConfigWithToken } from '../auth/authConfig';
import guide1 from "../img/guide1.gif"
import { motion } from 'framer-motion';

function Guide1() {
    const weekstyle = {
        borderRadius: '50%',
        width: '40px',
        height: '40px',
        border: '1px solid black',
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center'
    }

    const textstyle = {
        textAlign: 'center',
        color: '#A93528'
    }

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
            style={{ position: 'relative'}}
        >
            <div>
                <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
                    <h1 style={{ ...textstyle, marginTop: '50px', marginBottom: 0 }}>{storeInfo}</h1>
                    <h2 style={textstyle}>주 이용 요일을 설정해 주세요.</h2>

                    <img style={{ border: 'solid 1px', marginTop: '70px' }} src={guide1} alt="주 이용일 설정 가이드"></img>

                    <p style={{ textAlign: 'center' }}>
                        · 식당을 이용할 주된 요일을 설정해주세요
                        <br />· 이용 요일은 수정할 수 있어요.
                        <br />· 개인 일정에 따라 일별로도 수정할 수 있어요.</p>


                    <div style={{ marginTop: '50px', marginLeft: '80%' }}>
                        <button>
                            <Link to='/Guide2' style={{ display: 'flex', alignItems: 'center' }}>
                                <FaArrowRight style={{ marginTop: '5px', marginBottom: '5px' }} />
                            </Link>
                        </button>
                    </div>
                    <p style={{  color: '#A93528' }}>(1/3)</p>
                </div>
            </div>
        </motion.div>
    );
}

export default Guide1;