package com.hanbat.zanbanzero.entity.user.user;

import com.hanbat.zanbanzero.dto.user.user.UserJoinDto;
import com.hanbat.zanbanzero.entity.order.Order;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserPolicy userPolicy;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserMypage userMypage;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> order;
    private String username;
    private String password;
    private String roles;
    private String loginDate;

    public static User of(UserJoinDto dto) {
        return new User(
                null,
                null,
                null,
                null,
                dto.getUsername(),
                dto.getPassword(),
                "ROLE_USER",
                null
        );
    }

    public static User of(String userSub, String roles) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        int length = 10;

        for (int i = 0; i < length; i++) {
            int digit = random.nextInt(10);
            sb.append(digit);
        }

        return new User(
                null,
                null,
                null,
                null,
                userSub,
                sb.toString(),
                roles,
                null
        );
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setLoginDate(LocalDate date) {
        this.loginDate = date.toString();
    }
}
