/* eslint-disable */
import { addMonths, subMonths, format, startOfMonth, endOfMonth, startOfWeek, endOfWeek, addDays } from 'date-fns';
import React, { useState, useEffect } from "react";
import { AiOutlineLeft, AiOutlineRight } from 'react-icons/ai';
import { useMatch, useNavigate } from 'react-router-dom';
import shortid from 'shortid';
import Switch from 'react-switch';
import axios from "axios";
import Select from 'react-select';
import { ConfigWithToken, UserBaseApi } from '../auth/authConfig';
import Swal from "sweetalert2";
import { useCookies } from 'react-cookie';
import { isloginAtom } from '../atom/loginAtom';
import { useRecoilState } from 'recoil';

const holidayServiceKey = `ziROfCzWMmrKIseBzkXs58HpS39GI%2FmxjSEmUeZbKwYuyxnSc2kILXCBXlRpPZ8iam5cqwZqtw6db7CnWG%2FQQQ%3D%3D`;
const holidayBaseApi = `http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getHoliDeInfo?`;

const ArrowCSS = { color: '#969696', fontSize: '20px' };

const Wrapper = {
    display: 'flex',
    width: '78vw',
    marginBottom: '15px',
    flexDirection: 'column',
    alignItems: 'center',
    borderRadius: '15px 15px 0 0',
    border: '1px solid #0000001A',
};

const HeaderW = {
    width: '78vw',
    height: '6vh',
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
};

const DaysWrapper = {
    display: 'flex',
    justifyContent: 'space-between',
    width: '78vw',
};

const DivDay = {
    width: '24vw',
    height: '10vh',
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'space-between',
    border: '0.1px solid #ededed',
};

const DaysDiv = {
    width: '24vw',
    height: '3vh',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#0000001A',
};

const DaySpan = {
    'firstChild': {
        color: 'red',
    },
    'lastChild': {
        color: '#64b5f6',
    },
};

const DivWeek = {
    display: 'flex',
    width: '78vw',
    height: '10vh',
};

const DivWrapper = {
    width: '78vw',
    flexDirection: 'column',
};

const colorBox = {
    width: '70%',
    justifyContent: 'space-between',
    fontSize: '12px',
    fontWeight: 'bold',
    display: 'flex',
    marginBottom: '20px'
}

const colorBox2 = {
    display: 'flex',
    alignItems: 'center',
}

const colorBox1 = {
    width: '20px',
    height: '20px',
    borderRadius: '50%',
}

const infoBox = {
    width: '80%',
    color: '#f55b5b',
    boxShadow: '2px 2px 10px rgba(0, 0, 0, 0.2)',
    borderRadius: '10px',
    fontSize: '10px',
    marginBottom: '25%',
}

const WriteWrapper = {
    width: '70%',
    borderRadius: '15px',
    height: 'auto',
    margin: '0 auto',
    position: 'fixed',
    marginTop: '40%',
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    backgroundColor: 'white',
    border: '1px solid blue',
};

const WriteTitle = {
    width: '70%',
    height: '6vh',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
};

const WriteInfo = {
    width: '70%',
    height: 'auto',
    marginBottom: '10px',
    marginTop: '5px',
    justifyContent: 'center',
    whiteSpace: 'pre-wrap',
    textAlign: 'center',
};

const saveButton = {
    marginTop: '10px',
    marginRight: '10px',
    marginBottom: '20px',
    background: '#6782e0',
    color: 'white',
    borderRadius: '10px'
};


const selectStyles = {
    control: (provided, state) => ({
        ...provided,
        width: '200px',
        marginBottom: '10px',
    }),
};

function Calendar() {
    const navigate = useNavigate();
    const [islogin, setIsLogin] = useRecoilState(isloginAtom);
    const [cookies, setCookie] = useCookies(['accesstoken']);
    if (cookies.accesstoken === undefined) {
        setIsLogin(false);
        Swal.fire({
            icon: 'error',
            text: `다시 로그인해 주세요.`,
            confirmButtonText: "확인",
        });
        navigate("/login")
    }

    const [holiday, setHoliday] = useState([]);
    const isArray = holiday?.length !== undefined;

    const [activeDays, setActiveDays] = useState([]);
    const [menus, setMenus] = useState([]);
    const [menusId, setMenusId] = useState([]);
    const config = ConfigWithToken();

    const daysOfWeek = {
        monday: '월',
        tuesday: '화',
        wednesday: '수',
        thursday: '목',
        friday: '금',
    };

    //현재 이용일 조회
    const ActiveDays = ({ activeDays }) => {
        const activeDayLabels = Object.entries(activeDays)
            .filter(([_, isActive]) => isActive)
            .map(([day]) => daysOfWeek[day]);

        return (
            <p style={{ fontSize: '12px', marginBottom: '2px' }}>
                현재 이용일: {activeDayLabels.join(' ')}
            </p>
        );
    };


    //유저 정책, 메뉴 조회
    useEffect(() => {
        axios
            .get(`${UserBaseApi}/policy/date`, config)
            .then(res => {
                setActiveDays(res.data);
            })
            .catch(error => {
                console.error("유저 정책 조회 실패", error);
            });


        axios.get(`${UserBaseApi}/menu`, config)
            .then(res => setMenus(res.data))

        axios.get(`${UserBaseApi}/menu`, config)
            .then(res => setMenusId(res.data.id))

    }, []);


    //오늘의 메뉴 조희
    const [todayMenu, setTodayMenu] = useState([]);
    const DayPathMatch = useMatch('/calendar/:id');
    //이용일 조회
    const [useDays, setUseDays] = useState([]);
    const [notUseDays, setNotUseDays] = useState([]);

    useEffect(() => {
        if (DayPathMatch && DayPathMatch.params && DayPathMatch.params.id) {
            const { id } = DayPathMatch.params;
            const year = id.slice(0, 4);
            const month = id.slice(4, 6);
            const day = id.slice(6, 8);

            axios
                .get(`${UserBaseApi}/planner/${year}/${month}/${day}`, config)
                .then(res => setTodayMenu(res.data.menus))
                .catch((error) => {
                    console.error(error);
                    setTodayMenu([]);
                });

        }
    }, [DayPathMatch]);

    //달력 구성
    const today = new Date();
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, '0');
    const day1 = String(today.getDate()).padStart(2, '0');
    const currentDate = `${year}${month}${day1}`;

    //현재시간(예약 마감시간 설정)
    const currentHour = today.getHours();
    const currentMinute = today.getMinutes();

    const [currentMonth, setCurrentMonth] = useState(new Date());

    const days = [];
    const date = ['일', '월', '화', '수', '목', '금', '토'];
    for (let i = 0; i < 7; i++) {
        days.push(
            <div style={DaysDiv} key={shortid.generate()}>
                <span style={i === 0 ? DaySpan.firstChild : (i === 6 ? DaySpan.lastChild : {})}>
                    {date[i]}
                </span>
            </div>
        );
    }

    const monthStart = startOfMonth(currentMonth);
    const monthEnd = endOfMonth(monthStart);
    const startDate = startOfWeek(monthStart);
    const endDate = endOfWeek(monthEnd);
    let day = startDate;
    let formattedDate = '';
    let dayss = [];
    let line = [];

    //달력 일 클릭시 해당 id페이지 출력
    const onDay = (id) => {
        const idYear = id.substring(0, 4);
        const idMonth = id.substring(4, 6);
        const idDay = id.substring(6, 8);

        navigate(`/calendar/${id}`);
    }

    //메뉴 리스트
    const [aquaticCreatures, setAquaticCreatures] = useState([]);
    //휴무일 조회
    const [storeoff, setStoreoff] = useState([]);

    //이용메뉴
    const [defaultMenu, setDefaultMenu] = useState('');

    //이용일별 이용 메뉴
    const [useMenuIds, setUseMenuIds] = useState({});

    //메뉴 저장 버튼 클릭시 동작
    const handleSave = () => {

        if (DayPathMatch && DayPathMatch.params && DayPathMatch.params.id) {
            const { id } = DayPathMatch.params;
            const year = id.slice(0, 4);
            const month = id.slice(4, 6);
            const day = id.slice(6, 8);

            //수동 이용함
            if (useMenuID !== 0) {
                axios
                    .post(`${UserBaseApi}/order/add/${useMenuID}/${year}/${month}/${day}`, 'true', config)
                    .then(() => {
                        console.log("수동 이용 성공");
                        setUseMenuIds(prevState => {
                            const menuName = aquaticCreatures.find(menu => menu.id === useMenuID).label;
                            console.log(useMenuID);
                            return { ...prevState, [id]: menuName };
                        });
                        //임시방편
                        window.location.reload();
                    })
                    .catch((error) => {
                        console.error("수동 이용 에러발생");
                    });

                if (!useMenuID) {
                    Swal.fire({
                        icon: 'info',
                        text: `이용 메뉴를 선택해주세요.`,
                        confirmButtonText: "확인",
                    })
                    return;
                }
                navigate('/calendar');
            }

            //수동 이용안함
            if (useMenuID === 0) {
                axios
                    .post(`${UserBaseApi}/order/cancel/${year}/${month}/${day}`, 'false', config)
                    .then(() => {
                        console.log("수동 이용 안함 성공");
                        setNotUseDays(id)
                        window.location.reload();
                    })
                    .catch((error) => {
                        console.error("수동 이용 안함 에러");
                    });
                navigate('/calendar');
            }
        }
    };

    useEffect(() => {
        //이용일 조회
        axios.get(`${UserBaseApi}/order/${format(currentMonth, 'yyyy')}/${format(currentMonth, 'MM')}`, config)
            .then(res => setUseDays(res.data))
            .catch(error => {
                console.error("유저 이용일 조회 실패", error);
            });
        //키클락 로그인은 적용X

        //공공데이터 휴무 조회
        axios
            .get(
                `${holidayBaseApi}solYear=${format(currentMonth, 'yyyy')}&solMonth=${format(currentMonth, 'MM')}&ServiceKey=${holidayServiceKey}`
            )
            .then((res) => {
                setHoliday(res.data.response.body.items.item);
            })
            .catch((err) => console.log('dayoffError', err));
    }, [format(currentMonth, 'MM')])

    //이용일만 따로 저장
    function getRecognizedOrderDates(data) {
        return data
            .filter(item => item.recognize === true)
            .map(item => item.orderDate);
    }
    const recognizedOrderDates = getRecognizedOrderDates(useDays);
    //메뉴만 따로 저장
    function getRecognizedMenus(data) {
        return data
            .filter(item => item.recognize === true)
            .map(item => item.menu);
    }
    const recognizedMenus = getRecognizedMenus(useDays);
    //이용안하는 날 따로 저장
    function getRecognizedNotUseDates(data) {
        return data
            .filter(item => item.recognize === false)
            .map(item => item.orderDate);
    }
    const recognizedNotUseDates = getRecognizedNotUseDates(useDays);


    const handleCancel = () => {
        navigate('/calendar');
    };

    function isHoliday(day) {
        const holidayDates = storeoff.filter(item => item.off).map(item => item.date);
        const formattedDay = format(day, 'yyyyMMdd');
        return holidayDates.includes(formattedDay);
    }

    function isHoliday1(day) {
        if (!Array.isArray(holiday)) {
            // holiday 변수가 배열이 아닌 경우 처리
            return false;
        }

        const holiday1 = holiday.map(item => item.locdate.toString());
        const formattedDay1 = format(day, 'yyyyMMdd');
        return holiday1.includes(formattedDay1);
    }


    //10시30분 배경색 변환
    const isAfter1030 = currentHour > 10 || (currentHour === 10 && currentMinute >= 30);

    //달력 생성
    while (day <= endDate) {
        for (let i = 0; i < 7; i++) {
            formattedDate = format(day, 'd').padStart(2, '0').toString();
            const id = format(day, 'yyyyMMdd').toString();
            if (format(monthStart, 'M') !== format(day, 'M')) {
                dayss.push(
                    <div style={{ ...DivDay, backgroundColor: '#c2bebe' }} key={shortid.generate()} >
                        <span style={{ fontSize: '13px', color: '#666464', margin: '0.3vh 0.3vh' }}>{formattedDate}</span>
                    </div>
                );
            } else {

                let circlebackgroundColor = id < currentDate ? '#dec8b4' : '#f7dfc8'; //기본 원 배경색
                let textcolor = '';

                if (i >= 1 && i <= 5) {
                    const dayIndex = i - 1;
                    if ((Object.values(activeDays)[dayIndex] && defaultMenu !== '') || recognizedOrderDates.includes(id)) {  //이용일 원 배경색
                        circlebackgroundColor = id < currentDate ? '#c0d4ab' : '#e0f7c8';
                        //주이용일 이용안함 설정시 원 배경색 변경
                        if (recognizedNotUseDates.includes(id)) {
                            circlebackgroundColor = id < currentDate ? '#dec8b4' : '#f7dfc8';
                        }
                        //나중에 추가로 주이용 설정한걸 10시30분에 마감하여 서버로 전송
                    }

                } if (i == 6) {
                    circlebackgroundColor = '#dec8f7';
                    textcolor = '#64b5f6';
                } if (i === 0 || isHoliday(day) || isHoliday1(day)) {  //휴무 원 배경색 (사용안함), 글자색
                    circlebackgroundColor = '#dec8f7';
                    textcolor = '#f44336';

                }


                dayss.push(
                    <div
                        style={{
                            ...DivDay,
                            // 10시30분 이후에 배경색 변하게 설정
                            backgroundColor: (id < currentDate) || (id === currentDate && isAfter1030 === true) ? '#f5f5f5' : 'white',
                            pointerEvents: isHoliday(day) || isHoliday1(day) || (i === 0 || i === 6) ? 'none' : 'auto'
                        }}
                        onClick={() => {
                            //마감 이후 클릭 불가능
                            if (id >= currentDate && !isHoliday(day) && !isHoliday1(day) && !(i === 0 || i === 6) && !(id === currentDate && isAfter1030 === true)) {
                                onDay(id);
                            }
                        }}
                        key={shortid.generate()}
                    >
                        <div style={{ display: 'flex', flexDirection: 'row', justifyContent: 'space-between' }}>
                            <span style={{ fontSize: '13px', margin: '0.3vh 0.3vh', color: textcolor || 'black' }}>{formattedDate}</span>
                            {circlebackgroundColor !== '#dec8f7' && (
                                <div style={{
                                    margin: '1.0vh 0.7vh', width: '1vh', height: '1vh', backgroundColor: circlebackgroundColor,
                                    color: id < currentDate ? '#f20c0c' : 'black', borderRadius: '10px', border: '1px solid black'
                                }}></div>
                            )}
                        </div>
                        {(circlebackgroundColor === '#e0f7c8' || circlebackgroundColor === '#c0d4ab') && (
                            <p style={{ textAlign: 'right', fontSize: '1vh', marginBottom: '1vh', marginTop: 0 }}>
                                {/* 이용일 설정돼 있으면 해당하는 메뉴 출력*/}
                                {recognizedOrderDates.includes(id) ? recognizedMenus[recognizedOrderDates.indexOf(id)] : useMenuIds[id] || defaultMenu}
                            </p>
                        )}
                    </div>
                );
            }
            day = addDays(day, 1);
        }
        line.push(
            <div style={DivWeek} key={shortid.generate()}>
                {dayss}
            </div>
        );
        dayss = [];
    }

    //월 넘기기
    const prevMonth = () => {
        setCurrentMonth(subMonths(currentMonth, 1));
    };
    const nextMonth = () => {
        setCurrentMonth(addMonths(currentMonth, 1));
    };

    //월 바뀔때 마다 새로고침
    useEffect(() => {
        //식당 휴뮤일 조회
        axios.get(`${UserBaseApi}/store/off/${format(currentMonth, 'yyyy')}/${format(currentMonth, 'MM')}`, config)
            .then(res => setStoreoff(res.data))

        axios
            .get(`${UserBaseApi}/policy/date`, config)
            .then(res => {
                const receivedDefaultMenu = res.data.defaultMenu;

                axios.get(`${UserBaseApi}/menu`, config)
                    .then(res => {
                        const menuList = res.data;
                        //const id = menuList.map(menu => menu.id);
                        const updatedAquaticCreatures = menuList.map((menu) => ({
                            label: menu.name,
                            value: menu.id.toString(),
                            id: menu.id
                        }));
                        updatedAquaticCreatures.unshift({ label: '예약 취소', value: '메뉴를 선택해주세요.', id: 0 });
                        setAquaticCreatures(updatedAquaticCreatures);

                        const defaultMenu = menuList.find(menu => menu.id === receivedDefaultMenu);
                        if (defaultMenu) {
                            setDefaultMenu(defaultMenu.name);
                        }
                    })
                    .catch(error => {
                        console.error("메뉴 리스트 조회 실패:", error);
                    });
            })
            .catch(error => {
                console.error("유저 정책 조회 실패:", error);
            });

    }, [format(currentMonth, 'MM')])


    //메뉴 리스트
    const [selectedOption, setSelectedOption] = useState(aquaticCreatures[0]);
    const [useMenuID, setUseMenuID] = useState();

    const handleListChange = (selectedOptions) => {
        setSelectedOption(selectedOptions);
        setUseMenuID(selectedOptions.id);
    };
    const matchedMenu = menus.find((menu) => menu.id === useMenuID);


    return (
        <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
            <p style={{ fontWeight: 'bold', marginBottom: '20px' }}>요일을 클릭하여 이용일을 수정할 수 있어요.</p>
            <div>
                <ActiveDays activeDays={activeDays} />
                <div style={Wrapper}>
                    <div style={{ ...HeaderW, fontSize: '20px' }}>
                        <AiOutlineLeft style={{ ...ArrowCSS, marginLeft: '20px' }} onClick={prevMonth} />
                        <span style={{ color: '#64b5f6' }}>
                            {format(currentMonth, 'yyyy')}. {format(currentMonth, 'MM')}
                        </span>
                        <AiOutlineRight style={{ ...ArrowCSS, marginRight: '20px' }} onClick={nextMonth} />
                    </div>
                    <div style={DaysWrapper}>{days}</div>
                    <div style={DivWrapper}>{line}</div>
                </div>
            </div>

            {DayPathMatch ? (
                <div style={WriteWrapper}>
                    <div style={WriteTitle}>
                        <span style={{ marginRight: '5px' }}>{`${DayPathMatch.params.id.slice(0, 4)}년 ${DayPathMatch.params.id.slice(4, 6)}월 ${DayPathMatch.params.id.slice(6, 8)}일`}</span>
                    </div>
                    <div>
                        <Select
                            options={aquaticCreatures}
                            value={selectedOption}
                            onChange={handleListChange}
                            styles={selectStyles}
                        />
                    </div>

                    {/* 메뉴 상세정보 */}
                    <div style={WriteInfo}>
                        {matchedMenu && (
                            <div key={matchedMenu.id}>
                                <p style={{ margin: 0, fontWeight: 'bold' }}>{matchedMenu.name}</p>
                                <p style={{ marginTop: 0 }}>{matchedMenu.cost}원</p>
                                <div style={{ textAlign: 'left' }}>
                                    <p style={{ margin: 0 }}>{matchedMenu.details}</p>
                                    <p style={{ margin: 0 }}>{matchedMenu.info}</p>
                                    {matchedMenu.name === '오늘의 메뉴' && (
                                        <div style={{ border: '1px solid black' }}>
                                            <p style={{ margin: 0 }}>{todayMenu}</p>
                                        </div>
                                    )}
                                </div>
                            </div>
                        )}
                    </div>
                    <div>
                        <button style={saveButton} onClick={() => { handleSave(); }}>
                            저장
                        </button>
                        <button style={{ background: '#e07967', color: 'white', borderRadius: '10px' }} onClick={() => { handleCancel(); }}>
                            취소
                        </button>
                    </div>
                </div>
            ) : null}

            <div style={colorBox}>
                {/* <div className='color-box2'>
                    <div className='color-box1' style={{ backgroundColor: '#dec8f7', border: '1px solid black' }}></div>
                    <p>식당 휴일</p>
                </div> */}
                <div style={colorBox2}>
                    <div style={{ ...colorBox1, backgroundColor: '#e0f7c8', border: '1px solid black' }}></div>
                    <p>현재 이용일</p>
                </div>
                <div style={colorBox2}>
                    <div style={{ ...colorBox1, backgroundColor: '#f7dfc8', border: '1px solid black' }}></div>
                    <p>현재 미이용일</p>
                </div>
            </div>

            <div style={infoBox}>
                <p>✔이용일 당일은 오전10시에 예약이 마감됩니다.</p>
                <p>✔요일 클릭 시 당일 메뉴를 확인할 수 있습니다.</p>
                <p>✔식당운영시간: 11:30~13:00(석식 미운영) 매주 금요일 미운영</p>
            </div>
        </div>
    );
}

export default Calendar;