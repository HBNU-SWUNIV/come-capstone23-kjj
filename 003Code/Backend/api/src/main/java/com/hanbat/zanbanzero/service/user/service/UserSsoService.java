package com.hanbat.zanbanzero.service.user.service;

import com.hanbat.zanbanzero.dto.user.info.UserInfoDto;
import com.hanbat.zanbanzero.dto.user.user.UserJoinDto;
import com.hanbat.zanbanzero.entity.user.User;
import org.springframework.transaction.annotation.Transactional;

public interface UserSsoService {
    @Transactional
    User join(User user);

    boolean existsByUsername(String userName);

    @Transactional
    void joinSso(UserJoinDto dto);

    @Transactional
    UserInfoDto login(User u);

    void withdrawSso(String username);
}
