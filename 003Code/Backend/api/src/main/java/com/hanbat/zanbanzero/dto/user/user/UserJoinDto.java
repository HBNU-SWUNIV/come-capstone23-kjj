package com.hanbat.zanbanzero.dto.user.user;

import com.hanbat.zanbanzero.entity.user.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserJoinDto {
    private String username;
    private String password;

    public static UserJoinDto of(User user) {
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
