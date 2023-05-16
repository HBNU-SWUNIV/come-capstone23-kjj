package com.hanbat.zanbanzero.entity.menu;

import com.hanbat.zanbanzero.dto.menu.MenuUpdateDto;
import com.hanbat.zanbanzero.entity.planner.Planner;
import com.hanbat.zanbanzero.entity.store.Store;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
    private List<Planner> planners;

    @OneToOne(mappedBy = "menu", cascade = CascadeType.ALL)
    private MenuInfo menuInfos;

    private String name;
    private int cost;
    private String image;
    private Boolean sold;
    private boolean usePlanner;

    public void patch(MenuUpdateDto dto) {
        if (dto.getName() != null) {
            this.name = dto.getName();
        }
        if (dto.getCost() != null) {
            this.cost = dto.getCost();
        }
        if (dto.getUsePlanner() != null) {
            this.usePlanner = dto.getUsePlanner();
        }
    }

    public static Menu createMenu(MenuUpdateDto dto, String filePath) {
        return new Menu(
                null,
                null,
                null,
                dto.getName(),
                dto.getCost(),
                filePath,
                true,
                dto.getUsePlanner()
        );
    }

    public void setImage(String path) { image = path; }
    public void setSold(boolean type) {
        sold = type;
    }
    public void setUsePlanner(Boolean usePlanner) {
        this.usePlanner = usePlanner;
    }
}
