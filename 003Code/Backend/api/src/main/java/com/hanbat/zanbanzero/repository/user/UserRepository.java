package com.hanbat.zanbanzero.repository.user;

import com.hanbat.zanbanzero.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByUsername(String username);

    User findByUsername(String username);

    @Query("select u from User u join fetch u.userPolicy where u.id = :id")
    User findByIdWithFetchPolicy(Long id);

    @Query("select u from User u join fetch u.userMypage where u.id = :id")
    User findByIdWithFetchMyPage(Long id);
}
