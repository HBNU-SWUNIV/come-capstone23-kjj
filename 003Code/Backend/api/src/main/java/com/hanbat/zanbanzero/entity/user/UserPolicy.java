package com.hanbat.zanbanzero.entity.user;

import com.hanbat.zanbanzero.dto.user.user.UserDatePolicyDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean monday;
    private boolean tuesday;
    private boolean wednesday;
    private boolean thursday;
    private boolean friday;

    private Long defaultMenu;

    public static UserPolicy createNewUserPolicy() {
        return new UserPolicy(
                null,
                false,
                false,
                false,
                false,
                false,
                null
        );
    }

    public void setPolicy(UserDatePolicyDto dto) {
        monday = dto.isMonday();
        tuesday = dto.isTuesday();
        wednesday = dto.isWednesday();
        thursday = dto.isThursday();
        friday = dto.isFriday();
    }

    public void setDefaultMenu(Long id) {
        defaultMenu = id;
    }
    public void clearDefaultMenu() {
        defaultMenu = null;
    }
}
