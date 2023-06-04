package com.hanbat.zanbanzero.entity.user.user;

import com.hanbat.zanbanzero.dto.user.user.UserDto;
import com.hanbat.zanbanzero.dto.user.user.UserJoinDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String roles;

    public static User of(UserDto dto) {
        return new User(
                dto.getId(),
                dto.getUsername(),
                dto.getPassword(),
                dto.getRoles()
        );
    }

    public static User of(UserJoinDto dto) {
        return new User(
                null,
                dto.getUsername(),
                dto.getPassword(),
                "ROLE_USER"
        );
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
