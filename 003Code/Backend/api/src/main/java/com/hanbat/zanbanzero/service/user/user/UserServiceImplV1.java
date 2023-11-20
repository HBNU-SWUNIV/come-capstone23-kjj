package com.hanbat.zanbanzero.service.user.user;

import com.hanbat.zanbanzero.auth.login.userDetails.UserDetailsInterface;
import com.hanbat.zanbanzero.auth.login.userDetails.UserDetailsInterfaceImpl;
import com.hanbat.zanbanzero.dto.user.auth.WithdrawDto;
import com.hanbat.zanbanzero.dto.user.info.UserInfoDto;
import com.hanbat.zanbanzero.dto.user.user.*;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImplV1 implements UserService {


    private final UserRepository userRepository;
    private final UserMyPageRepository userMypageRepository;
    private final UserPolicyRepository userPolicyRepository;

    private final MenuRepository menuRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    @Transactional
    public void join(UserJoinDto dto) {
        dto.setEncodePassword(bCryptPasswordEncoder);
        UserMypage userMypage = userMypageRepository.save(UserMypage.createNewUserMyPage());
        UserPolicy userPolicy = userPolicyRepository.save(UserPolicy.createNewUserPolicy());
        userRepository.save(User.of(dto, userPolicy, userMypage));
    }

    @Override
    @Transactional
    public void withdraw(String username, WithdrawDto dto) throws WrongRequestDetails {
        User user = userRepository.findByUsername(username);
        if (bCryptPasswordEncoder.matches(dto.getPassword(), user.getPassword())) userRepository.delete(user);
        else throw new WrongRequestDetails("비밀번호가 맞지 않습니다.");
    }

    @Override
    @Transactional(readOnly = true)
    public boolean check(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public UserInfoDto getInfo(Long id) throws CantFindByIdException {
        User user = userRepository.findById(id).orElseThrow(() -> new CantFindByIdException("""
                해당 id를 가진 유저를 찾을 수 없습니다.
                id :""", id));
        return UserInfoDto.from(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserMypageDto getMyPage(Long id) {
        return UserMypageDto.from(userRepository.findByIdWithFetchMyPage(id).getUserMypage());
    }

    @Override
    @Transactional
    public Integer usePoint(Long id, UsePointDto dto) throws WrongRequestDetails {
        UserMypage userMypage = userRepository.findByIdWithFetchMyPage(id).getUserMypage();
        int data = -1 * dto.getValue();
        if (userMypage.getPoint() + data < 0) throw new WrongRequestDetails("""
            포인트가 부족합니다.
            포인트 사용 시 잔여 포인트가 음수가 됩니다.
            point : """ + userMypage.getPoint());
        userMypage.updatePoint(data);

        return userMypage.getPoint();
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetailsInterface loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        return new UserDetailsInterfaceImpl(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public UserPolicyDto setUserDatePolicy(UserDatePolicyDto dto, Long id) {
        User user = userRepository.findByIdWithFetchPolicy(id);
        UserPolicy userPolicy = user.getUserPolicy();
        userPolicy.setPolicy(dto);

        return UserPolicyDto.from(userPolicy);
    }

    @Override
    @Transactional
    public UserPolicyDto setUserMenuPolicy(Long id, Long menuId) throws WrongParameter {
        if (!menuRepository.existsById(menuId)) throw new WrongParameter("""
                menuId를 가진 메뉴 데이터가 존재하지 않습니다.
                menuId : """ + menuId);

        UserPolicy policy = userRepository.findByIdWithFetchPolicy(id).getUserPolicy();
        policy.setDefaultMenu(menuId);

        return UserPolicyDto.from(policy);
    }

    @Override
    @Transactional(readOnly = true)
    public UserPolicyDto getUserPolicy(Long id) {
        UserPolicy policy = userRepository.findByIdWithFetchPolicy(id).getUserPolicy();
        return UserPolicyDto.from(policy);
    }

    @Override
    @Transactional
    public UserInfoDto getInfoForUsername(String username) {
        User user = userRepository.findByUsername(username);
        UserInfoDto userInfoDto = UserInfoDto.from(user);
        user.updateLoginDate();
        return userInfoDto;
    }
}