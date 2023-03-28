package com.hanbat.zanbanzero.entity.order;

import com.hanbat.zanbanzero.entity.user.user.User;
import com.hanbat.zanbanzero.dto.order.OrderDto;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Timestamp updated;
    // 0 : 미승인, 1 : 승인, 2 : 취소
    private int recognize;

    public static Order createOrder(OrderDto dto, User user) {
        return new Order(
                dto.getId(),
                user,
                dto.getUpdated(),
                dto.getRecognize()
        );
    }

    public void setRecognizeToCancel() {
        recognize = 2;
    }
}
