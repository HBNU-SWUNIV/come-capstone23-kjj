package com.hanbat.zanbanzero.service.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanbat.zanbanzero.entity.order.Order;
import com.hanbat.zanbanzero.entity.order.OrderDetails;
import com.hanbat.zanbanzero.dto.order.OrderDto;
import com.hanbat.zanbanzero.dto.order.OrderDetailsDto;
import com.hanbat.zanbanzero.exception.controller.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.controller.exceptions.RequestDataisNull;
import com.hanbat.zanbanzero.repository.order.OrderDetailsRepository;
import com.hanbat.zanbanzero.repository.order.OrderRepository;
import com.hanbat.zanbanzero.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailsRepository orderDetailsRepository;

    @Transactional
    public void addOrder(OrderDetailsDto dto, Long id) throws RequestDataisNull, JsonProcessingException {
        if (dto.getMenu() == null) {
            throw new RequestDataisNull("데이터가 부족합니다.");
        }

        OrderDto orderDto = OrderDto.builder()
                .updated(new Date().toString())
                .userId(id)
                .recognize(0)
                .build();

        Order order = Order.createOrder(orderDto, userRepository.getReferenceById(id));
        orderRepository.save(order);

        OrderDetails orderDetails = OrderDetails.createOrderDetails(dto, order);
        orderDetailsRepository.save(orderDetails);
    }

    @Transactional
    public void deleteOrder(Long id) throws CantFindByIdException {
        Order order = orderRepository.findById(id).orElseThrow(CantFindByIdException::new);

        order.setRecognizeToCancel();
    }

    public List<OrderDto> getOrders(Long id) {
        List<Order> orders = orderRepository.findByUserId(id);

        return orders.stream()
                .map(order -> {
                    try {
                        return OrderDto.createOrderDto(order);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    public OrderDetailsDto getOrderDetails(Long id) throws CantFindByIdException, JsonProcessingException {
        OrderDetails orderDetails = orderDetailsRepository.getOrderDetails(id).orElseThrow(CantFindByIdException::new);

        return OrderDetailsDto.createOrderMenuDto(orderDetails);
    }
}
