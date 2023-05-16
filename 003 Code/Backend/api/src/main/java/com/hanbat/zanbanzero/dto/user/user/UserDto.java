package com.hanbat.zanbanzero.dto.user.user;

import com.hanbat.zanbanzero.entity.user.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private String roles = "ROLE_USER";

    public static UserDto createCommonUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getRoles()
        );
    }

    public void setEncodePassword(BCryptPasswordEncoder bCryptPasswordEncoder) {
        password = bCryptPasswordEncoder.encode(password);
    }

    public boolean checkForm() {
        if (id == null || password == null) return false;
        else if (id.equals("") || password.equals("")) return false;
        return true;
    }
}
