package com.hanbat.zanbanzero.service.user.sso;

import com.hanbat.zanbanzero.dto.user.info.UserInfoDto;
import com.hanbat.zanbanzero.dto.user.user.UserJoinDto;
import com.hanbat.zanbanzero.entity.user.User;
import com.hanbat.zanbanzero.entity.user.UserMypage;
import com.hanbat.zanbanzero.entity.user.UserPolicy;
import com.hanbat.zanbanzero.exception.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.exceptions.CantFindByUsernameException;
import com.hanbat.zanbanzero.exception.exceptions.KeycloakJoinException;
import com.hanbat.zanbanzero.exception.exceptions.KeycloakWithdrawException;
import com.hanbat.zanbanzero.repository.user.UserMyPageRepository;
import com.hanbat.zanbanzero.repository.user.UserPolicyRepository;
import com.hanbat.zanbanzero.repository.user.UserRepository;
import com.hanbat.zanbanzero.service.user.user.UserService;
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
        user.setUserPolicy(userPolicyRepository.save(UserPolicy.createNewUserPolicy()));
        user.setUserMypage(userMypageRepository.save(UserMypage.createNewUserMyPage()));

        return userRepository.save(user);
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
    public UserInfoDto login(Long id) throws CantFindByIdException {
        User user = userRepository.findById(id).orElseThrow(() -> new CantFindByIdException("""
            해당 id의 유저를 찾을 수 없습니다.
            Filter에서 진행된 가입 절차에 문제가 발생했을 가능성이 있습니다.
            id : """, id));
        UserInfoDto userInfoDto = UserInfoDto.from(user);
        user.updateLoginDate();
        return userInfoDto;
    }

    @Override
    public void withdrawSso(String username) throws CantFindByUsernameException{
        String originUsername = username.replace("_keycloak", "");

        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();
        try (Response response = usersResource.delete(originUsername)) {
            if (response.getStatus() == 204) userRepository.delete(userRepository.findByUsername(username).orElseThrow(() -> new CantFindByUsernameException("""
                    해당 username을 가진 유저 데이터를 찾을 수 없습니다.
                    username : """, username)));
            else throw new KeycloakWithdrawException("username = " + username);
        }
    }
}
