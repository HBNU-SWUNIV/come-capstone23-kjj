import React from "react";

function Graph() {

    return (
        <div style={{ marginTop: '15px', display: 'flex', justifyContent: 'center', alignItems: 'center', flexDirection: 'column' }}>
            <div style={{ lineHeight: "0.5", width: '80%' }}>
                <p>지난달,</p>
                <p>잔반제로를 통해 약 ??kg의 음식물</p>
                <p>쓰레기 저감 활동에 동참했어요!</p>
                <br />
                <p>음식물쓰레기 저감을 통해 ??kg의</p>
                <p>이산화탄소 배출을 줄였습니다.</p>
                <br />
                <p>이는 소나무 ㅇㅇ그루가 ㅇ년간</p>
                <p>흡수하는 이산화탄소량과 같습니다.</p>
            </div>

            <div style={{ marginTop: '30px', width: '80%'}}>
                <p>지난달,</p>
                <p>탄소 그래프</p>
            </div>
        </div>

    );
}

export default Graph;