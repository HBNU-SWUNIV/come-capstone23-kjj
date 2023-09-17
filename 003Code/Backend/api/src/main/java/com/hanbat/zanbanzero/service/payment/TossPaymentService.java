//package com.hanbat.zanbanzero.service.payment;
//
//import com.hanbat.zanbanzero.auth.login.dto.KeycloakTokenDto;
//import com.hanbat.zanbanzero.dto.payment.TossPayment;
//import com.hanbat.zanbanzero.dto.payment.TossPaymentFail;
//import com.hanbat.zanbanzero.entity.order.OrderPayment;
//import com.hanbat.zanbanzero.repository.order.OrderPaymentRepository;
//import com.hanbat.zanbanzero.repository.order.OrderRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class TossPaymentService {
//    private final RestTemplate restTemplate;
//    private final OrderRepository orderRepository;
//    private final OrderPaymentRepository orderPaymentRepository;
//
//    @Value("${payment.toss.header}") private String tossApiHeader;
//
//    @Transactional
//    public Boolean success(Long id, String paymentKey, String orderId, String amount) throws IllegalAccessException {
//        if (orderPaymentRepository.existsById(id)) throw new IllegalAccessException("already payed");
//
//        String url = "https://api.tosspayments.com/v1/payments/confirm";
//
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.add("Authorization", tossApiHeader);
//        httpHeaders.add("Content-Type", "application/json");
//
//        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//        body.add("amount", amount);
//        body.add("orderId", orderId);
//        body.add("paymentKey", paymentKey);
//
//        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, httpHeaders);
//
//        ResponseEntity<TossPayment> response = restTemplate.exchange(
//                url,
//                HttpMethod.POST,
//                entity,
//                TossPayment.class
//        );
//
//        if (response.getStatusCode() == HttpStatusCode.valueOf(200)) {
//            orderPaymentRepository.save(OrderPayment.of(orderRepository.getReferenceById(id), paymentKey, orderId));
//            return true;
//        }
//        else return false;
//    }
//
//    public TossPaymentFail fail(String code, String orderId, String message) {
//        return TossPaymentFail.of(code, orderId, message);
//    }
//}
