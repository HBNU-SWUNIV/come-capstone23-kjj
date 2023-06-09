package com.hanbat.zanbanzero.dto.user.user;

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

    public void setEncodePassword(BCryptPasswordEncoder bCryptPasswordEncoder) {
        password = bCryptPasswordEncoder.encode(password);
    }

    public boolean checkForm() {
        if (username == null || password == null) return false;
        else if (username.equals("") || password.equals("")) return false;
        return true;
    }
}
