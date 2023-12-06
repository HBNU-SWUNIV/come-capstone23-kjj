package com.hanbat.zanbanzero.dto.user.user;

import com.hanbat.zanbanzero.entity.user.UserMypage;

public record UserMypageDto(
        Long userId,
        int point
) {
    public static UserMypageDto from(UserMypage userMyPage) {
        return new UserMypageDto(
                userMyPage.getId(),
                userMyPage.getPoint()
        );
    }
}
