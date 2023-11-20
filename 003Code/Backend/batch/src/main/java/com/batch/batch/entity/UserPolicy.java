package com.batch.batch.entity;

import lombok.Data;

@Data
public class UserPolicy {
    private boolean monday;
    private boolean tuesday;
    private boolean wednesday;
    private boolean thursday;
    private boolean friday;
    private Long defaultMenu;
    private Long id;

    public UserPolicy(String defaultMenu, String id) {
        this.defaultMenu = Long.valueOf(defaultMenu);
        this.id = Long.valueOf(id);
    }
}
