package com.hanbat.zanbanzero.dto.user.info;

import com.hanbat.zanbanzero.entity.user.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserInfoDto {
    private Long id;
    private String username;
    private String loginDate;

    public static UserInfoDto of(User user) {
        return new UserInfoDto(
                user.getId(),
                user.getUsername(),
                user.getLoginDate()
        );
    }
}
