import React, { useEffect, useRef, useState } from "react";
import {
    PaymentWidgetInstance,
    loadPaymentWidget,
    ANONYMOUS,
} from "@tosspayments/payment-widget-sdk";
import { Link, useLocation } from 'react-router-dom';
import axios from "axios";
import { ConfigWithToken, UserBaseApi } from '../auth/authConfig';

// const clientKey = "test_ck_D5GePWvyJnrK0W0k6q8gLzN97Eoq";
const clientKey = "test_ck_LlDJaYngronXNddwLRNVezGdRpXx";
const customerKey = "SwSYDa97ryVqyfgnFnEA7";

export function CheckoutPage() {
    const config = ConfigWithToken();

    const today = new Date();
    const QRyear = today.getFullYear();
    const QRmonth = String(today.getMonth() + 1).padStart(2, '0');
    const QRday = String(today.getDate()).padStart(2, '0');

    // 이용일 조회
    const [useDays, setUseDays] = useState([]);
    const [menuName, setMenuName] = useState('');
    const [userId, setUserId] = useState('');
    const [point, setPoint] = useState(0);
    const [checked, setChecked] = useState('');

    useEffect(() => {
        // 이용일 조회
        axios
            .get(`${UserBaseApi}/order/${QRyear}/${QRmonth}/${QRday}`, config)
            .then((res) => {
                setUseDays(res.data);
                setPrice(res.data.cost);
                setMenuName(res.data.menu);
                setUserId(res.data.id);
                console.log(res.data); // 데이터를 가져온 후에 로그를 출력
            })
            .catch((error) => {
                console.error('유저 이용일 조회 실패', error);
            });

        // 포인트 조회
        axios
            .get(`${UserBaseApi}/page`, config)
            .then((res) => {
                setPoint(res.data.point);
            })
            .catch((error) => {
                console.error('유저 포인트 조회 실패', error);
            });
    }, []);

    const paymentWidgetRef = useRef(null);
    const paymentMethodsWidgetRef = useRef(null);
    const [price, setPrice] = useState(10);

    const handlePointButtonClick = () => {
        axios
          .post(`/api/user/page/point`, { value: point }, config)
          .then((res) => {
            console.log('Axios 요청 성공:', res);
          })
          .catch((error) => {
            console.error('Axios 요청 실패:', error);
          });
      };

    useEffect(() => {
        (async () => {
            // ------  결제위젯 초기화 ------
            // 비회원 결제에는 customerKey 대신 ANONYMOUS를 사용하세요.
            const paymentWidget = await loadPaymentWidget(clientKey, customerKey);  // 회원 결제
            // const paymentWidget = await loadPaymentWidget(clientKey, ANONYMOUS);  // 비회원 결제

            // ------  결제위젯 렌더링 ------
            // 결제수단 UI를 렌더링할 위치를 지정합니다. `#payment-method`와 같은 CSS 선택자와 결제 금액 객체를 추가하세요.
            // DOM이 생성된 이후에 렌더링 메서드를 호출하세요.
            // https://docs.tosspayments.com/reference/widget-sdk#renderpaymentmethods선택자-결제-금액-옵션
            const paymentMethodsWidget = paymentWidget.renderPaymentMethods(
                "#payment-widget",
                { value: price },
                { variantKey: "DEFAULT" } // 렌더링하고 싶은 결제 UI의 variantKey
            );

            // ------  이용약관 렌더링 ------
            // 이용약관 UI를 렌더링할 위치를 지정합니다. `#agreement`와 같은 CSS 선택자를 추가하세요.
            // https://docs.tosspayments.com/reference/widget-sdk#renderagreement선택자
            paymentWidget.renderAgreement("#agreement");

            paymentWidgetRef.current = paymentWidget;
            paymentMethodsWidgetRef.current = paymentMethodsWidget;
        })();
    }, []);

    useEffect(() => {
        const paymentMethodsWidget = paymentMethodsWidgetRef.current;

        if (paymentMethodsWidget == null) {
            return;
        }

        // ------ 금액 업데이트 ------
        // 새로운 결제 금액을 넣어주세요.
        // https://docs.tosspayments.com/reference/widget-sdk#updateamount결제-금액
        paymentMethodsWidget.updateAmount(
            price
        );
    }, [price]);
    
    return (
        <div>
            <h1 style={{ textAlign: 'center' }}>주문서</h1>
            <div style={{ margin: '24px', padding: '5px', border: '1px solid' }}>
                <span>{`메뉴 이름 : ${menuName}`}</span>
                <br />
                <span>{`가격 : ${price}원`}</span>
            </div>
            <div>
                <label style={{ marginLeft: '24px' }}>
                    <input
                        type="checkbox"
                        onChange={(event) => {
                            setPrice(event.target.checked ? price - point : price + point);
                            setChecked(event.target.checked)
                        }}
                    />
                    {point}포인트 할인 적용

                </label>
            </div>
            <div id="payment-widget" />
            <div id="agreement" />
            <div style={{ display: 'flex', justifyContent: 'space-around' }}>
                <button
                    onClick={async () => {
                        const paymentWidget = paymentWidgetRef.current;
                        await handlePointButtonClick();

                        try {
                            // ------ '결제하기' 버튼 누르면 결제창 띄우기 ------
                            // 더 많은 결제 정보 파라미터는 결제위젯 SDK에서 확인하세요.
                            // https://docs.tosspayments.com/reference/widget-sdk#requestpayment결제-정보
                            await paymentWidget?.requestPayment({
                                orderId: "B6CkHEaUKKC79LElVWrJ2",
                                orderName: `${menuName}`,
                                customerName: `${userId}`,
                                successUrl: `http://kjj.kjj.r-e.kr:81/success`,
                                failUrl: `http://kjj.kjj.r-e.kr:81/fail`,
                            });

                        } catch (error) {
                            // 에러 처리하기
                            console.error(error);
                            if (error.code === 'USER_CANCEL') {
                                console.log('asdasd')
                                // 결제 고객이 결제창을 닫았을 때 에러 처리
                            } else if (error.code === 'INVALID_CARD_COMPANY') {
                                // 유효하지 않은 카드 코드에 대한 에러 처리
                            }
                        }
                    }}
                >
                    결제하기
                </button>
                <button >
                    <Link to="/home" style={{ color: "black", textDecoration: "none" }}>결제취소</Link>
                </button>
            </div>
        </div>
    );
}

export default CheckoutPage;
