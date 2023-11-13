package com.hanbat.zanbanzero.repository.user;

import com.hanbat.zanbanzero.entity.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class UserRepositoryTest {

    @Autowired private UserRepository userRepository;

    private String username = "test";
    private final User user = new User(1L, null, null, null, username, "1234", "ROLE_USER", null);

    @BeforeEach
    void setup() {
        userRepository.save(user);
    }

    @Test
    void findByUsername() {
        User result = userRepository.findByUsername(username);
        assertEquals(username, result.getUsername());
    }

    @Test
    void existsByUsername() {
        assertTrue(userRepository.existsByUsername(username));
    }
}