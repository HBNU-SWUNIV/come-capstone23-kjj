import React, { useState, useEffect } from "react";
import qr from "../img/qr.png"
import food_icon from "../img/food_icon.png"
import axios from "axios";
import { format } from "date-fns";
import { useMatch, useNavigate, Link } from "react-router-dom";
import { useSelector } from "react-redux";

const Home = () => {
    const navigate = useNavigate();
    let now = new Date();
    let t_year = format(now, 'yyyy');
    let t_month = format(now, 'MM');
    let t_day = format(now, 'dd');
    const [todayMenu, setTodayMenu] = useState("");
    const [menus, setMenus] = useState([]);
    const [user, setUser] = useState("");


    //회원 이름, 번호
    const userinfo = useSelector(state => state.username);
    console.log(userinfo);
    const name = useSelector(state => state.username.username);
    //console.log(name);
    const userid = useSelector(state => state.username.userid);
    //console.log(userid);


    const DetailPathMatch = useMatch('/home/:id');
    const DetailPath = DetailPathMatch?.params.id && menus.find(data => data.id == DetailPathMatch.params.id)

    const [goodmenu, setGoodmenu] = useState([]);
    const [currentIdx, setCurrentIdx] = useState(0);
    const [storeInfo, setStoreInfo] = useState("");
    const [defaultMenu, setDefaultMenu] = useState('');

    useEffect(() => {
        axios
        .get(`/api/user/store`)
        .then(res => {
            setStoreInfo(res.data.name);
        })

        axios.get(`/api/user/planner/${t_year}/${t_month}/${t_day}`)
            .then(res => setTodayMenu(res.data.menus))

        axios.get(`/api/manager/menu`)
            .then(res => setMenus(res.data))

            axios
            .get(`/api/user/${userid}/policy/date`)
            .then(res => {
              console.log("A request has occurred:", res.data);
          
              const receivedActiveDays = Object.values(res.data);
              setActiveDays(receivedActiveDays);
          
              const receivedDefaultMenu = res.data.defaultMenu;
              //setDefaultMenu(receivedDefaultMenu);
          
              axios.get(`/api/manager/menu`)
                .then(res => {
                  const menuList = res.data;
                  const defaultMenu = menuList.find(menu => menu.id === receivedDefaultMenu);
                  if (defaultMenu) {
                    setDefaultMenu(defaultMenu.name);
                  }
                })
                .catch(error => {
                  console.error("Failed to get menu list:", error);
                });
            })
            .catch(error => {
              console.error("Failed to get user policy date:", error);
            });

        axios.get(`/api/manager/state/menu`)
            .then(res => setGoodmenu(res.data.map(menu => menu.name)))
        // axios.patch(`/api/user/${ userid }/date`)
        //     .then(() => {
        //         console.log("연결 성공");
        //     })
        //     .catch(error => {
        //         console.error("연결 실패:", error);
        //     });

        setUser(name);

        const interval = setInterval(() => {
            setCurrentIdx((prevIdx) => (prevIdx + 1) % goodmenu.length);
        }, 2200);

        return () => clearInterval(interval);
    }, [goodmenu.length]);

    const fadeDuration = 800;


    const infoboxStyle = {
        fontSize: '15px',
        lineHeight: '1.5',
        width: '80%',
        padding: '8px 16px',
        margin: '10px auto',
        borderRadius: '10px',
        boxShadow: '0px 0px 4px rgba(0, 0, 0, 0.2)',
    };

    const qrbox = {
        width: '50%',
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        boxShadow: '0px 0px 5px rgba(0, 0, 0, 0.2)',
        borderRadius: '10px',
    };

    const bestmenulistboxStyle = {
        fontSize: '15px',
        width: '80%',
        whiteSpace: 'pre-wrap',
        padding: '8px 16px',
        borderRadius: '10px',
        boxShadow: '0px 0px 4px rgba(0, 0, 0, 0.2)',
        marginTop: 0,
    };

    const menulistboxStyle = {
        fontSize: '15px',
        width: '30%',
        whiteSpace: 'pre-wrap',
        padding: '8px 16px',
        borderRadius: '10px',
        boxShadow: '0px 0px 4px rgba(0, 0, 0, 0.2)',
        textAlign: 'center',
        marginTop: 0,
    };

    const menuboxStyle = {
        fontSize: "15px",
        lineHeight: "1.5",
        padding: "8px 16px",
        margin: "10px",
        borderRadius: "10px",
        boxShadow: '2px 2px 4px rgba(0, 0, 0, 0.2)',
        display: 'flex',
        flexDirection: 'column',
    };

    const menu1boxStyle = {
        width: '90%',
        justifyContent: 'center',
        margin: '0 auto',
        display: 'grid',
        gridTemplateColumns: 'repeat(2, 1fr)',
        gap: '20px',
    };

    const [showDialog, setShowDialog] = useState(false);

    const handleqr = () => {
        setShowDialog(true);
    }

    const handleqrCancel = () => {
        setShowDialog(false);
    }


    const DialogButtonStyle = {
        backgroundColor: 'white',
        fontSize: '15px',
        width: '80%',
        padding: '8px 16px',
        margin: '10px',
        borderRadius: '10px',
        border: "2px solid black",
        color: 'black',
    };

    const boldP = {
        fontWeight: 'bold',
        marginBottom: '10px',
        fontSize: '20px'
    }

    //QR코드 부분에 오늘 날짜 출력
    const today = new Date();

    const formattedDate = `${today.toLocaleDateString('ko-KR', {
        year: '2-digit',
        month: '2-digit',
        day: '2-digit',
    })} (${today.toLocaleString('ko-KR', { weekday: 'short' })})`;

    const handleUsedateClick = () => {
        const usedatesetDiv = document.getElementById('usedateset');
        const usedateDiv = document.getElementById('usedate');
        const nextusedateDiv = document.getElementById('nextusedate');
        const menuDiv = document.getElementById('menu');

        usedateDiv.style.display = 'none';
        nextusedateDiv.style.display = 'none';
        menuDiv.style.display = 'none';

        if (usedatesetDiv.style.display === 'none') {
            usedatesetDiv.style.display = 'block'; // Show the targetDiv
        }
    };

    const [activeDays, setActiveDays] = useState([false, false, false, false, false]);

    const toggleDay = (dayIndex) => {
        const updatedActiveDays = [...activeDays];
        updatedActiveDays[dayIndex] = !updatedActiveDays[dayIndex];
        setActiveDays(updatedActiveDays);

        const weekDays = document.querySelectorAll('.weekday');
        weekDays[dayIndex].classList.toggle('active');

    };

    const handleUsedatesetSaveClick = () => {
        const usedatesetDiv = document.getElementById('usedateset');
        const usedateDiv = document.getElementById('usedate');
        const nextusedateDiv = document.getElementById('nextusedate');
        const menuDiv = document.getElementById('menu');
        const weekDays = [...document.querySelectorAll('.weekday')];

        //receivedActiveDays이걸 추가해야될거 같은데
        const updatedActiveDays = [];
        weekDays.forEach((day) => {
            updatedActiveDays.push(day.classList.contains('active'));
        });
        setActiveDays(updatedActiveDays);

        usedatesetDiv.style.display = 'none';
        usedateDiv.style.display = 'block';
        nextusedateDiv.style.display = 'block';
        menuDiv.style.display = 'block';

        const activeDaysObject = {
            monday: updatedActiveDays[0],
            tuesday: updatedActiveDays[1],
            wednesday: updatedActiveDays[2],
            thursday: updatedActiveDays[3],
            friday: updatedActiveDays[4],
        };

        axios.patch(`/api/user/${userid}/policy/date`, activeDaysObject, {
            headers: {
                "Content-Type": "application/json",
            },
        })
            .then(() => {
                console.log("패치 성공");
                axios.get(`/api/user/${userid}/policy/date`)
                    .then(res => {
                        console.log("겟 성공");
                        const receivedActiveDays = Object.values(res.data);
                        setActiveDays(receivedActiveDays);
                    })
                    .catch(error => {
                        console.error("겟 실패:", error);
                    });
            })
            .catch(error => {
                console.error("패치 실패:", error);
            });
    };

    // const menu_id = menus.map(menu => menu.id);
    // console.log(menu_id)

    const handlemenuSaveClick = () => {
        axios
            .patch(`/api/user/${userid}/policy/menu/${DetailPath.id}`, {})
            .then(() => {
                console.log("patch successful");
                // Update the default menu value in the state
                setDefaultMenu(DetailPath.name);
            })
            .catch(error => {
                console.error("Patch failed:", error);
            })
            .finally(() => {
                navigate('/home');
            });
    };



    const handleUsedatesetCancleClick = () => {
        const usedatesetDiv = document.getElementById('usedateset');
        const usedateDiv = document.getElementById('usedate');
        const nextusedateDiv = document.getElementById('nextusedate');
        const menuDiv = document.getElementById('menu');

        usedatesetDiv.style.display = 'none';
        usedateDiv.style.display = 'block';
        nextusedateDiv.style.display = 'block';
        menuDiv.style.display = 'block';

    };

    const weekstyle = active => ({
        borderRadius: '50%',
        width: '30px',
        height: '30px',
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: active ? '#A3F394' : ''
    });

    const savecancelbtn = {
        width: '20%',
        height: '30px',
        fontWeight: 'bold',
    }

    const getSelectedDays = () => {
        const daysOfWeek = ['월', '화', '수', '목', '금'];
        const selectedDays = daysOfWeek.filter((day, index) => activeDays[index]);
        return selectedDays.join(' ');
    };


    return (
        <div>
            <div style={infoboxStyle}>
                <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                    <div>
                        <p><span style={{ fontSize: '15px', fontWeight: 'bold', lineHeight: 2 }}>{storeInfo}</span></p>
                        <p style={{ fontWeight: 'bold', lineHeight: 0 }}>{user}님</p>
                        <p>안녕하세요</p>
                    </div>

                    <div style={qrbox} onClick={handleqr}>
                        <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', marginTop: '10px' }}>
                            <img src={qr} alt="QR코드" style={{ maxWidth: '50%', height: 'auto' }} />
                        </div>
                        <div style={{ marginTop: '10px', textAlign: 'center' }}>
                            <p style={{ lineHeight: 0 }}>{formattedDate}</p>
                            <p>중식</p>
                            <p style={{ color: 'gray' }}>+크게보기</p>
                        </div>
                    </div>
                </div>

                {showDialog && (
                    <div style={{ position: 'fixed', top: 0, left: 0, width: '100vw', height: '100vh', backgroundColor: 'rgba(0, 0, 0, 0.5)', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                        <div style={{ backgroundColor: '#fff', padding: '20px', borderRadius: '10px', width: '300px', height: '80%', textAlign: 'center' }}>
                            <img src={qr} alt="QR코드" style={{ maxWidth: '50%', height: 'auto', marginTop: '10%' }} />
                            <p>예약자명: {user}님<br />예약번호: 123번
                                ,<br />메뉴: 백반 정식</p>
                            <p>예약일시: 2023-04-20 08:40</p>
                            <div style={{ display: 'flex', justifyContent: 'space-around', marginTop: '10%' }}>
                                <button style={ DialogButtonStyle } onClick={handleqrCancel}>확인</button>
                            </div>
                        </div>
                    </div>
                )}

                <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>

                    <div id="usedate" onClick={handleUsedateClick}>
                        <p style={{ fontWeight: 'bold', lineHeight: 0.5 }}>현재 이용일</p>
                        <p style={{ lineHeight: 0 }}>{getSelectedDays() || "등록 안됨"}</p>
                    </div>
                    <div id="nextusedate">
                        <p style={{ fontWeight: 'bold', lineHeight: 0.5 }}>다음 이용 예정일</p>
                        <p style={{ lineHeight: 0 }}>2023-06-12(월)</p>
                    </div>
                    <div id="menu">
                        <p style={{ fontWeight: 'bold', lineHeight: 0.5 }}>현재 기본 메뉴</p>
                        <p style={{ lineHeight: 0 }}>{defaultMenu ? defaultMenu : ( <> 등록 안됨{" "} </>)}</p>
                    </div>

                </div>


                <div id="usedateset" style={{ display: 'none' }}>
                    <p style={{ fontWeight: 'bold', lineHeight: 0.5 }}>현재 이용일 수정</p>
                    <div style={{ display: 'flex', justifyContent: 'space-between', width: '100%', marginTop: '10px', marginBottom: '10px', border: '1px solid black' }}>
                        <div key="monday" style={weekstyle(activeDays[0])} className="weekday" onClick={() => toggleDay(0)}>월</div>
                        <div key="tuesday" style={weekstyle(activeDays[1])} className="weekday" onClick={() => toggleDay(1)}>화</div>
                        <div key="wednesday" style={weekstyle(activeDays[2])} className="weekday" onClick={() => toggleDay(2)}>수</div>
                        <div key="thursday" style={weekstyle(activeDays[3])} className="weekday" onClick={() => toggleDay(3)}>목</div>
                        <div key="friday" style={weekstyle(activeDays[4])} className="weekday" onClick={() => toggleDay(4)}>금</div>
                    </div>
                    <div>
                        <button id="save" style={{ ...savecancelbtn, marginRight: '10px', backgroundColor: '#6782e0', color: 'white' }} onClick={handleUsedatesetSaveClick}>저장</button>
                        <button id="cancel" style={{ ...savecancelbtn, backgroundColor: '#e07967', color: 'white' }} onClick={handleUsedatesetCancleClick}>취소</button>
                    </div>
                </div>
            </div>

            <div style={{ marginLeft: '10%' }}>
                <p style={boldP}>주간 인기 메뉴</p>
                <div style={bestmenulistboxStyle}>
                    <Link to='/Home/BestMenu' style={{ textDecoration: 'none', color: 'black' }}>
                        {goodmenu.map((menu, idx) => (
                            <p key={idx} style={{ margin: 0, display: currentIdx === idx ? 'block' : 'none' }}>
                                {idx + 1} {menu}
                            </p>
                        ))}
                    </Link>
                </div>
            </div>

            <div style={{ marginTop: '30px', marginLeft: '10%' }}>
                <p style={boldP}>오늘의 메뉴</p>
                <div style={menulistboxStyle}>
                    {todayMenu ? (
                        <p>{todayMenu}</p>
                    ) : (
                        <>
                            <p>등록된<br />메뉴가<br />없습니다.</p>
                        </>
                    )}
                </div>
            </div>

            <div style={{ marginTop: '30px' }}>
                <p style={{ ...boldP, marginLeft: '10%' }}>이용 가능 메뉴</p>
                <div style={menu1boxStyle}>
                    {menus.map((s_menu) => {
                        const soldType = s_menu.sold ? 'y' : 'n';

                        return (
                            soldType === 'n' ? (
                                <div
                                    id={s_menu.name}
                                    style={{ ...menuboxStyle, backgroundColor: '#e3e3e3' }}
                                >
                                    <img
                                        style={{ maxWidth: '100%', height: '70%' }}
                                        src={s_menu.image ? `http://kjj.kjj.r-e.kr:8080/api/image?dir=${s_menu.image}` : food_icon}
                                        alt="이미지 없음"
                                    />
                                    <p style={{ marginTop: '30px', marginBottom: 0, fontSize: '15px', fontWeight: 'bold' }}>{s_menu.name}</p>
                                    <p style={{ lineHeight: 0 }}>{s_menu.cost}원</p>
                                    <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
                                        <p style={{ margin: 0, color: 'red' }}>품절</p>
                                    </div>
                                </div>
                            ) : (
                                <div
                                    onClick={() => {
                                        navigate(`/home/${s_menu.id}`);
                                    }}
                                    id={s_menu.name}
                                    style={menuboxStyle}
                                >
                                    <img
                                        style={{ maxWidth: '100%', height: '70%' }}
                                        src={s_menu.image ? `http://kjj.kjj.r-e.kr:8080/api/image?dir=${s_menu.image}` : food_icon}
                                        alt="이미지 없음"
                                    />
                                    <p style={{ marginTop: '30px', marginBottom: 0, fontSize: '15px', fontWeight: 'bold' }}>{s_menu.name}</p>
                                    <p style={{ lineHeight: 0 }}>{s_menu.cost}원</p>
                                </div>
                            )
                        );
                    })}
                </div>
            </div>

            {DetailPathMatch && (
                <div style={{ position: 'fixed', top: 0, left: 0, width: '100vw', height: '100vh', backgroundColor: 'rgba(0, 0, 0, 0.5)', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                    <div style={{ backgroundColor: '#fff', padding: '20px', borderRadius: '10px', width: '300px', height: '500px', textAlign: 'center' }}>
                        <img src={`http://kjj.kjj.r-e.kr:8080/api/image?dir=${DetailPath?.image}`} alt="메뉴사진" style={{ maxWidth: '50%', height: 'auto', marginTop: '10%', border: "1px solid black", borderRadius: '10px', }} />
                        <h1>{DetailPath.name}<br />{DetailPath.cost}원</h1>
                        <p>{DetailPath.details}</p>
                        <p>{DetailPath.info}</p>
                        <p style={{ fontWeight: 'bold', marginTop: '30px' }}>선택하신 메뉴를 기본메뉴로 <br />등록하시겠습니까?</p>
                        <div style={{ display: 'flex', justifyContent: 'space-around', marginTop: '10%' }}>
                            <button style={{ ...DialogButtonStyle }} onClick={handlemenuSaveClick}>등록</button>
                            <button style={{ ...DialogButtonStyle }} onClick={() => navigate('/home')}>취소</button>
                        </div>
                    </div>
                </div>
            )}

            <div style={{ marginBottom: '100px' }}></div>
        </div>
    )
}

export default Home;