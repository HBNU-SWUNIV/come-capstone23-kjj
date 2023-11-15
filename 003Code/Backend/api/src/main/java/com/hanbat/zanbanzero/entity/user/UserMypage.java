package com.hanbat.zanbanzero.entity.user;

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
    private int point;

    public void updatePoint(int value) {
        point += value;
    }

    public static UserMypage createNewUserMyPage() {
        return new UserMypage(
                null,
                0
        );
    }
}
