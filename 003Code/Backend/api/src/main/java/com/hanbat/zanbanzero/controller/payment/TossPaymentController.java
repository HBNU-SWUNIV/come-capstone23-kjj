//package com.hanbat.zanbanzero.controller.payment;
//
//import com.hanbat.zanbanzero.service.payment.TossPaymentService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/payment")
//@RequiredArgsConstructor
//public class TossPaymentController {
//
//    private final TossPaymentService tossPaymentService;
//
//    @GetMapping("/toss/success/{orderId}")
//    public ResponseEntity<Boolean> paymentSuccess(@PathVariable("orderId") Long id, @RequestParam("paymentKey") String paymentKey, @RequestParam("orderId") String orderId, @RequestParam("amount") String amount) throws IllegalAccessException {
//        return ResponseEntity.ok(tossPaymentService.success(id, paymentKey, orderId, amount));
//    }
//
//    @GetMapping("/toss/fail")
//    public ResponseEntity<TossPaymentFail> paymentFail(@RequestParam("code") String code, @RequestParam("orderId") String orderId, @RequestParam("message") String message) {
//        return ResponseEntity.status(400).body(tossPaymentService.fail(code, orderId, message));
//    }
//}
