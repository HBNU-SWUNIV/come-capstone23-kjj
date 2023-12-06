package com.hanbat.zanbanzero.dto.user.user;

import com.hanbat.zanbanzero.entity.user.UserPolicy;

public record UserPolicyDto(
        boolean monday,
        boolean tuesday,
        boolean wednesday,
        boolean thursday,
        boolean friday,
        Long defaultMenu
) {

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
