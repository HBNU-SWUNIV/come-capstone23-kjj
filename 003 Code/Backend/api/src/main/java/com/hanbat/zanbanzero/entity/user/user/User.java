package com.hanbat.zanbanzero.entity.user.user;

import com.hanbat.zanbanzero.dto.user.user.UserDto;
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

    public void setUsername(String username) {
        this.username = username;
    }
}
