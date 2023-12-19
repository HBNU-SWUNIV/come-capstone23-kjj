package com.hanbat.zanbanzero.service.user.manager;

import com.hanbat.zanbanzero.auth.login.user_details.UserDetailsInterface;
import com.hanbat.zanbanzero.dto.user.info.ManagerInfoDto;
import com.hanbat.zanbanzero.exception.exceptions.CantFindByUsernameException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface ManagerService extends UserDetailsService {
    ManagerInfoDto getInfoForUsername(String username) throws CantFindByUsernameException;

    ManagerInfoDto getInfo(String username) throws CantFindByUsernameException;

    @Override
    UserDetailsInterface loadUserByUsername(String username) throws UsernameNotFoundException;
}
