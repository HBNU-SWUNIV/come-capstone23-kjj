package com.batch.batch.pojo;

import lombok.Data;

@Data
public class UserPolicy {
    private boolean monday;
    private boolean tuesday;
    private boolean wednesday;
    private boolean thursday;
    private boolean friday;
    private Long defaultMenu;
    private Long userId;

    public UserPolicy(String defaultMenu, String userId) {
        this.defaultMenu = Long.valueOf(defaultMenu);
        this.userId = Long.valueOf(userId);
    }
}
