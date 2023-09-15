package com.hanbat.zanbanzero.service.user;

import com.hanbat.zanbanzero.auth.jwt.JwtUtil;
import com.hanbat.zanbanzero.auth.login.userDetails.UserDetailsInterfaceImpl;
import com.hanbat.zanbanzero.dto.user.WithdrawDto;
import com.hanbat.zanbanzero.dto.user.info.UserInfoDto;
import com.hanbat.zanbanzero.dto.user.user.UserJoinDto;
import com.hanbat.zanbanzero.dto.user.user.UserMypageDto;
import com.hanbat.zanbanzero.dto.user.user.UserPolicyDto;
import com.hanbat.zanbanzero.entity.user.user.User;
import com.hanbat.zanbanzero.entity.user.user.UserMypage;
import com.hanbat.zanbanzero.entity.user.user.UserPolicy;
import com.hanbat.zanbanzero.exception.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.exceptions.WrongParameter;
import com.hanbat.zanbanzero.exception.exceptions.WrongRequestDetails;
import com.hanbat.zanbanzero.repository.menu.MenuRepository;
import com.hanbat.zanbanzero.repository.user.UserMyPageRepository;
import com.hanbat.zanbanzero.repository.user.UserPolicyRepository;
import com.hanbat.zanbanzero.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    private final UserRepository userRepository;
    private final UserMyPageRepository userMypageRepository;
    private final UserPolicyRepository userPolicyRepository;

    private final MenuRepository menuRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtil jwtUtil;
    private final RabbitTemplate rabbitTemplate;

    @Transactional
    public User join(UserJoinDto dto) {
        dto.setEncodePassword(bCryptPasswordEncoder);
        User user = userRepository.save(User.of(dto));
        userMypageRepository.save(UserMypage.createNewUserMyPage(user));
        userPolicyRepository.save(UserPolicy.createNewUserPolicy(user));
        return user;
    }

    @Transactional
    public User join(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        userMypageRepository.save(UserMypage.createNewUserMyPage(savedUser));
        userPolicyRepository.save(UserPolicy.createNewUserPolicy(savedUser));
        return savedUser;
    }

    @Transactional
    public UserInfoDto loginFromKeycloak(User u) {
        return UserInfoDto.of(userRepository.findByUsername(u.getUsername()));
    }

    @Transactional
    public void withdraw(String username, WithdrawDto dto) throws WrongRequestDetails {
        User user = userRepository.findByUsername(username);
        if (bCryptPasswordEncoder.matches(dto.getPassword(), user.getPassword())) rabbitTemplate.convertAndSend(exchangeName, routingKey, user.getId());
        else throw new WrongRequestDetails("error");
    }

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void reciveMessage(Long userId) throws CantFindByIdException {
        userRepository.delete(userRepository.findById(userId).orElseThrow(() -> new CantFindByIdException("userId : " + userId)));
    }

    public boolean check(String username) {
        return userRepository.existsByUsername(username);
    }

    public UserInfoDto getInfo(String username) throws CantFindByIdException {
        User user = userRepository.findByUsername(username);
        if (user == null) throw new CantFindByIdException(username);

        return UserInfoDto.of(user);
    }

    public UserMypageDto getMyPage(String username) throws CantFindByIdException {
        Long id = userRepository.findByUsername(username).getId();
        UserMypage userMypage = userMypageRepository.findById(id).orElseThrow(() -> new CantFindByIdException("id : " + id));

        return UserMypageDto.createUserMyPageDto(userMypage);
    }

    @Override
    public UserDetailsInterfaceImpl loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        return new UserDetailsInterfaceImpl(user);
    }

    @Transactional
    public UserPolicyDto setUserDatePolicy(UserPolicyDto dto, String username) throws CantFindByIdException {
        Long id = userRepository.findByUsername(username).getId();
        UserPolicy policy = userPolicyRepository.findById(id).orElseThrow(() -> new CantFindByIdException("id : " + id));
        policy.setPolicy(dto);

        return UserPolicyDto.of(policy);
    }

    @Transactional
    public UserPolicyDto setUserMenuPolicy(String username, Long menuId) throws CantFindByIdException, WrongParameter {
        if (!menuRepository.existsById(menuId)) throw new WrongParameter("menuId : " + menuId);

        Long id = userRepository.findByUsername(username).getId();
        UserPolicy policy = userPolicyRepository.findById(id).orElseThrow(() -> new CantFindByIdException("id : " + id));
        policy.setDefaultMenu(menuId);

        return UserPolicyDto.of(policy);
    }

    public UserPolicyDto getUserPolicy(String username) throws CantFindByIdException {
        Long id = userRepository.findByUsername(username).getId();
        UserPolicy policy = userPolicyRepository.findById(id).orElseThrow(() -> new CantFindByIdException("id : " + id));
        return UserPolicyDto.of(policy);
    }

    public UserInfoDto getInfoForUsername(String username) {
        return UserInfoDto.of(userRepository.findByUsername(username));
    }

    public Map<String, String> testToken(String username) {
        if (username == null) username = "user";
        return Map.of("accessToken", jwtUtil.createToken(loadUserByUsername(username)));
    }
}
