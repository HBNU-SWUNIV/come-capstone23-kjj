package com.hanbat.zanbanzero.dto.order;

import com.hanbat.zanbanzero.entity.order.Order;
import com.hanbat.zanbanzero.service.DateUtil;

public record OrderDto(
        Long id,
        String menu,
        int cost,
        String orderDate,
        boolean recognize,
        boolean expired,
        boolean payment
) {

    public static OrderDto from(Order order) {
        return new OrderDto(
                order.getId(),
                order.getMenu(),
                order.getCost(),
                DateUtil.makeLocaldateToFormatterString(order.getOrderDate()),
                order.isRecognize(),
                order.isExpired(),
                order.isPayment()
        );
    }
}
