package com.hanbat.zanbanzero.service.user;

import com.hanbat.zanbanzero.auth.jwt.JwtUtil;
import com.hanbat.zanbanzero.auth.login.userDetails.UserDetailsInterfaceImpl;
import com.hanbat.zanbanzero.dto.user.WithdrawDto;
import com.hanbat.zanbanzero.dto.user.info.UserInfoDto;
import com.hanbat.zanbanzero.dto.user.user.UsePointDto;
import com.hanbat.zanbanzero.dto.user.user.UserJoinDto;
import com.hanbat.zanbanzero.dto.user.user.UserMypageDto;
import com.hanbat.zanbanzero.dto.user.user.UserPolicyDto;
import com.hanbat.zanbanzero.entity.user.user.User;
import com.hanbat.zanbanzero.entity.user.user.UserMypage;
import com.hanbat.zanbanzero.entity.user.user.UserPolicy;
import com.hanbat.zanbanzero.exception.exceptions.*;
import com.hanbat.zanbanzero.repository.menu.MenuRepository;
import com.hanbat.zanbanzero.repository.user.UserMyPageRepository;
import com.hanbat.zanbanzero.repository.user.UserPolicyRepository;
import com.hanbat.zanbanzero.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    @Value("${keycloak.realm}")
    private String realm;

    private final UserRepository userRepository;
    private final UserMyPageRepository userMypageRepository;
    private final UserPolicyRepository userPolicyRepository;

    private final MenuRepository menuRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtil jwtUtil;
    private final Keycloak keycloak;

    @Transactional
    public void join(UserJoinDto dto) {
        dto.setEncodePassword(bCryptPasswordEncoder);
        User user = userRepository.save(User.of(dto));
        userMypageRepository.save(UserMypage.createNewUserMyPage(user));
        userPolicyRepository.save(UserPolicy.createNewUserPolicy(user));
    }

    @Transactional
    public User join(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        userMypageRepository.save(UserMypage.createNewUserMyPage(savedUser));
        userPolicyRepository.save(UserPolicy.createNewUserPolicy(savedUser));
        return savedUser;
    }

    public boolean existsByUsernameFromKeycloak(String userName) {
        List<UserRepresentation> search = keycloak.realm(realm).users()
                .search(userName);
        return !search.isEmpty();
    }

    @Transactional
    public void joinKeycloak(UserJoinDto dto) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setEnabled(true);
        userRepresentation.setUsername(dto.getUsername());

        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();

        try (Response response = usersResource.create(userRepresentation)) {
            if (response.getStatus() == 201) {
                String userId = CreatedResponseUtil.getCreatedId(response);
                CredentialRepresentation passwordCred = new CredentialRepresentation();
                passwordCred.setTemporary(false);
                passwordCred.setType(CredentialRepresentation.PASSWORD);
                passwordCred.setValue(dto.getPassword());
                UserResource userResource = usersResource.get(userId);

                userResource.resetPassword(passwordCred);

                String roleName = "ROLE_USER";
                RoleRepresentation realmRoleRep = realmResource.roles().get(roleName).toRepresentation();
                userResource.roles().realmLevel().add(Arrays.asList(realmRoleRep));

                dto.setUsername(userId + "_keycloak");
                join(dto);
            }
            else throw new KeycloakJoinException("keycloak join error - code : " + response.getStatus());
        }
    }
    @Transactional
    public UserInfoDto loginFromKeycloak(User u) {
        User user = userRepository.findByUsername(u.getUsername());
        UserInfoDto userInfoDto = UserInfoDto.of(user);
        user.setLoginDate(LocalDate.now());
        return userInfoDto;
    }

    @Transactional
    public void withdraw(String username, WithdrawDto dto) throws WrongRequestDetails, CantFindByIdException {
        User user = userRepository.findByUsername(username);
        if (bCryptPasswordEncoder.matches(dto.getPassword(), user.getPassword())) userRepository.delete(userRepository.findById(user.getId()).orElseThrow(() -> new CantFindByIdException("userId : " + user.getId())));
        else throw new WrongRequestDetails("error");
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

    @Transactional
    public Integer usePoint(Long id, UsePointDto dto) throws CantFindByIdException {
        UserMypage userMypage = userMypageRepository.findById(id).orElseThrow(() -> new CantFindByIdException("id : " + id));
        userMypage.updatePoint(-1 * dto.getValue());

        return userMypage.getPoint();
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

    public void withdrawKeycloak(String username) {
        String originUsername = username.replace("_keycloak", "");

        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();
        try (Response response = usersResource.delete(originUsername)) {
            if (response.getStatus() == 204) userRepository.delete(userRepository.findByUsername(username));
            else throw new KeycloakWithdrawException("username = " + username);
        }
    }
}