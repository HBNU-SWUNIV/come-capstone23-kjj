package com.hanbat.zanbanzero.dto.user.user;

import com.hanbat.zanbanzero.entity.user.User;

public record UserJoinDto(
        String username,
        String password
) {
    public static UserJoinDto from(User user) {
        return new UserJoinDto(
                user.getUsername(),
                user.getPassword()
        );
    }

    public boolean checkForm() {
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) return false;
        return true;
    }
}
