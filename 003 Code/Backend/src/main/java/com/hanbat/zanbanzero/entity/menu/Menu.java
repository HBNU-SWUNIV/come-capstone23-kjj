package com.hanbat.zanbanzero.entity.menu;

import com.hanbat.zanbanzero.dto.menu.MenuDto;
import com.hanbat.zanbanzero.dto.menu.MenuUpdateDto;
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
    private String image;
    private Boolean sold;

    public static Menu createMenu(MenuDto dto) {
        return new Menu(
                dto.getId(),
                dto.getName(),
                dto.getCost(),
                dto.getImage(),
                dto.getSold()
        );
    };

    public void patch(MenuUpdateDto dto) {
        if (dto.getName() != null) {
            this.name = dto.getName();
        }
        if (dto.getCost() != null) {
            this.cost = dto.getCost();
        }
    }

    public void setSold(boolean type) {
        sold = type;
    }
}
