import { addMonths, subMonths, format, startOfMonth, endOfMonth, startOfWeek, endOfWeek, addDays } from 'date-fns';
import React, { useState, useEffect } from "react";
import { AiOutlineLeft, AiOutlineRight } from 'react-icons/ai';
import { useMatch, useNavigate, useParams } from 'react-router-dom';
import shortid from 'shortid';
import Switch from 'react-switch';
import axios from "axios";


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
    justifyContent: 'space-between',
    border: '0.1px solid #5B5B5B',
    borderRightStyle: 'none',
    span: {
        '&:first-child': {
            fontSize: '13px',
            fontWeight: '600',
            margin: '5px 5px',
        },
        '&:last-child': {
            marginTop: '0.2vh',
            marginRight: '0.2vw',
            fontSize: '12px',
            whiteSpace: 'pre-wrap',
        },
    },
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
    height: '25vh',
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
    height: '15vh',
    display: 'flex',
    justifyContent: 'center',
    whiteSpace: 'pre-wrap',
    textAlign: 'center',
};


function Calendar() {
    const [todayMenu, setTodayMenu] = useState([]);
    const DayPathMatch = useMatch('/calendar/:id');

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
        }
    }, [DayPathMatch]);


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

    //취소버튼 클릭시 스위치 기본값으로
    const [originalCheckedStates, setOriginalCheckedStates] = useState({});
    const handleCancel = () => {
        setCheckedStates(originalCheckedStates);
    };

    const monthStart = startOfMonth(currentMonth);
    const monthEnd = endOfMonth(monthStart);
    const startDate = startOfWeek(monthStart);
    const endDate = endOfWeek(monthEnd);
    let day = startDate;
    let formattedDate = '';
    let dayss = [];
    let line = [];

    //const DayPathMatch = useMatch('/calendar/:id');
    const onDay = (id) => {
        navigate(`/calendar/${id}`);
    }

    while (day <= endDate) {
        for (let i = 0; i < 7; i++) {
            formattedDate = format(day, 'd').padStart(2, '0').toString();
            const id = format(day, 'yyyyMMdd').toString();
            if (format(monthStart, 'M') !== format(day, 'M')) {
                dayss.push(
                    <div style={{ ...DivDay, backgroundColor: '#c2bebe' }} key={shortid.generate()}>
                        <span style={{ fontSize: '13px', color: '#666464', margin: '5px 5px' }}>{formattedDate}</span>
                    </div>
                );
            } else {
                dayss.push(
                    <div style={{ ...DivDay, backgroundColor: checkedStates[id] ? '#e0f7c8' : (i === 0 || i === 6 ? '#dec8f7' : '#f7dfc8') }} onClick={() => onDay(id)} key={shortid.generate()}>
                        <span>{formattedDate}</span>
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

    return (
        <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
            <p style={{ fontWeight: 'bold', marginBottom: '20px' }}>요일을 클릭하여 이용일을 수정할 수 있어요.</p>
            <div>
                <p style={{ fontSize: '12px', marginBottom: '2px' }}>현재 이용일:</p>
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
                    <div style={WriteInfo}>
                        <p style={{margin: 0}}>{todayMenu}</p>
                    </div>
                    <div>
                        <button style={{ marginRight: '10px' }} onClick={() => { setOriginalCheckedStates(checkedStates); navigate('/calendar'); }}>
                            저장
                        </button>
                        <button onClick={() => { handleCancel(); navigate('/calendar') }}>
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
                <p>✔백반단가: 5000원</p>
                <p>✔요일 클릭 시 당일 메뉴를 확인할 수 있습니다.</p>
                <p>✔식당운영시간: 11:30~13:00(석식 미운영) 매주 금요일 미운영</p>
                <p>✔방학기간에는 교직원식당을 운영하지 않으니, 학생식당을 이용해주시기 바랍니다.</p>
            </div>
        </div>
    );
}

export default Calendar;