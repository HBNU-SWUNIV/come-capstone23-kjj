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

    private Long menu;
    private int cost;
    private String orderDate;
    private boolean recognize;

    public static Order createOrder(OrderDto dto, User user) {
        return new Order(
                dto.getId(),
                user,
                dto.getMenu(),
                dto.getCost(),
                dto.getOrderDate(),
                dto.isRecognize()
        );
    }

    public void setMenu(Long menu_id) {menu = menu_id;}

    public void setRecognizeToCancel() {
        recognize = false;
    }

    public void setRecognizeToUse() { recognize = true;
    }
}
