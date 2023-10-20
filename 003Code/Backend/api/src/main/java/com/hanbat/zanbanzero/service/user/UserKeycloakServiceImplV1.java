package com.hanbat.zanbanzero.service.user;

import com.hanbat.zanbanzero.dto.user.info.UserInfoDto;
import com.hanbat.zanbanzero.dto.user.user.UserJoinDto;
import com.hanbat.zanbanzero.entity.user.User;
import com.hanbat.zanbanzero.entity.user.UserMypage;
import com.hanbat.zanbanzero.entity.user.UserPolicy;
import com.hanbat.zanbanzero.exception.exceptions.KeycloakJoinException;
import com.hanbat.zanbanzero.exception.exceptions.KeycloakWithdrawException;
import com.hanbat.zanbanzero.repository.user.UserMyPageRepository;
import com.hanbat.zanbanzero.repository.user.UserPolicyRepository;
import com.hanbat.zanbanzero.repository.user.UserRepository;
import com.hanbat.zanbanzero.service.user.service.UserService;
import com.hanbat.zanbanzero.service.user.service.UserSsoService;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserKeycloakServiceImplV1 implements UserSsoService {

    @Value("${keycloak.realm}") private String realm;

    private final UserService userService;
    private final UserRepository userRepository;
    private final UserMyPageRepository userMypageRepository;
    private final UserPolicyRepository userPolicyRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final Keycloak keycloak;

    @Override
    @Transactional
    public User join(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        userMypageRepository.save(UserMypage.createNewUserMyPage(savedUser));
        userPolicyRepository.save(UserPolicy.createNewUserPolicy(savedUser));
        return savedUser;
    }

    @Override
    public boolean existsByUsername(String userName) {
        List<UserRepresentation> search = keycloak.realm(realm).users()
                .search(userName);
        return !search.isEmpty();
    }

    @Override
    @Transactional
    public void joinSso(UserJoinDto dto) {
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
                userService.join(dto);
            }
            else throw new KeycloakJoinException("keycloak join error - code : " + response.getStatus());
        }
    }

    @Override
    @Transactional
    public UserInfoDto login(User u) {
        User user = userRepository.findByUsername(u.getUsername());
        UserInfoDto userInfoDto = UserInfoDto.of(user);
        user.updateLoginDate();
        return userInfoDto;
    }

    @Override
    public void withdrawSso(String username) {
        String originUsername = username.replace("_keycloak", "");

        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();
        try (Response response = usersResource.delete(originUsername)) {
            if (response.getStatus() == 204) userRepository.delete(userRepository.findByUsername(username));
            else throw new KeycloakWithdrawException("username = " + username);
        }
    }
}
