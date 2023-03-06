package com.hanbat.zanbanzero.entity.menu;

import com.hanbat.zanbanzero.dto.menu.MenuDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer cost;
    private String info;
    private String image;

    public static Menu createMenu(MenuDto dto) {
        return new Menu(dto.getId(),
        dto.getName(),
        dto.getCost(),
        dto.getInfo(),
        dto.getImage());
    };

    public void patch(MenuDto dto) {
        if (dto.getName() != null) {
            this.name = dto.getName();
        }
        if (dto.getCost() != null) {
            this.cost = dto.getCost();
        }
        if (dto.getInfo() != null) {
            this.info = dto.getInfo();
        }
        if (dto.getImage() != null) {
            this.image = dto.getImage();
        }
    }
}
