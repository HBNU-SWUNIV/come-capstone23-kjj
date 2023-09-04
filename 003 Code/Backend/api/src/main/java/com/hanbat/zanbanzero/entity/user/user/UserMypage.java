package com.hanbat.zanbanzero.entity.user.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserMypage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private User user;

    private int point;

    public static UserMypage createNewUserMyPage(User user) {
        return new UserMypage(
                user.getId(),
                user,
                0
        );
    }
}
