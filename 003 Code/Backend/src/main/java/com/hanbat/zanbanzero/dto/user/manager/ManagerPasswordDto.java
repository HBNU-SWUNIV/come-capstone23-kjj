package com.hanbat.zanbanzero.dto.user.manager;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManagerPasswordDto {
    private String oldPass;
    private String newPass;
}
