import React from 'react';
import { FaArrowRight } from 'react-icons/fa';
import { FaArrowLeft } from 'react-icons/fa';
import { Link } from 'react-router-dom';

function Guide2() {
    return (
        <div>
            <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
                <h1 style={{ textAlign: 'center', color: '#A93528', marginTop: '50px', marginBottom: 0 }}>한밭대학교</h1>
                <h2 style={{ textAlign: 'center', color: '#A93528' }}>이용 요일과 메뉴를 설정해주세요.</h2>
                <p style={{ textAlign: 'center', marginTop: '70px' }}>
                    · 기본으로 선택할 메뉴를 설정해주세요.
                    <br />· 이용 요일마다 선택하신 메뉴를 자동으로 예약해드려요.
                    <br />· 이후에 수정할 수 있어요.
                    <br />· 매일 그날 먹고싶은 메뉴로 수정할 수 있어요.</p>

                <p style={{ textAlign: 'center', marginTop: '30px' }}>
                    기본메뉴<br />
                    <select defaultValue="" style={{ width: '200px', height: '30px', borderRadius: '5px' }}>
                        <option value="" disabled>선택해주세요.</option>
                        <option value="menu1">메뉴 1</option>
                        <option value="menu2">메뉴 2</option>
                        <option value="menu3">메뉴 3</option>
                    </select>
                </p>

                <div style={{ position: 'absolute', bottom: '100px', left: '30px' }}>
                    <button>
                        <Link to='/Guide1' style={{ display: 'flex', alignItems: 'center' }}>
                            <FaArrowLeft style={{ marginTop: '5px', marginBottom: '5px' }} />
                        </Link>
                    </button>
                </div>

                <div style={{ position: 'absolute', bottom: '100px', right: '30px' }}>
                    <button>
                        <Link to='/setting' style={{ display: 'flex', alignItems: 'center' }}>
                            <FaArrowRight style={{ marginTop: '5px', marginBottom: '5px' }} />
                        </Link>
                    </button>
                </div>
            </div>
        </div>
    );
}

export default Guide2;