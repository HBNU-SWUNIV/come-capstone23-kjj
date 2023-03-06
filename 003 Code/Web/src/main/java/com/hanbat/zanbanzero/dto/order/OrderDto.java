package com.hanbat.zanbanzero.dto.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanbat.zanbanzero.entity.order.Order;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(builderMethodName = "OrderDtoBuilder")
public class OrderDto {

    private Long id;
    private Long userId;

    private String updated;
    private int recognize;

    public static OrderDto createOrderDto(Order order) throws JsonProcessingException {
        return new OrderDto(
                order.getId(),
                order.getUser().getId(),
                order.getUpdated(),
                order.getRecognize()
        );
    }

    public static OrderDtoBuilder builder() {
        return OrderDtoBuilder();
    }
}
