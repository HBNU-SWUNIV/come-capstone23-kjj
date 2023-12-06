package com.hanbat.zanbanzero.dto.user.info;

import com.hanbat.zanbanzero.entity.user.User;

public record UserInfoDto(
        Long id,
        String username,
        String loginDate
) {
    public static UserInfoDto from(User user) {
        return new UserInfoDto(
                user.getId(),
                user.getUsername(),
                user.getLoginDate()
        );
    }
}
