package com.hanbat.zanbanzero.repository.user;

import com.hanbat.zanbanzero.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.userPolicy WHERE u.id = :id")
    Optional<User> findByIdWithFetchPolicy(Long id);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.userMypage WHERE u.id = :id")
    Optional<User> findByIdWithFetchMyPage(Long id);
}
