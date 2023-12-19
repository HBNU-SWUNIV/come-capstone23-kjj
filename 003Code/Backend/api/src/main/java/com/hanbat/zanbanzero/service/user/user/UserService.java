package com.hanbat.zanbanzero.service.user.user;

import com.hanbat.zanbanzero.auth.login.user_details.UserDetailsInterface;
import com.hanbat.zanbanzero.dto.user.auth.WithdrawDto;
import com.hanbat.zanbanzero.dto.user.info.UserInfoDto;
import com.hanbat.zanbanzero.dto.user.user.*;
import com.hanbat.zanbanzero.entity.user.User;
import com.hanbat.zanbanzero.exception.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.exceptions.CantFindByUsernameException;
import com.hanbat.zanbanzero.exception.exceptions.WrongParameter;
import com.hanbat.zanbanzero.exception.exceptions.WrongRequestDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService extends UserDetailsService {
    void join(UserJoinDto dto);

    void withdraw(String username, WithdrawDto dto) throws WrongRequestDetails, CantFindByIdException, CantFindByUsernameException;

    boolean check(String username);

    UserInfoDto getInfo(Long id) throws CantFindByIdException;

    UserMypageDto getMyPage(Long id) throws CantFindByIdException;

    Integer usePoint(Long id, UsePointDto dto) throws CantFindByIdException, WrongRequestDetails;

    UserPolicyDto setUserDatePolicy(UserDatePolicyDto dto, Long id) throws CantFindByIdException;

    UserPolicyDto setUserMenuPolicy(Long id, Long menuId) throws CantFindByIdException, WrongParameter;

    UserPolicyDto getUserPolicy(Long id) throws CantFindByIdException;

    UserInfoDto getInfoForUsername(String username) throws CantFindByUsernameException;

    @Override
    UserDetailsInterface loadUserByUsername(String username) throws UsernameNotFoundException;

    User findByUsername(String username) throws CantFindByUsernameException;

    void joinSso(UserJoinDto dto, String newUsername);
}
