package com.hanbat.zanbanzero.dto.user.user;

import com.hanbat.zanbanzero.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Data
@AllArgsConstructor
public class UserJoinDto {
    private String username;
    private String password;

    public static UserJoinDto from(User user) {
        return new UserJoinDto(
                user.getUsername(),
                user.getPassword()
        );
    }

    public void setEncodePassword(BCryptPasswordEncoder bCryptPasswordEncoder) {
        password = bCryptPasswordEncoder.encode(password);
    }

    public boolean checkForm() {
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) return false;
        return true;
    }
}
