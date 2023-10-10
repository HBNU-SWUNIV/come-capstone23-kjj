package com.hanbat.zanbanzero.service.user.service;

import com.hanbat.zanbanzero.dto.user.info.ManagerInfoDto;

public interface ManagerService {
    ManagerInfoDto getInfoForUsername(String username);

    ManagerInfoDto getInfo(String username);
}
