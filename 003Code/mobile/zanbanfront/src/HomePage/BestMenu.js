import React from "react";
import { FaArrowLeft } from 'react-icons/fa';
import { Link } from 'react-router-dom';
import ApexCharts from "react-apexcharts";
import { useState, useEffect } from 'react';
import axios from 'axios';
import { ConfigWithToken, UserBaseApi } from '../auth/authConfig';
import { motion } from 'framer-motion';

const boldP = {
    fontWeight: 'bold',
    marginBottom: '20px',
    fontSize: '20px'
}

const menulistboxStyle = {
    fontSize: '15px',
    whiteSpace: 'pre-wrap',
    padding: '8px',
    borderRadius: '10px',
    boxShadow: '0px 0px 4px rgba(0, 0, 0, 0.2)',
    marginTop: 0,
};

const menuboxStyle = {
    justifyContent: 'center',
    margin: '0 auto',
    display: 'grid',
    gridTemplateColumns: 'repeat(2, 1fr)',
    gap: '20px',
};
function BestMenu() {

    const [goodmenu, setGoodmenu] = useState([]);

    const config = ConfigWithToken();

    useEffect(() => {
        axios.get(`/api/user/state/menu`, config)
            .then(res => setGoodmenu(res.data))
    }, [])

    const names = [];

    for (let i = 0; i < 5; i++) {
        if (goodmenu[i]) {
            names[i] = goodmenu[i].name;
        } else {
            names[i] = '';
        }
    }

    return (
        <div>
            <div style={{ position: 'absolute', left: '30px' }}>
                <button>
                    <Link to='/Home' style={{ display: 'flex', alignItems: 'center' }}>
                        <FaArrowLeft style={{ marginTop: '5px', marginBottom: '5px' }} />
                    </Link>
                </button>
            </div>

            <div style={{ marginTop: '20px', display: 'flex', justifyContent: 'center', alignItems: 'center', flexDirection: 'column' }}>
                <div style={{ lineHeight: "0.5", width: '80%', marginTop: '30px' }}>
                    <p style={boldP}>주간 인기 메뉴</p>
                    <div style={menulistboxStyle}>
                        <div style={{ ...menuboxStyle, fontSize: '12px' }}>
                            <p style={{ margin: '5px' }}>1위 {names[0]}</p>
                            <p style={{ margin: '5px' }}>2위 {names[1]}</p>
                            <p style={{ margin: '5px' }}>3위 {names[2]}</p>
                            <p style={{ margin: '5px' }}>4위 {names[3]}</p>
                            <p style={{ margin: '5px' }}>5위 {names[4]}</p>
                            <p style={{ margin: '5px' }}>6위 {names[5]}</p>
                        </div>
                    </div>
                </div>
            </div>

            <motion.div
                initial={{ scale: 0, opacity: 0.5 }}
                animate={{
                    scale: 1,
                    opacity: 1,
                }}
                transition={{
                    duration: 1,
                    type: "spring",
                    stiffness: 110,
                }}
                style={{ marginTop: '20px', width: '90%' }}>
                <ApexCharts
                    type='pie'
                    series={goodmenu.map(menu => menu.count)}
                    options={{
                        chart: {
                            type: 'pie',
                            toolbar: { show: false }
                        },
                        labels: goodmenu.map(menu => menu.name)
                    }}
                />
            </motion.div>
        </div>
    );
}

export default BestMenu;