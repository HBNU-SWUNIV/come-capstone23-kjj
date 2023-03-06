package com.hanbat.zanbanzero.dto.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanbat.zanbanzero.entity.order.OrderDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDetailsDto {

//    private static final Map<Integer, String> paymentType = Map.of(
//            0, "현장결제",
//            2, "카카오페이",
//            3, "신용카드"
//    );
//
//    public Integer getPaymentKey() {
//        for (int key : paymentType.keySet()) {
//            if (paymentType.get(key).equals(payment)) {
//                return key;
//            }
//        }
//        return null;
//    }

    private Long id;
    private Long orderId;

    private int cost;
    private Map<String, Integer> menu;
    private int payment;

    public static OrderDetailsDto createOrderMenuDto(OrderDetails orderDetails) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return new OrderDetailsDto(
                orderDetails.getId(),
                orderDetails.getOrders().getId(),
                orderDetails.getCost(),
                objectMapper.readValue(orderDetails.getMenu(), new TypeReference<Map<String, Integer>>() {}),
                orderDetails.getPayment()
        );
    }
}
