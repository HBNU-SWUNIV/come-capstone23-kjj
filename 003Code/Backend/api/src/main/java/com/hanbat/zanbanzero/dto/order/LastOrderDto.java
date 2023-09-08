package com.hanbat.zanbanzero.dto.order;

import com.hanbat.zanbanzero.entity.order.Order;
import com.hanbat.zanbanzero.service.DateTools;
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

    public static LastOrderDto of(Order order) {
        return new LastOrderDto(
                order.getId(),
                order.getMenu(),
                order.getCost(),
                DateTools.makeLocaldateToFormatterString(order.getOrderDate()),
                order.isRecognize()
        );
    }
}
