package com.hanbat.zanbanzero.service.user.user;

import com.hanbat.zanbanzero.auth.login.user_details.UserDetailsInterface;
import com.hanbat.zanbanzero.auth.login.user_details.UserDetailsInterfaceImpl;
import com.hanbat.zanbanzero.dto.user.auth.WithdrawDto;
import com.hanbat.zanbanzero.dto.user.info.UserInfoDto;
import com.hanbat.zanbanzero.dto.user.user.*;
import com.hanbat.zanbanzero.entity.user.User;
import com.hanbat.zanbanzero.entity.user.UserMypage;
import com.hanbat.zanbanzero.entity.user.UserPolicy;
import com.hanbat.zanbanzero.exception.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.exceptions.CantFindByUsernameException;
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
        UserMypage userMypage = userMypageRepository.save(UserMypage.createNewUserMyPage());
        UserPolicy userPolicy = userPolicyRepository.save(UserPolicy.createNewUserPolicy());
        User user = User.of(dto, userPolicy, userMypage);
        user.setEncodePassword(bCryptPasswordEncoder);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void joinSso(UserJoinDto dto, String newUsername) {
        UserMypage userMypage = userMypageRepository.save(UserMypage.createNewUserMyPage());
        UserPolicy userPolicy = userPolicyRepository.save(UserPolicy.createNewUserPolicy());
        User user = User.of(dto, newUsername, userPolicy, userMypage);
        user.setEncodePassword(bCryptPasswordEncoder);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void withdraw(String username, WithdrawDto dto) throws WrongRequestDetails, CantFindByUsernameException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new CantFindByUsernameException("""
                해당 username을 가진 유저 데이터를 찾을 수 없습니다.
                username : """, username));
        if (bCryptPasswordEncoder.matches(dto.password(), user.getPassword())) userRepository.delete(user);
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
    public UserMypageDto getMyPage(Long id) throws CantFindByIdException {
        UserMypage mypage= userRepository.findByIdWithFetchMyPage(id).orElseThrow(() -> new CantFindByIdException("""
                해당 id를 가진 유저 데이터를 찾을 수 없습니다.
                id : """, id)).getUserMypage();
        return UserMypageDto.from(mypage);
    }

    @Override
    @Transactional
    public Integer usePoint(Long id, UsePointDto dto) throws WrongRequestDetails, CantFindByIdException {
        UserMypage mypage= userRepository.findByIdWithFetchMyPage(id).orElseThrow(() -> new CantFindByIdException("""
                해당 id를 가진 유저 데이터를 찾을 수 없습니다.
                id : """, id)).getUserMypage();
        int data = -1 * dto.value();
        if (mypage.getPoint() + data < 0) throw new WrongRequestDetails("""
            포인트가 부족합니다.
            포인트 사용 시 잔여 포인트가 음수가 됩니다.
            point : """ + mypage.getPoint());
        mypage.updatePoint(data);

        return mypage.getPoint();
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetailsInterface loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("""
                해당 username을 가진 유저 데이터를 찾을 수 없습니다.
                username : """ + username));
        return new UserDetailsInterfaceImpl(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User findByUsername(String username) throws CantFindByUsernameException {
        return userRepository.findByUsername(username).orElseThrow(() -> new CantFindByUsernameException("""
                해당 username을 가진 유저 데이터를 찾을 수 없습니다.
                username : """, username));
    }

    @Override
    @Transactional
    public UserPolicyDto setUserDatePolicy(UserDatePolicyDto dto, Long id) throws CantFindByIdException {
        User user = userRepository.findByIdWithFetchPolicy(id).orElseThrow(() -> new CantFindByIdException("""
                해당 id를 가진 유저 데이터를 찾을 수 없습니다.
                id : """, id));
        UserPolicy userPolicy = user.getUserPolicy();
        userPolicy.setPolicy(dto);

        return UserPolicyDto.from(userPolicy);
    }

    @Override
    @Transactional
    public UserPolicyDto setUserMenuPolicy(Long id, Long menuId) throws WrongParameter, CantFindByIdException {
        if (!menuRepository.existsById(menuId)) throw new WrongParameter("""
                menuId를 가진 메뉴 데이터가 존재하지 않습니다.
                menuId : """ + menuId);

        UserPolicy policy = userRepository.findByIdWithFetchPolicy(id).orElseThrow(() -> new CantFindByIdException("""
                해당 id를 가진 유저 데이터를 찾을 수 없습니다.
                id : """, id)).getUserPolicy();
        policy.setDefaultMenu(menuId);

        return UserPolicyDto.from(policy);
    }

    @Override
    @Transactional(readOnly = true)
    public UserPolicyDto getUserPolicy(Long id) throws CantFindByIdException {
        UserPolicy policy = userRepository.findByIdWithFetchPolicy(id).orElseThrow(() -> new CantFindByIdException("""
                해당 id를 가진 유저 데이터를 찾을 수 없습니다.
                id : """, id)).getUserPolicy();
        return UserPolicyDto.from(policy);
    }

    @Override
    @Transactional
    public UserInfoDto getInfoForUsername(String username) throws CantFindByUsernameException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new CantFindByUsernameException("""
                해당 username을 가진 유저를 찾을 수 없습니다.
                username : """, username));
        UserInfoDto userInfoDto = UserInfoDto.from(user);
        user.updateLoginDate();
        return userInfoDto;
    }
}