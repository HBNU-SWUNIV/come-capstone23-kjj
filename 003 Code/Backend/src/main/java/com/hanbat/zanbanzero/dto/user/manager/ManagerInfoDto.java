package com.hanbat.zanbanzero.dto.user.manager;

import com.hanbat.zanbanzero.entity.user.manager.Manager;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManagerInfoDto {
    private Long id;
    private String username;

    public static ManagerInfoDto createManagerInfoDto(Manager manager) {
        return new ManagerInfoDto(
                manager.getId(),
                manager.getUsername()
        );
    }
}
