package com.hanbat.zanbanzero.service.user.user;

import com.hanbat.zanbanzero.auth.login.userDetails.UserDetailsInterface;
import com.hanbat.zanbanzero.dto.user.auth.WithdrawDto;
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

    UserInfoDto getInfo(Long id) throws CantFindByIdException;

    UserMypageDto getMyPage(Long id) throws CantFindByIdException;

    Integer usePoint(Long id, UsePointDto dto) throws CantFindByIdException, WrongRequestDetails;

    UserPolicyDto setUserDatePolicy(UserDatePolicyDto dto, Long id) throws CantFindByIdException;

    UserPolicyDto setUserMenuPolicy(Long id, Long menuId) throws CantFindByIdException, WrongParameter;

    UserPolicyDto getUserPolicy(Long id) throws CantFindByIdException;

    UserInfoDto getInfoForUsername(String username);

    @Override
    UserDetailsInterface loadUserByUsername(String username) throws UsernameNotFoundException;
}
