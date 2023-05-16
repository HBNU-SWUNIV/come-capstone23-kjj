package com.hanbat.zanbanzero.dto.user.info;

import com.hanbat.zanbanzero.entity.user.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManagerInfoDto {
    private Long id;
    private String loginId;

    public static ManagerInfoDto createManagerInfoDto(User manager) {
        return new ManagerInfoDto(
                manager.getId(),
                manager.getUsername()
        );
    }
}
