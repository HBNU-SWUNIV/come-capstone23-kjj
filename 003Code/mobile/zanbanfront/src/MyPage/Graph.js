import React from "react";
import { FaArrowLeft } from 'react-icons/fa';
import { Link } from 'react-router-dom';
import co2 from '../img/co2.png'
import co22 from '../img/co22.png'

function Graph() {

    return (
        <div>
            <div style={{ position: 'absolute', left: '30px' }}>
                <button>
                    <Link to='/My' style={{ display: 'flex', alignItems: 'center' }}>
                        <FaArrowLeft style={{ marginTop: '5px', marginBottom: '5px' }} />
                    </Link>
                </button>
            </div>

            <div style={{ marginTop: '20px', display: 'flex', justifyContent: 'center', alignItems: 'center', flexDirection: 'column' }}>
                <div style={{ width: '80%', marginTop: '30px' }}>
                    <p>저희 식단미리 서비스를 이용하시면 사전예약을 통한 음식 주문이 이루어져 불필요한 식재료 낭비와 음식물 쓰레기의 발생을 최소화할 수 있습니다.</p>
                    <br />
                    <img style={{ maxWidth: '100%' }} src={co2}></img>
                </div>
                <div style={{ width: '80vw' }}>
                    <p>한국환경공단의 조사에 따르면 한국의 생활 쓰레기 중 음식물 쓰레기가 차지하는 비중은 약 29%입니다.</p>
                    <p>2017년 기준, 총 생활 쓰레기 54,390톤 중 15,903톤이 음식물 쓰레기였습니다. 음식물 쓰레기를 20% 줄이면 온실가스 배출량 177만 톤이 감소합니다.</p>
                    <p>이는 승용차 47만 대가 배출하는 온실가스 배출량과 맞먹습니다. 🌲소나무 3억 6천만 그루를 심는 것과 같은 효과를 낼 수 있습니다.</p>
                </div>

                <div style={{ marginTop: '30px', width: '80%', marginBottom: '80px' }}>
                    <img style={{ maxWidth: '100%' }} src={co22}></img>
                    <p>출처: Hannah Ritchie, 2020</p>
                </div>
            </div>
        </div>

    );
}

export default Graph;