package com.hanbat.zanbanzero.dto.user.user;

import com.hanbat.zanbanzero.entity.user.User;

public record UserDto(
        Long id,
        String username,
        String password,
        String roles
) {
    public static UserDto from(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getRoles()
        );
    }
}
