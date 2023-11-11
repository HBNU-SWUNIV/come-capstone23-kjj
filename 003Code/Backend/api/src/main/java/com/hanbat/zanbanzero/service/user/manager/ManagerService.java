package com.hanbat.zanbanzero.service.user.manager;

import com.hanbat.zanbanzero.auth.login.userDetails.UserDetailsInterface;
import com.hanbat.zanbanzero.dto.user.info.ManagerInfoDto;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface ManagerService extends UserDetailsService {
    ManagerInfoDto getInfoForUsername(String username);

    ManagerInfoDto getInfo(String username);

    @Override
    UserDetailsInterface loadUserByUsername(String username) throws UsernameNotFoundException;
}
