package com.hanbat.zanbanzero.repository.user;

import com.hanbat.zanbanzero.entity.user.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

//    @Query(value =
//            "SELECT EXISTS(" +
//                    "SELECT * " +
//                    "FROM user " +
//                    "WHERE username = :username)",
//        nativeQuery = true)
    Boolean existsByUsername(String username);

    User findByUsername(@Param("username") String username);

}
