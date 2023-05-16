package com.hanbat.zanbanzero.dto.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanbat.zanbanzero.entity.order.Order;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDto {

    private Long id;

    private String menu;
    private int cost;
    private String orderDate;
    private boolean recognize;

    public static OrderDto createOrderDto(Order order) {
        return new OrderDto(
                order.getId(),
                order.getMenu(),
                order.getCost(),
                order.getOrderDate(),
                order.isRecognize()
        );
    }
}
