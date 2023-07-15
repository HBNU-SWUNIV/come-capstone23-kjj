package com.hanbat.zanbanzero.controller.order;

import com.hanbat.zanbanzero.dto.order.LastOrderDto;
import com.hanbat.zanbanzero.dto.order.OrderDto;
import com.hanbat.zanbanzero.exception.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.exceptions.WrongRequestDetails;
import com.hanbat.zanbanzero.service.order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/")
public class OrderUserApiController {

    private final OrderService orderService;

    @Operation(summary="수동으로 이용안함 설정")
    @PostMapping("{id}/order/cancel/{year}/{month}/{day}")
    public ResponseEntity<String> cancelOrder(@PathVariable Long id, @PathVariable int year, @PathVariable int month, @PathVariable int day) throws WrongRequestDetails, CantFindByIdException {
        orderService.cancelOrder(id, year, month, day);
        return ResponseEntity.status(HttpStatus.OK).body("취소되었습니다.");
    }

    @Operation(summary="수동으로 이용함 설정")
    @PostMapping("{id}/order/add/{menuId}/{year}/{month}/{day}")
    public ResponseEntity<String> addOrder(@PathVariable Long id, @PathVariable Long menuId, @PathVariable int year, @PathVariable int month, @PathVariable int day) throws CantFindByIdException {
        orderService.addOrder(id, menuId, year, month, day);
        return ResponseEntity.status(HttpStatus.OK).body("등록되었습니다.");
    }

    @Operation(summary="특정 유저의 전체 주문내역 개수 조회(페이지 구성용)", description="페이지 사이즈보다 작을 경우 null")
    @GetMapping("{id}/order/count")
    public ResponseEntity<Integer> countUserOrders(@PathVariable Long id) {
        int result = orderService.countPages(id);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary="특정 유저의 n번 페이지 주문내역 조회", description="페이지 사이즈 = 10")
    @GetMapping("{id}/order/show/{page}")
    public ResponseEntity<List<OrderDto>> getOrders(@PathVariable Long id, @PathVariable int page) {
        List<OrderDto> result = orderService.getOrdersPage(id, page);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary="id 유저의 가장 최근 주문내역 조회")
    @GetMapping("{id}/order/last")
    public ResponseEntity<LastOrderDto> getLastOrder(@PathVariable Long id) {
        LastOrderDto result = orderService.getLastOrder(id);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary="id 오더 정보 조회(QR data 용)")
    @GetMapping("order/{id}")
    public ResponseEntity<LastOrderDto> getOrderById(@PathVariable Long id) throws CantFindByIdException {
        LastOrderDto result = orderService.getOrderById(id);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary="QR코드 조회")
    @GetMapping("order/{id}/qr")
    public void getOrderQr(HttpServletResponse response, @PathVariable Long id) throws Exception {
        orderService.getOrderQr(response, id);
    }
}
