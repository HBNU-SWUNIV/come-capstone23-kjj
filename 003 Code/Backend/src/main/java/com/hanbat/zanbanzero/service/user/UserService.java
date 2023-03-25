package com.hanbat.zanbanzero.service.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanbat.zanbanzero.dto.user.user.UserMyPageDto;
import com.hanbat.zanbanzero.entity.user.user.User;
import com.hanbat.zanbanzero.dto.user.info.UserInfoDto;
import com.hanbat.zanbanzero.dto.user.user.UserDto;
import com.hanbat.zanbanzero.entity.user.user.UserMyPage;
import com.hanbat.zanbanzero.exception.controller.exceptions.CantFindByIdException;
import com.hanbat.zanbanzero.exception.controller.exceptions.JwtException;
import com.hanbat.zanbanzero.repository.user.UserMyPageRepository;
import com.hanbat.zanbanzero.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMyPageRepository userMyPageRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void join(UserDto dto) throws JsonProcessingException {
        dto.setEncodePassword(bCryptPasswordEncoder);

        User user = userRepository.save(User.createUser(dto));

        userMyPageRepository.save(UserMyPage.createNewUserMyPage(user));
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

    public UserMyPageDto getMyPage(Long id) throws CantFindByIdException, JsonProcessingException {
        UserMyPage userMyPage = userMyPageRepository.getMyPage(id).orElseThrow(CantFindByIdException::new);

        return UserMyPageDto.createUserMyPageDto(userMyPage);
    }

}
