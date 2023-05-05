package com.hanbat.zanbanzero.dto.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanbat.zanbanzero.entity.menu.Menu;
import com.hanbat.zanbanzero.entity.order.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LastOrderDto {
    private Long id;

    private String menu;
    private int cost;
    private String orderDate;
    private boolean recognize;

    public static LastOrderDto createOrderDto(Order order, String menu) {
        return new LastOrderDto(
                order.getId(),
                menu,
                order.getCost(),
                order.getOrderDate(),
                order.isRecognize()
        );
    }
}
