package com.hanbat.zanbanzero.service.user;

import com.hanbat.zanbanzero.entity.user.user.User;
import com.hanbat.zanbanzero.dto.user.info.UserInfoDto;
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
        final User user = new User(null, null, null, null, username, "1234", "ROLE_USER", null);

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

        UserInfoDto expected = new UserInfoDto(user.getId(), user.getUsername(), null);
        UserInfoDto result = UserInfoDto.of(user);

        assertEquals(expected.toString(), result.toString());
    }

}