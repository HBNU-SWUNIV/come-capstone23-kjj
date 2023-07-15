package com.hanbat.zanbanzero.controller.order;

import com.hanbat.zanbanzero.auth.jwt.JwtTemplate;
import com.hanbat.zanbanzero.auth.jwt.JwtUtil;
import com.hanbat.zanbanzero.dto.order.LastOrderDto;
import com.hanbat.zanbanzero.dto.order.OrderDto;
import com.hanbat.zanbanzero.exception.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.exceptions.WrongRequestDetails;
import com.hanbat.zanbanzero.service.order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
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
    @PostMapping("order/cancel/{year}/{month}/{day}")
    public ResponseEntity<String> cancelOrder(HttpServletRequest request, @PathVariable int year, @PathVariable int month, @PathVariable int day) throws WrongRequestDetails, CantFindByIdException {
        String username = JwtUtil.getUsernameFromToken(request.getHeader(JwtTemplate.HEADER_STRING));
        orderService.cancelOrder(username, year, month, day);
        return ResponseEntity.status(HttpStatus.OK).body("취소되었습니다.");
    }

    @Operation(summary="수동으로 이용함 설정")
    @PostMapping("order/add/{menuId}/{year}/{month}/{day}")
    public ResponseEntity<String> addOrder(HttpServletRequest request, @PathVariable Long menuId, @PathVariable int year, @PathVariable int month, @PathVariable int day) throws CantFindByIdException {
        String username = JwtUtil.getUsernameFromToken(request.getHeader(JwtTemplate.HEADER_STRING));
        orderService.addOrder(username, menuId, year, month, day);
        return ResponseEntity.status(HttpStatus.OK).body("등록되었습니다.");
    }

    @Operation(summary="일 단위 이용정보 조회")
    @GetMapping("order/{year}/{month}/{day}")
    public ResponseEntity<OrderDto> getOrder(HttpServletRequest request, @PathVariable int year, @PathVariable int month, @PathVariable int day) {
        String username = JwtUtil.getUsernameFromToken(request.getHeader(JwtTemplate.HEADER_STRING));
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrder(username, year, month, day));
    }

    @Operation(summary="특정 유저의 전체 이용내역 개수 조회(페이지 구성용)", description="페이지 사이즈보다 작을 경우 null")
    @GetMapping("order/count")
    public ResponseEntity<Integer> countUserOrders(HttpServletRequest request) {
        String username = JwtUtil.getUsernameFromToken(request.getHeader(JwtTemplate.HEADER_STRING));
        int result = orderService.countPages(username);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary="유저의 n번 페이지 이용내역 조회", description="페이지 사이즈 = 10")
    @GetMapping("order/show/{page}")
    public ResponseEntity<List<OrderDto>> getOrders(HttpServletRequest request, @PathVariable int page) {
        String username = JwtUtil.getUsernameFromToken(request.getHeader(JwtTemplate.HEADER_STRING));
        List<OrderDto> result = orderService.getOrdersPage(username, page);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary="유저의 가장 최근 이용내역 조회")
    @GetMapping("order/last")
    public ResponseEntity<LastOrderDto> getLastOrder(HttpServletRequest request) {
        String username = JwtUtil.getUsernameFromToken(request.getHeader(JwtTemplate.HEADER_STRING));
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getLastOrder(username));
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
