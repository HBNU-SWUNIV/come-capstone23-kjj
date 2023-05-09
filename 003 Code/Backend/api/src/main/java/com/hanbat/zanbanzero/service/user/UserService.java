package com.hanbat.zanbanzero.service.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanbat.zanbanzero.auth.login.userDetails.UserDetailsInterfaceImpl;
import com.hanbat.zanbanzero.dto.user.user.UserMypageDto;
import com.hanbat.zanbanzero.dto.user.user.UserPolicyDto;
import com.hanbat.zanbanzero.entity.user.user.User;
import com.hanbat.zanbanzero.dto.user.info.UserInfoDto;
import com.hanbat.zanbanzero.dto.user.user.UserDto;
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

    private boolean checkForm(UserDto dto) {
        if (dto.getId() == null || dto.getPassword() == null) {
            return false;
        }

        else if (dto.getId().equals("") || dto.getPassword().equals("")) {
            return false;
        }
        return true;
    }

    @Transactional
    public void join(UserDto dto) throws JsonProcessingException, WrongRequestDetails {
        if (checkForm(dto)) {
            throw new WrongRequestDetails("잘못된 정보입니다.");
        }

        dto.setEncodePassword(bCryptPasswordEncoder);

        User user = userRepository.save(User.createUser(dto));

        userMyPageRepository.save(UserMypage.createNewUserMyPage(user));
    }

    @Transactional
    public void withdraw(UserDto dto) throws CantFindByIdException, WrongRequestDetails {
        if (checkForm(dto)) {
            throw new WrongRequestDetails("잘못된 정보입니다.");
        }

        User user = userRepository.findByUsername(dto.getUsername());
        UserMypage userMyPage = userMyPageRepository.findById(user).orElseThrow(CantFindByIdException::new);

        userMyPageRepository.delete(userMyPage);
        userRepository.delete(user);
    }

    public boolean check(UserDto dto) {
        if (userRepository.existsByUsername(dto.getUsername()))
            return true;
        else return false;
    }

    public UserInfoDto getInfo(UserDto dto) throws JwtException {
        User user = userRepository.findByUsername(dto.getUsername());

        return new UserInfoDto(user.getId(), user.getUsername());
    }

    public UserMypageDto getMyPage(Long id) throws CantFindByIdException, JsonProcessingException {
        UserMypage userMyPage = userMyPageRepository.getMyPage(id).orElseThrow(CantFindByIdException::new);

        return UserMypageDto.createUserMyPageDto(userMyPage);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        return new UserDetailsInterfaceImpl(user);
    }

    @Transactional
    public void setUserDatePolicy(UserPolicyDto dto, Long id) throws CantFindByIdException {
        UserPolicy policy = userPolicyRepository.findById(id).orElseThrow(CantFindByIdException::new);
        policy.setPolicy(dto);
    }

    @Transactional
    public void setUserMenuPolicy(Long userId, Long menuId) throws CantFindByIdException, WrongParameter {
        if (!menuRepository.existsById(menuId)) {
            throw new WrongParameter("잘못된 메뉴 ID");
        }
        UserPolicy policy = userPolicyRepository.findById(userId).orElseThrow(CantFindByIdException::new);
        policy.setDefaultMenu(menuId);
    }

    public UserPolicyDto getUserPolicy(Long id) throws CantFindByIdException {
        UserPolicy policy = userPolicyRepository.findById(id).orElseThrow(CantFindByIdException::new);
        return UserPolicyDto.createUserPolicyDto(policy);
    }
}
