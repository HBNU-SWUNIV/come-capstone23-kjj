package com.hanbat.zanbanzero.controller.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanbat.zanbanzero.dto.order.OrderDto;
import com.hanbat.zanbanzero.dto.order.OrderDetailsDto;
import com.hanbat.zanbanzero.exception.controller.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.controller.exceptions.WrongRequestDetails;
import com.hanbat.zanbanzero.service.order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderService orderService;

    @Operation(summary="주문 성공", description="")
    @PostMapping("/api/user/{id}/order/add")
    public ResponseEntity<String> addOrder(@RequestBody OrderDetailsDto orderDto, @PathVariable Long id) throws WrongRequestDetails, JsonProcessingException {
        orderService.addOrder(orderDto, id);
        return ResponseEntity.status(HttpStatus.OK).body("등록되었습니다.");
    }

    @Operation(summary="주문 취소", description="")
    @PatchMapping("/api/user/order/{id}/del")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) throws CantFindByIdException {
        orderService.deleteOrder(id);
        return ResponseEntity.status(HttpStatus.OK).body("취소되었습니다.");
    }

    @Operation(summary="특정 유저의 전체 주문내역 조회", description="")
    @GetMapping("/api/user/{id}/order/show")
    public ResponseEntity<List<OrderDto>> getOrders(@PathVariable Long id) {
        List<OrderDto> result = orderService.getOrders(id);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary="특정 주문의 상세정보 조회", description="")
    @GetMapping("/api/user/order/{id}/details")
    public ResponseEntity<OrderDetailsDto> getOrderDetails(@PathVariable Long id) throws CantFindByIdException, JsonProcessingException {
        OrderDetailsDto result = orderService.getOrderDetails(id);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
