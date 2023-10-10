package com.hanbat.zanbanzero.dto.user.user;

import com.hanbat.zanbanzero.entity.user.user.UserMypage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMypageDto {
    private Long userId;
    private int point;

    public static UserMypageDto createUserMyPageDto(UserMypage userMyPage) {
        return new UserMypageDto(
                userMyPage.getId(),
                userMyPage.getPoint()
        );
    }
}
