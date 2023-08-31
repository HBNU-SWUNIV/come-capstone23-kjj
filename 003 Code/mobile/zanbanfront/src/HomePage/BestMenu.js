import React from "react";
import { FaArrowLeft } from 'react-icons/fa';
import { Link } from 'react-router-dom';
import ApexCharts from "react-apexcharts";
import { useState, useEffect } from 'react';
import axios from 'axios';
import { ConfigWithToken, UserBaseApi } from '../auth/authConfig';

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


    const first = goodmenu.length > 0 ? goodmenu[0].name : '';
    const second = goodmenu.length > 0 ? goodmenu[1].name : '';
    const third = goodmenu.length > 0 ? goodmenu[2].name : '';
    // const fourth = goodmenu.length > 0 ? goodmenu[3].name : '';
    // const fifth = goodmenu.length > 0 ? goodmenu[4].name : '';
    // const sixth = goodmenu.length > 0 ? goodmenu[5].name : '';

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
                        <div style={{...menuboxStyle, fontSize: '12px'}}>
                            <p style={{ margin: '5px' }}>1위 {first}</p>
                            <p style={{ margin: '5px' }}>2위 {second}</p>
                            <p style={{ margin: '5px' }}>3위 {third}</p>
                            <p style={{ margin: '5px' }}>4위 </p>
                            <p style={{ margin: '5px' }}>5위 </p>
                            <p style={{ margin: '5px' }}>6위 </p>
                        </div>
                    </div>
                </div>
            </div>

            <div style={{ marginTop: '20px', width: '90%' }}>
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
            </div>
        </div>

    );
}

export default BestMenu;