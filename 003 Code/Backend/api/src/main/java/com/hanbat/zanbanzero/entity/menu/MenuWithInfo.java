package com.hanbat.zanbanzero.entity.menu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuWithInfo {
    private Long id;
    private String name;
    private int cost;
    private String image;
    private Boolean sold;
    private boolean usePlanner;

    private String info;
    private String details;
}
