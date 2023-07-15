package com.hanbat.zanbanzero.entity.user.user;

import com.hanbat.zanbanzero.auth.login.dao.KeycloakUserInfoDAO;
import com.hanbat.zanbanzero.dto.user.LoginDto;
import com.hanbat.zanbanzero.dto.user.user.UserDto;
import com.hanbat.zanbanzero.dto.user.user.UserJoinDto;
import com.hanbat.zanbanzero.entity.order.Order;
import jakarta.persistence.*;
import lombok.*;

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

    public static User of(UserDto dto) {
        return new User(
                dto.getId(),
                null,
                null,
                null,
                dto.getUsername(),
                dto.getPassword(),
                dto.getRoles()
        );
    }

    public static User of(UserJoinDto dto) {
        return new User(
                null,
                null,
                null,
                null,
                dto.getUsername(),
                dto.getPassword(),
                dto.getRoles()
        );
    }

    public static User of(KeycloakUserInfoDAO dao, String roles) {
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
                dao.getSub() + "_keycloak",
                sb.toString(),
                roles
        );
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
