package com.hanbat.zanbanzero.entity.order;

import com.hanbat.zanbanzero.entity.menu.Menu;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    private int cost;
    private String orderDate;
    private boolean recognize;

    public void setMenu(Menu menu) {this.menu = menu;}

    public void setRecognizeToCancel() {
        recognize = false;
    }

    public void setRecognizeToUse() { recognize = true;
    }
}
