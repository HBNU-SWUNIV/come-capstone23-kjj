package com.hanbat.zanbanzero.service.user;

import com.hanbat.zanbanzero.entity.user.user.User;
import com.hanbat.zanbanzero.dto.user.info.UserInfoDto;
import com.hanbat.zanbanzero.dto.user.user.UserDto;
import com.hanbat.zanbanzero.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    UserRepository userRepository;

    private String username = "test";
    @BeforeEach
    void setup() {
        final User user = new User(null, username, "1234", "ROLE_USER");

        userRepository.save(user);
    }

    @Test
    void existsByUsername() {
        Boolean result = userRepository.existsByUsername(username);

        assertEquals(true, result);
    }

    @Test
    void getInfo() {
        User user = userRepository.findByUsername(username);

        UserInfoDto expected = new UserInfoDto(user.getId(), user.getUsername());
        UserInfoDto result = UserInfoDto.createUserInfoDto(UserDto.createCommonUserDto(user));

        assertEquals(expected.toString(), result.toString());
    }

}