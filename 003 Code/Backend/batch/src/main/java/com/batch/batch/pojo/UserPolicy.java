package com.batch.batch.pojo;

import lombok.Data;

@Data
public class UserPolicy {
    private boolean monday;
    private boolean tuesday;
    private boolean wednesday;
    private boolean thursday;
    private boolean friday;
    private Long default_menu;
    private Long user_id;

    public UserPolicy(String defaultMenu, String userId) {
        default_menu = Long.valueOf(defaultMenu);
        user_id = Long.valueOf(userId);
    }
}
