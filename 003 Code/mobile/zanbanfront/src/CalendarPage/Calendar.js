import { addMonths, subMonths, format, startOfMonth, endOfMonth, startOfWeek, endOfWeek, addDays } from 'date-fns';
import React, { useState, useEffect } from "react";
import { AiOutlineLeft, AiOutlineRight } from 'react-icons/ai';
import { useMatch, useNavigate } from 'react-router-dom';
import shortid from 'shortid';
import Switch from 'react-switch';
import axios from "axios";
import { useSelector } from "react-redux";
import Select from 'react-select';


const ArrowCSS = { color: '#969696', fontSize: '20px' };

const Wrapper = {
    display: 'flex',
    width: '78vw',
    marginBottom: '15px',
    boxShadow: '10px 10px 15px rgba(0, 0, 0, 0.2)',
    flexDirection: 'column',
    alignItems: 'center',
    borderRadius: '15px 15px 0 0',
    border: '1px solid #5B5B5B',
};

const HeaderW = {
    width: '78vw',
    height: '8vh',
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
    span: {
        color: '#383838',
        fontSize: '25px',
        fontWeight: '600',
    },
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
    border: '0.1px solid #5B5B5B',
    borderRightStyle: 'none',
};

const DaysDiv = {
    width: '24vw',
    height: '6vh',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    border: '0.1px solid #5B5B5B',
    borderRightStyle: 'none',
};

const DaySpan = {
    'firstChild': {
        color: 'red',
    },
    'lastChild': {
        color: 'blue',
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
    width: '25px',
    height: '25px',
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
    const userid = useSelector(state => state.username.userid);
    const [activeDays, setActiveDays] = useState([]);
    const [menus, setMenus] = useState([]);

    const daysOfWeek = {
        monday: '월',
        tuesday: '화',
        wednesday: '수',
        thursday: '목',
        friday: '금',
    };

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



    useEffect(() => {
        axios
            .get(`/api/user/${userid}/policy/date`)
            .then(res => {
                setActiveDays(res.data);
            })
            .catch(error => {
                console.error("유저 정책 조회 실패", error);
            });

        axios.get(`/api/manager/menu`)
            .then(res => setMenus(res.data))

    }, []);


    //메뉴 조희
    const [todayMenu, setTodayMenu] = useState([]);
    const DayPathMatch = useMatch('/calendar/:id');
    //이용일 조회
    const [useDays, setUseDays] = useState([]);

    useEffect(() => {
        if (DayPathMatch && DayPathMatch.params && DayPathMatch.params.id) {
            const { id } = DayPathMatch.params;
            const year = id.slice(0, 4);
            const month = id.slice(4, 6);
            const day = id.slice(6, 8);

            axios
                .get(`/api/user/planner/${year}/${month}/${day}`)
                .then(res => setTodayMenu(res.data.menus))
                .catch((error) => {
                    console.error(error);
                    setTodayMenu([]);
                });

            //이용일 조회
            axios
                .get(`/api/user/order/${year}/${month}/${day}`)
                .then(res => {
                    setUseDays(res.data);
                    console.log(useDays);
                })
                .catch(error => {
                    console.error("유저 정책 조회 실패", error);
                });
        }
    }, [DayPathMatch]);

    const today = new Date();
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, '0');
    const day1 = String(today.getDate()).padStart(2, '0');
    const currentDate = `${year}${month}${day1}`;


    const navigate = useNavigate();
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

    //id마다 스위치 값 다르게 적용
    const [checkedStates, setCheckedStates] = useState({});
    const handleChange = (id) => {
        setCheckedStates((prevState) => ({
            ...prevState,
            [id]: !prevState[id],
        }));
    };

    const monthStart = startOfMonth(currentMonth);
    const monthEnd = endOfMonth(monthStart);
    const startDate = startOfWeek(monthStart);
    const endDate = endOfWeek(monthEnd);
    let day = startDate;
    let formattedDate = '';
    let dayss = [];
    let line = [];

    const onDay = (id) => {
        navigate(`/calendar/${id}`);
    }


    //메뉴 리스트
    const [aquaticCreatures, setAquaticCreatures] = useState([]);
    //휴무일 조회
    const [storeoff, setStoreoff] = useState([]);

    //이용메뉴
    const [defaultMenu, setDefaultMenu] = useState('');

    useEffect(() => {
        axios.get(`/api/manager/store/off/${format(currentMonth, 'yyyy')}/${format(currentMonth, 'MM')}`)
            .then(res => setStoreoff(res.data))

        axios
            .get(`/api/user/${userid}/policy/date`)
            .then(res => {
                const receivedDefaultMenu = res.data.defaultMenu;

                axios.get(`/api/manager/menu`)
                    .then(res => {
                        const menuList = res.data;
                        //const id = menuList.map(menu => menu.id);
                        const updatedAquaticCreatures = menuList.map((menu) => ({
                            label: menu.name,
                            value: menu.id.toString(),
                            id: menu.id
                        }));
                        setAquaticCreatures(updatedAquaticCreatures);

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
    }, [])

    //이용일별 이용 메뉴
    const [useMenuIds, setUseMenuIds] = useState({});

    //저장버튼 클릭시 동작
    const handleSave = () => {

        if (DayPathMatch && DayPathMatch.params && DayPathMatch.params.id) {
            const { id } = DayPathMatch.params;
            const year = id.slice(0, 4);
            const month = id.slice(4, 6);
            const day = id.slice(6, 8);

            //수동 이용함
            if (checkedStates[id]) {
                axios
                    .post(`/api/user/order/add/${useMenuID}/${year}/${month}/${day}`, 'false')
                    .then(() => {
                        console.log("수동 이용 성공");
                        setUseMenuIds(prevState => {
                            const menuName = aquaticCreatures.find(menu => menu.id === useMenuID).label;
                            return { ...prevState, [id]: menuName };
                        });
                    })
                    .catch((error) => {
                        console.error("수동 이용 에러발생");
                    });

                if (!useMenuID) {
                    setCheckedStates(originalCheckedStates);
                    alert("이용 메뉴를 선택해주세요.");
                    return;
                }
                setOriginalCheckedStates(checkedStates);
                navigate('/calendar');
            }

            //수동 이용안함
            if (!checkedStates[id]) {
                axios
                    .post(`/api/user/${userid}/order/cancel/${year}/${month}/${day}`, 'false')
                    .then(() => {
                        console.log("수동 이용 안함 성공");
                    })
                    .catch((error) => {
                        console.error("수동 이용 안함 에러");
                    });
                setOriginalCheckedStates(checkedStates);
                navigate('/calendar');
            }
        }
    };

    //취소버튼 클릭시 스위치 기본값으로
    const [originalCheckedStates, setOriginalCheckedStates] = useState({});
    const handleCancel = () => {
        setCheckedStates(originalCheckedStates);
        navigate('/calendar');
    };

    function isHoliday(day) {
        const holidayDates = storeoff.filter(item => item.off).map(item => item.date);
        const formattedDay = format(day, 'yyyyMMdd');

        return holidayDates.includes(formattedDay);
    }

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
                let backgroundColor = id < currentDate ? '#dec8b4' : '#f7dfc8'; //기본 배경색

                if (i >= 1 && i <= 5) {
                    const dayIndex = i - 1;

                    if (Object.values(activeDays)[dayIndex] || checkedStates[id]) {  //이용일 배경색
                        backgroundColor = '#e0f7c8';
                        if (id < currentDate) {
                            backgroundColor = '#c0d4ab';
                        }
                    }


                } if ((i === 0 || i === 6) || isHoliday(day)) {  //휴무 배경색
                    backgroundColor = '#dec8f7';
                    if (id < currentDate) {
                        backgroundColor = '#b2a1c4';
                    }
                }

                dayss.push(
                    <div
                        style={{
                            ...DivDay,
                            backgroundColor: backgroundColor,
                            color: id < currentDate ? '#7d7d7d' : 'black',
                            pointerEvents: isHoliday(day) || (i === 0 || i === 6) ? 'none' : 'auto'
                        }}
                        onClick={() => {
                            if (id >= currentDate && !isHoliday(day) && !(i === 0 || i === 6)) {
                                onDay(id);
                            }
                        }}
                        key={shortid.generate()}
                    >
                        <span style={{ fontSize: '13px', margin: '0.3vh 0.3vh' }}>{formattedDate}</span>
                        {(backgroundColor === '#e0f7c8' || backgroundColor === '#c0d4ab') && (
                            <p style={{ textAlign: 'right', fontSize: '1vh', marginBottom: '1vh', marginTop: 0 }}>{useMenuIds[id] || defaultMenu}</p>
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

    // 월 넘기기
    const prevMonth = () => {
        setCurrentMonth(subMonths(currentMonth, 1));
    };
    const nextMonth = () => {
        setCurrentMonth(addMonths(currentMonth, 1));
    };


    //메뉴 리스트
    const [selectedOption, setSelectedOption] = useState(aquaticCreatures[0]);
    const [useMenuID, setUseMenuID] = useState();

    const handleListChange = (selectedOptions) => {
        setSelectedOption(selectedOptions);
        setUseMenuID(selectedOptions.id);
    };

    const writeInfoArray = Array.from({ length: aquaticCreatures.length }, (_, index) => {
        const adjustedIndex = index + 2;
        const display = selectedOption && selectedOption.value === adjustedIndex.toString() ? 'block' : 'none';
        return { display };
    });


    return (
        <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
            <p style={{ fontWeight: 'bold', marginBottom: '20px' }}>요일을 클릭하여 이용일을 수정할 수 있어요.</p>
            <div>
                <ActiveDays activeDays={activeDays} />
                <div style={Wrapper}>
                    <div style={{ ...HeaderW, fontSize: '20px' }}>
                        <AiOutlineLeft style={{ ...ArrowCSS, marginLeft: '20px' }} onClick={prevMonth} />
                        <span>
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
                        <Switch
                            checked={checkedStates[DayPathMatch.params.id] || false}
                            onChange={() => handleChange(DayPathMatch.params.id)}
                            onColor="#91f321"
                            offColor="#ccc"
                            checkedIcon={false}
                            uncheckedIcon={false}
                        />
                    </div>
                    <div>
                        <Select
                            options={aquaticCreatures}
                            value={selectedOption}
                            onChange={handleListChange}
                            styles={selectStyles}
                        />
                    </div>
                    <div style={WriteInfo}>
                        {writeInfoArray.map((writeInfo, index) => (
                            <div style={writeInfo} key={index}>
                                <p style={{ margin: 0, fontWeight: 'bold' }}>{menus[index]?.name}</p>
                                <p style={{ marginTop: 0 }}>{menus[index]?.cost}원</p>
                                <div style={{ textAlign: 'left' }}>
                                    <p style={{ margin: 0 }}>{menus[index]?.details}</p>
                                    <p style={{ margin: 0 }}>{menus[index]?.info}</p>
                                </div>
                            </div>
                        ))}
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
                <div style={colorBox2}>
                    <div style={{ ...colorBox1, backgroundColor: '#dec8f7' }}></div>
                    <p>식당 휴일</p>
                </div>
                <div style={colorBox2}>
                    <div style={{ ...colorBox1, backgroundColor: '#e0f7c8' }}></div>
                    <p>현재 이용일</p>
                </div>
                <div style={colorBox2}>
                    <div style={{ ...colorBox1, backgroundColor: '#f7dfc8' }}></div>
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