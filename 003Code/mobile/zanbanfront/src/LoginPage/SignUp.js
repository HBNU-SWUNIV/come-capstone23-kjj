import React, { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import axios from 'axios';
import { FiEye, FiEyeOff } from 'react-icons/fi';
import { ConfigWithToken, UserBaseApi } from '../auth/authConfig';
import Swal from "sweetalert2";
import { motion } from 'framer-motion';

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
                Swal.fire({
                    icon: 'warning',
                    text: `비밀번호를 다시 확인해 주세요.`,
                    confirmButtonText: "확인",
                })
            } else {
                Swal.fire({
                    icon: 'warning',
                    text: `비밀번호를 다시 확인해 주세요.`,
                    confirmButtonText: "확인",
                })
            }
        } else if (password.trim() === '') {
            Swal.fire({
                icon: 'warning',
                text: `비밀번호를 입력해주세요.`,
                confirmButtonText: "확인",
            })
        } else {
            let body = {
                username,
                password
            };

            if (username.trim() === '') {
                Swal.fire({
                    icon: 'warning',
                    text: `ID를 입력해주세요.`,
                    confirmButtonText: "확인",
                })
                return;
            }
            axios.get(`${UserBaseApi}/login/join/check?username=${username}`)
                .then(res => {
                    if (res.data === true) {
                        Swal.fire({
                            icon: 'warning',
                            text: `ID를 다시 확인해주세요.`,
                            confirmButtonText: "확인",
                        })
                    }
                    if (res.data === false) {
                        axios.post(`${UserBaseApi}/login/join`, body)
                            .then(res => {
                                console.log(res.data);
                                Swal.fire({
                                    icon: 'success',
                                    text: `회원가입이 완료되었습니다.`,
                                    confirmButtonText: "확인",
                                });
                                navigate('/login'); // 회원가입 성공 시에만 로그인 페이지로 이동
                            })
                            .catch(err => {
                                console.log(err);
                            });
                    }
                })
                .catch(error => {
                    console.log(error);
                    setIdMessage("회원가입 중 오류가 발생했습니다.");
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
            .get(`/api/user/login/join/check?username=${username}`)
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
        <motion.div
            initial={{ y: '100%' }}
            animate={{ y: 0 }}
            exit={{ y: '100%' }}
            transition={{ duration: 0.5, stiffness: 120 }}
        >
            <div></div>
            <div className='sigunup' style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center' }}>
                <Link to='/login' style={{ color: 'inherit', textDecoration: 'none' }}>
                    <div style={{ marginTop: '30px', marginBottom: '40px', color: '#A93528' }}>
                        <h1 style={{ marginBottom: 0 }}>식단미리</h1>
                        <p style={{ display: 'flex', justifyContent: 'center', marginTop: 0, fontSize: '12px' }}>구내식당 메뉴예약 시스템</p>
                    </div>
                </Link>

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
        </motion.div>
    );
};

export default SignUp;
