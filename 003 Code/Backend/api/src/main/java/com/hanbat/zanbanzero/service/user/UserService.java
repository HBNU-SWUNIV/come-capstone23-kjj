package com.hanbat.zanbanzero.service.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanbat.zanbanzero.auth.login.userDetails.UserDetailsInterfaceImpl;
import com.hanbat.zanbanzero.dto.user.user.*;
import com.hanbat.zanbanzero.entity.user.user.User;
import com.hanbat.zanbanzero.dto.user.info.UserInfoDto;
import com.hanbat.zanbanzero.entity.user.user.UserMypage;
import com.hanbat.zanbanzero.entity.user.user.UserPolicy;
import com.hanbat.zanbanzero.exception.controller.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.controller.exceptions.JwtException;
import com.hanbat.zanbanzero.exception.controller.exceptions.WrongParameter;
import com.hanbat.zanbanzero.exception.controller.exceptions.WrongRequestDetails;
import com.hanbat.zanbanzero.repository.menu.MenuRepository;
import com.hanbat.zanbanzero.repository.user.UserMyPageRepository;
import com.hanbat.zanbanzero.repository.user.UserPolicyRepository;
import com.hanbat.zanbanzero.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMyPageRepository userMyPageRepository;
    private final UserPolicyRepository userPolicyRepository;

    private final MenuRepository menuRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void join(UserJoinDto dto) throws JsonProcessingException {
        dto.setEncodePassword(bCryptPasswordEncoder);
        User user = userRepository.save(User.of(dto));
        userMyPageRepository.save(UserMypage.createNewUserMyPage(user));
        userPolicyRepository.save(UserPolicy.createNewUserPolicy(user));
    }

    @Transactional
    public void withdraw(UserJoinDto dto) throws WrongRequestDetails {
        User user = userRepository.findByUsername(dto.getUsername());
        if (bCryptPasswordEncoder.matches(dto.getPassword(), user.getPassword())) {
            userRepository.delete(user);
        }
        else throw new WrongRequestDetails("비밀번호 틀림");
    }

    public boolean check(String username) {
        if (userRepository.existsByUsername(username)) return true;
        return false;
    }

    public UserInfoDto getInfo(String username) throws CantFindByIdException {
        User user = userRepository.findByUsername(username);
        if (user == null) throw new CantFindByIdException();

        return UserInfoDto.of(user);
    }

    public UserMypageDto getMyPage(Long id) throws CantFindByIdException, JsonProcessingException {
        UserMypage userMyPage = userMyPageRepository.getMyPage(id).orElseThrow(CantFindByIdException::new);

        return UserMypageDto.createUserMyPageDto(userMyPage);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (!user.getRoles().equals("ROLE_USER")) throw new UsernameNotFoundException("UserService - loadUserByUsername() : 잘못된 유저 닉네임");
        return new UserDetailsInterfaceImpl(user);
    }

    @Transactional
    public void setUserDatePolicy(UserPolicyDto dto, Long id) throws CantFindByIdException {
        UserPolicy policy = userPolicyRepository.findById(id).orElseThrow(CantFindByIdException::new);
        policy.setPolicy(dto);
    }

    @Transactional
    public void setUserMenuPolicy(Long userId, Long menuId) throws CantFindByIdException, WrongParameter {
        if (!menuRepository.existsById(menuId)) throw new WrongParameter("잘못된 메뉴 ID");

        UserPolicy policy = userPolicyRepository.findById(userId).orElseThrow(CantFindByIdException::new);
        policy.setDefaultMenu(menuId);
    }

    public UserPolicyDto getUserPolicy(Long id) throws CantFindByIdException {
        UserPolicy policy = userPolicyRepository.findById(id).orElseThrow(CantFindByIdException::new);
        return UserPolicyDto.createUserPolicyDto(policy);
    }

    public UserInfoDto getInfoForUsername(String username) {
        return UserInfoDto.of(userRepository.findByUsername(username));
    }
}
