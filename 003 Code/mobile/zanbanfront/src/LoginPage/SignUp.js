import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { FiEye, FiEyeOff } from 'react-icons/fi';
import { ConfigWithToken, UserBaseApi } from '../auth/authConfig';

const SignUp = () => {
    const [username, setusername] = useState('');
    const [password, setPassword] = useState('');
    const [confirmpassword, setConfirmPassword] = useState('');
    const [email, setEmail] = useState('');
    const [phone, setPhone] = useState('');
    const navigate = useNavigate();
    const [showPassword, setShowPassword] = useState(false);
    const [showConfirmPassword, setShowConfirmPassword] = useState(false);

    const config = ConfigWithToken();

    const togglePasswordVisibility = () => {
        setShowPassword(!showPassword);
    };

    const toggleConfirmPasswordVisibility = () => {
        setShowConfirmPassword(!showConfirmPassword);
    };

    const handleSignUp = () => {
        if (password !== confirmpassword) {
            if (confirmpassword.trim() === '') {
                alert("비밀번호를 다시 확인해 주세요.");
            } else {
                alert("비밀번호를 다시 확인해 주세요");
            }
        } else if (password.trim() === '') {
            alert("비밀번호를 입력해주세요.");
        } else {
            let body = {
                username,
                password
            };

            if (username.trim() === '') {
                alert("ID를 입력해주세요.");
                return;
            }
            axios.get(`${UserBaseApi}/login/join/check?username=${username}`, config)
                .then(res => {
                    if (res.data === true) {
                        alert("ID를 다시 확인해 주세요.")
                    }
                    if (res.data === false) {
                        axios.post(`${UserBaseApi}/login/join`, body, config)
                            .then(res => console.log(res))
                            .catch(err => console.log(err))
                        alert("회원가입이 완료되었습니다.")
                        navigate('/login')
                    }
                });
        }
    };

    const [idMessage, setIdMessage] = useState("");
    useEffect(() => {
        if (username.trim() === "") {
            setIdMessage("");
            return;
        }

        axios
            .get(`${UserBaseApi}/login/join/check?username=${username}`, config)
            .then((res) => {
                if (res.data === true) {
                    setIdMessage("이미 사용중인 ID입니다.");
                } else {
                    setIdMessage("사용 가능한 ID입니다.");
                }
            })
            .catch((error) => {
                console.log(error);
                setIdMessage("ID 확인 중 오류가 발생했습니다.");
            });
    }, [username]);

    const inputStyle = {
        width: '250px',
        height: '30px',
        borderRadius: '10px'
    };

    const PassinputStyle = {
        width: '228px',
        height: '30px',
        borderRadius: '10px'
    };

    const PassStyle = {
        alignItems: 'center',
        display: 'flex',
        padding: '5px',
    };

    const IdTextStyle = {
        fontSize: '10px',
        margin: 0,
        paddingLeft: '5px',
        color: idMessage === "사용 가능한 ID입니다." ? 'blue' : 'red',
    };

    const PassTextStyle = {
        fontSize: '10px',
        margin: 0,
        paddingLeft: '5px',
        color: password === confirmpassword ? 'blue' : 'red'
    };

    const IconStyle = {
        marginLeft: '2px',
        width: '20px',
        height: '20px',
    };

    return (
        <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center' }}>
            <div style={{ marginTop: '30px', marginBottom: '40px', color: '#A93528' }}>
                <h1 style={{marginBottom: 0}}>식단미리</h1>
                <p style={{display: 'flex', justifyContent: 'center', marginTop: 0, fontSize: '12px'}}>구내식당 메뉴예약 시스템</p>
            </div>

            <div>
                <p style={{ fontWeight: 'bold', marginBottom: 0 }}>아이디</p>
                <div style={{ padding: '5px' }}>
                    <label htmlFor="id">
                        <input type="text" id="id" placeholder="아이디" value={username} onChange={(e) => {
                            const regex = /^[A-Za-z0-9]+$/;
                            if (regex.test(e.target.value) || e.target.value === '') {
                                setusername(e.target.value);
                            }
                        }}
                            style={inputStyle} />
                    </label>
                </div>
                {username !== "" && (
                    <p style={IdTextStyle}>{idMessage}</p>
                )}
            </div>

            <div style={{ width: '268px', height: '130px' }}>
                <p style={{ fontWeight: 'bold', marginBottom: 0 }}>비밀번호</p>
                <div style={PassStyle}>
                    <label htmlFor="password">
                        <input
                            type={showPassword ? 'text' : 'password'}
                            id="password" placeholder="비밀번호" value={password}
                            onChange={(e) => {
                                const inputValue = e.target.value;
                                if (/^[^ㄱ-ㅎㅏ-ㅣ가-힣]*$/.test(inputValue)) {
                                    setPassword(inputValue);
                                }
                            }}
                            style={PassinputStyle}
                        />
                    </label>
                    {showPassword ? (
                        <FiEye style={IconStyle} onClick={togglePasswordVisibility} />
                    ) : (
                        <FiEyeOff style={IconStyle} onClick={togglePasswordVisibility} />
                    )}
                </div>


                <div style={PassStyle}>
                    <label htmlFor="confirm password">
                        <input
                            type={showConfirmPassword ? 'text' : 'password'}
                            id="confirmpassword" placeholder="비밀번호 재입력" value={confirmpassword}
                            onChange={(e) => {
                                const inputValue = e.target.value;
                                if (/^[^ㄱ-ㅎㅏ-ㅣ가-힣]*$/.test(inputValue)) {
                                    setConfirmPassword(inputValue);
                                }
                            }}
                            style={PassinputStyle}
                        />
                    </label>
                    {showConfirmPassword ? (
                        <FiEye style={IconStyle} onClick={toggleConfirmPasswordVisibility} />
                    ) : (
                        <FiEyeOff style={IconStyle} onClick={toggleConfirmPasswordVisibility} />
                    )}
                </div>
                {password !== '' && confirmpassword !== '' && (
                    <p style={PassTextStyle}>
                        {password === confirmpassword
                            ? '일치합니다'
                            : '비밀번호가 일치하지 않습니다'}
                    </p>
                )}
            </div>

            <div style={{ marginTop: '10px' }}>
                <p style={{ fontWeight: 'bold', marginBottom: 0 }}>이메일</p>
                <div style={{ padding: '5px' }}>
                    <label htmlFor="E-mail">
                        <input type="text" id="email" placeholder="이메일" value={email} onChange={(e) => setEmail(e.target.value)}
                            style={inputStyle} />
                    </label>
                </div>
            </div>

            <div style={{ marginTop: '10px' }}>
                <p style={{ fontWeight: 'bold', marginBottom: 0 }}>전화번호</p>
                <div style={{ padding: '5px' }}>
                    <label htmlFor="password">
                        <input type="text" id="phone" placeholder="전화번호" value={phone} onChange={(e) => setPhone(e.target.value)}
                            style={inputStyle} />
                    </label>
                </div>
            </div>

            <div style={{ marginTop: '30px' }}>
                <button onClick={handleSignUp} style={{ backgroundColor: '#A93528', color: 'white', border: 'none', borderRadius: '5px', width: '100px', height: '30px' }}>회원가입</button>
            </div>
        </div>
    );
};

export default SignUp;
