package com.hanbat.zanbanzero.dto.user.user;

import com.hanbat.zanbanzero.entity.user.UserPolicy;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserPolicyDto {
    private boolean monday;
    private boolean tuesday;
    private boolean wednesday;
    private boolean thursday;
    private boolean friday;

    private Long defaultMenu;

    public static UserPolicyDto from(UserPolicy policy) {
        return new UserPolicyDto(
                policy.isMonday(),
                policy.isTuesday(),
                policy.isWednesday(),
                policy.isThursday(),
                policy.isFriday(),
                policy.getDefaultMenu()
        );
    }
}
