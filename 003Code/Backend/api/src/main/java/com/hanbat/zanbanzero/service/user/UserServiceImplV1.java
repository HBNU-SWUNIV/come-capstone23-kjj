package com.hanbat.zanbanzero.service.user;

import com.hanbat.zanbanzero.auth.jwt.JwtUtil;
import com.hanbat.zanbanzero.auth.login.userDetails.UserDetailsInterface;
import com.hanbat.zanbanzero.auth.login.userDetails.UserDetailsInterfaceImpl;
import com.hanbat.zanbanzero.dto.user.WithdrawDto;
import com.hanbat.zanbanzero.dto.user.info.UserInfoDto;
import com.hanbat.zanbanzero.dto.user.user.UsePointDto;
import com.hanbat.zanbanzero.dto.user.user.UserJoinDto;
import com.hanbat.zanbanzero.dto.user.user.UserMypageDto;
import com.hanbat.zanbanzero.dto.user.user.UserPolicyDto;
import com.hanbat.zanbanzero.entity.user.User;
import com.hanbat.zanbanzero.entity.user.UserMypage;
import com.hanbat.zanbanzero.entity.user.UserPolicy;
import com.hanbat.zanbanzero.exception.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.exceptions.WrongParameter;
import com.hanbat.zanbanzero.exception.exceptions.WrongRequestDetails;
import com.hanbat.zanbanzero.repository.menu.MenuRepository;
import com.hanbat.zanbanzero.repository.user.UserMyPageRepository;
import com.hanbat.zanbanzero.repository.user.UserPolicyRepository;
import com.hanbat.zanbanzero.repository.user.UserRepository;
import com.hanbat.zanbanzero.service.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImplV1 implements UserService {


    private final UserRepository userRepository;
    private final UserMyPageRepository userMypageRepository;
    private final UserPolicyRepository userPolicyRepository;

    private final MenuRepository menuRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public void join(UserJoinDto dto) {
        dto.setEncodePassword(bCryptPasswordEncoder);
        User user = userRepository.save(User.of(dto));
        userMypageRepository.save(UserMypage.createNewUserMyPage(user));
        userPolicyRepository.save(UserPolicy.createNewUserPolicy(user));
    }

    @Override
    @Transactional
    public void withdraw(String username, WithdrawDto dto) throws WrongRequestDetails, CantFindByIdException {
        User user = userRepository.findByUsername(username);
        if (bCryptPasswordEncoder.matches(dto.getPassword(), user.getPassword())) userRepository.delete(userRepository.findById(user.getId()).orElseThrow(() -> new CantFindByIdException("userId : " + user.getId())));
        else throw new WrongRequestDetails("error");
    }

    @Override
    public boolean check(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public UserInfoDto getInfo(String username) throws CantFindByIdException {
        User user = userRepository.findByUsername(username);
        if (user == null) throw new CantFindByIdException(username);

        return UserInfoDto.of(user);
    }

    @Override
    public UserMypageDto getMyPage(String username) throws CantFindByIdException {
        Long id = userRepository.findByUsername(username).getId();
        UserMypage userMypage = userMypageRepository.findById(id).orElseThrow(() -> new CantFindByIdException("id : " + id));

        return UserMypageDto.createUserMyPageDto(userMypage);
    }

    @Override
    @Transactional
    public Integer usePoint(Long id, UsePointDto dto) throws CantFindByIdException {
        UserMypage userMypage = userMypageRepository.findById(id).orElseThrow(() -> new CantFindByIdException("id : " + id));
        userMypage.updatePoint(-1 * dto.getValue());

        return userMypage.getPoint();
    }

    @Override
    public UserDetailsInterface loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        return new UserDetailsInterfaceImpl(user);
    }

    @Override
    @Transactional
    public UserPolicyDto setUserDatePolicy(UserPolicyDto dto, String username) throws CantFindByIdException {
        Long id = userRepository.findByUsername(username).getId();
        UserPolicy policy = userPolicyRepository.findById(id).orElseThrow(() -> new CantFindByIdException("id : " + id));
        policy.setPolicy(dto);

        return UserPolicyDto.of(policy);
    }

    @Override
    @Transactional
    public UserPolicyDto setUserMenuPolicy(String username, Long menuId) throws CantFindByIdException, WrongParameter {
        if (!menuRepository.existsById(menuId)) throw new WrongParameter("menuId : " + menuId);

        Long id = userRepository.findByUsername(username).getId();
        UserPolicy policy = userPolicyRepository.findById(id).orElseThrow(() -> new CantFindByIdException("id : " + id));
        policy.setDefaultMenu(menuId);

        return UserPolicyDto.of(policy);
    }

    @Override
    public UserPolicyDto getUserPolicy(String username) throws CantFindByIdException {
        Long id = userRepository.findByUsername(username).getId();
        UserPolicy policy = userPolicyRepository.findById(id).orElseThrow(() -> new CantFindByIdException("id : " + id));
        return UserPolicyDto.of(policy);
    }

    @Override
    @Transactional
    public UserInfoDto getInfoForUsername(String username) {
        User user = userRepository.findByUsername(username);
        UserInfoDto userInfoDto = UserInfoDto.of(user);
        user.setLoginDate(LocalDate.now());
        return userInfoDto;
    }

    public Map<String, String> testToken(String username) {
        if (username == null) username = "user";
        return Map.of("accessToken", jwtUtil.createToken(loadUserByUsername(username)));
    }
}