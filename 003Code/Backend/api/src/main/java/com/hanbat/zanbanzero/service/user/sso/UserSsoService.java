package com.hanbat.zanbanzero.service.user.sso;

import com.hanbat.zanbanzero.dto.user.info.UserInfoDto;
import com.hanbat.zanbanzero.dto.user.user.UserJoinDto;
import com.hanbat.zanbanzero.entity.user.User;
import com.hanbat.zanbanzero.exception.exceptions.CantFindByIdException;
import org.springframework.transaction.annotation.Transactional;

public interface UserSsoService {
    @Transactional
    User join(User user);

    boolean existsByUsername(String userName);

    @Transactional
    void joinSso(UserJoinDto dto);

    @Transactional
    UserInfoDto login(Long id) throws CantFindByIdException;

    void withdrawSso(String username);
}
