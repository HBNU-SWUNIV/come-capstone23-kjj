package com.hanbat.zanbanzero.entity.user.user;

import com.hanbat.zanbanzero.dto.user.user.UserPolicyDto;
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
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    private boolean monday;
    private boolean tuesday;
    private boolean wednesday;
    private boolean thursday;
    private boolean friday;

    private Long defaultMenu;

    public void setPolicy(UserPolicyDto dto) {
        monday = dto.isMonday();
        tuesday = dto.isTuesday();
        wednesday = dto.isWednesday();
        thursday = dto.isThursday();
        friday = dto.isFriday();
    }

    public void setDefaultMenu(Long id) {
        defaultMenu = id;
    }
}
