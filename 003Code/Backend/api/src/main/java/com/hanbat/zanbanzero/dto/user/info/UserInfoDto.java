package com.hanbat.zanbanzero.dto.user.info;

import com.hanbat.zanbanzero.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto {
    private Long id;
    private String username;
    private String loginDate;

    public static UserInfoDto from(User user) {
        return new UserInfoDto(
                user.getId(),
                user.getUsername(),
                user.getLoginDate()
        );
    }
}
