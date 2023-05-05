package com.hanbat.zanbanzero.dto.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanbat.zanbanzero.entity.order.Order;
import lombok.*;

import java.sql.Timestamp;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(builderMethodName = "OrderDtoBuilder")
public class OrderDto {

    private Long id;

    private String menu;
    private int cost;
    private String orderDate;
    private boolean recognize;

    public static OrderDto createOrderDto(Order order) throws JsonProcessingException {
        return new OrderDto(
                order.getId(),
                order.getMenu(),
                order.getCost(),
                order.getOrderDate(),
                order.isRecognize()
        );
    }

    public static OrderDtoBuilder builder() {
        return OrderDtoBuilder();
    }
}
