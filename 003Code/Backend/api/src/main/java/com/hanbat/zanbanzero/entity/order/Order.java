package com.hanbat.zanbanzero.entity.order;

import com.hanbat.zanbanzero.entity.menu.Menu;
import com.hanbat.zanbanzero.entity.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Index;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;


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

    private String menu;

    private int cost;

    @Index(name = "order_date_index")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate orderDate;

    @NotNull
    private boolean recognize;
    @NotNull
    @Column(columnDefinition = "BOOLEAN DEFAULT false")
    private boolean expired;

    public void setMenu(Menu menu) {this.menu = menu.getName();}

    public void setRecognizeToCancel() {
        recognize = false;
    }

    public void setRecognizeToUse() { recognize = true; }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public static Order createNewOrder(User user, String menu, int cost, LocalDate date, boolean type) {
        return new Order(
                null,
                user,
                menu,
                cost,
                date,
                type,
                false
        );
    }
}
