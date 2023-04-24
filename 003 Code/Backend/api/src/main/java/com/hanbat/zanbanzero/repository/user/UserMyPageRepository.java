package com.hanbat.zanbanzero.repository.user;

import com.hanbat.zanbanzero.entity.user.user.User;
import com.hanbat.zanbanzero.entity.user.user.UserMypage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserMyPageRepository extends JpaRepository<UserMypage, User> {

    @Query( value = "select * " +
            "from user_my_page " +
            "where users_id = :user_id",
    nativeQuery = true)
    Optional<UserMypage> getMyPage(@Param("user_id") Long id);

}
