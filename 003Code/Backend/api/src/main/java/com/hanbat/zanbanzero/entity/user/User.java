package com.hanbat.zanbanzero.entity.user;

import com.hanbat.zanbanzero.dto.user.user.UserJoinDto;
import com.hanbat.zanbanzero.entity.order.Order;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private UserPolicy userPolicy;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private UserMypage userMypage;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Order> order;

    private String username;
    private String password;
    private String roles;
    private String loginDate;

    public static User of(UserJoinDto dto, UserPolicy userPolicy, UserMypage userMypage) {
        return new User(
                null,
                userPolicy,
                userMypage,
                null,
                dto.username(),
                dto.password(),
                "ROLE_USER",
                null
        );
    }

    public static User of(UserJoinDto dto, String newUsername, UserPolicy userPolicy, UserMypage userMypage) {
        return new User(
                null,
                userPolicy,
                userMypage,
                null,
                newUsername,
                dto.password(),
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
    public void setUserPolicy(UserPolicy userPolicy) {
        this.userPolicy = userPolicy;
    }
    public void setUserMypage(UserMypage userMypage) {
        this.userMypage = userMypage;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setEncodePassword(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.password = bCryptPasswordEncoder.encode(password);
    }
    public void updateLoginDate() {
        loginDate = LocalDate.now().toString();
    }
}
