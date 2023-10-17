package com.hanbat.zanbanzero.service.user.service;

import com.hanbat.zanbanzero.auth.login.userDetails.UserDetailsInterface;
import com.hanbat.zanbanzero.dto.user.WithdrawDto;
import com.hanbat.zanbanzero.dto.user.info.UserInfoDto;
import com.hanbat.zanbanzero.dto.user.user.*;
import com.hanbat.zanbanzero.exception.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.exceptions.WrongParameter;
import com.hanbat.zanbanzero.exception.exceptions.WrongRequestDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService extends UserDetailsService {
    void join(UserJoinDto dto);

    void withdraw(String username, WithdrawDto dto) throws WrongRequestDetails, CantFindByIdException;

    boolean check(String username);

    UserInfoDto getInfo(String username) throws CantFindByIdException;

    UserMypageDto getMyPage(String username) throws CantFindByIdException;

    Integer usePoint(Long id, UsePointDto dto) throws CantFindByIdException, WrongRequestDetails;

    UserPolicyDto setUserDatePolicy(UserDatePolicyDto dto, Long id) throws CantFindByIdException;

    UserPolicyDto setUserMenuPolicy(Long id, Long menuId) throws CantFindByIdException, WrongParameter;

    UserPolicyDto getUserPolicy(String username) throws CantFindByIdException;

    UserInfoDto getInfoForUsername(String username);

    @Override
    UserDetailsInterface loadUserByUsername(String username) throws UsernameNotFoundException;
}
