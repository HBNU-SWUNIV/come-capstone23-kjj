package com.hanbat.zanbanzero.dto.order;

import com.hanbat.zanbanzero.entity.order.Order;
import com.hanbat.zanbanzero.service.DateTools;
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
    private boolean expired;
    private boolean payment;

    public static OrderDto of(Order order) {
        return new OrderDto(
                order.getId(),
                order.getMenu(),
                order.getCost(),
                DateTools.makeLocaldateToFormatterString(order.getOrderDate()),
                order.isRecognize(),
                order.isExpired(),
                order.isPayment()
        );
    }
}
