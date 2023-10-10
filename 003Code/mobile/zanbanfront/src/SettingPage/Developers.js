import React from "react";
import gi from '../img/gi.png'
import hy from '../img/hy.png'
import se from '../img/se.png'
import { FaGithub } from 'react-icons/fa';
import { Link } from 'react-router-dom';
import { FaArrowLeft } from 'react-icons/fa';

function Developers() {

    const imgStyle = {
        borderRadius: '50%',
        maxWidth: '25vw'
    }

    const fontStyle = {
        marginBottom: 0,
        fontWeight: 'bolder'
    }

    return (
        <div>
            <button style={{ marginLeft: '7%', marginTop: '7%' }}>
                <Link to='/Setting' style={{ display: 'flex', alignItems: 'center' }}>
                    <FaArrowLeft style={{ marginTop: '5px', marginBottom: '5px' }} />
                </Link>
            </button>
            <div style={{ textAlign: 'center' }}>
                <h1>Developer Information</h1>
                <div style={{ display: 'flex', flexDirection: 'row', justifyContent: 'space-between', marginLeft: '10px', marginRight: '10px', marginTop: '10vh', marginBottom: '5vh' }}>
                    <div>
                        <img style={imgStyle} src={gi}></img>
                        <p style={fontStyle}>GiBeom</p>
                        <p style={{ margin: 0 }}>Web Frontend</p>
                        <Link to={'https://github.com/kkb4363'} target="_blank">
                            <FaGithub />
                        </Link>
                    </div>
                    <div>
                        <img style={imgStyle} src={hy}></img>
                        <p style={fontStyle}>HyeongMok</p>
                        <p style={{ margin: 0 }}>Web&App Backend</p>
                        <Link to="https://github.com/HyeongMokJeong" target="_blank">
                            <FaGithub />
                        </Link>
                    </div>
                    <div>
                        <img style={imgStyle} src={se}></img>
                        <p style={fontStyle}>SeongHoon</p>
                        <p style={{ margin: 0 }}>App Frontend</p>
                        <Link to="https://github.com/joseonghoon" target="_blank">
                            <FaGithub />
                        </Link>
                    </div>
                </div>
                <p style={{ marginBottom: 0 }}>Capstone Design 2023 KJJ Team</p>
                <p style={{ margin: 0 }}>김차종 교수님 / 모비젠 송영관 멘토님</p>
            </div>
        </div>
    );
}

export default Developers;