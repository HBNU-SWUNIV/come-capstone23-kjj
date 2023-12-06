package com.hanbat.zanbanzero.dto.order;

import com.hanbat.zanbanzero.entity.order.Order;
import com.hanbat.zanbanzero.service.DateUtil;

public record LastOrderDto(
        Long id,
        String menu,
        int cost,
        String orderDate,
        boolean recognize
) {

    public static LastOrderDto from(Order order) {
        return new LastOrderDto(
                order.getId(),
                order.getMenu(),
                order.getCost(),
                DateUtil.makeLocaldateToFormatterString(order.getOrderDate()),
                order.isRecognize()
        );
    }
}
