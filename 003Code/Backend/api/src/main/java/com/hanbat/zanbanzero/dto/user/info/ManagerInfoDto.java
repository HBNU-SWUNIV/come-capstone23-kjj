package com.hanbat.zanbanzero.dto.user.info;

import com.hanbat.zanbanzero.entity.user.User;

public record ManagerInfoDto(
        Long id,
        String loginId
) {
    public static ManagerInfoDto from(User manager) {
        return new ManagerInfoDto(
                manager.getId(),
                manager.getUsername()
        );
    }
}