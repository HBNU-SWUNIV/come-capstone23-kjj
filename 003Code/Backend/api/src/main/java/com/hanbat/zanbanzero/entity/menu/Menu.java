package com.hanbat.zanbanzero.entity.menu;

import com.hanbat.zanbanzero.dto.menu.MenuUpdateDto;
import com.hanbat.zanbanzero.entity.store.Store;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(indexes ={
        @jakarta.persistence.Index(name = "menu_name_index", columnList = "name")
})
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Store store;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private MenuInfo menuInfo;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private MenuFood menuFood;

    private String name;
    private int cost;
    private String image;
    private Boolean sold;
    private boolean usePlanner;

    public void patch(MenuUpdateDto dto) {
        if (dto.name() != null) {
            this.name = dto.name();
        }
        if (dto.cost() != null) {
            this.cost = dto.cost();
        }
        if (dto.usePlanner() != null) {
            this.usePlanner = dto.usePlanner();
        }
    }

    public static Menu of(Store store, MenuUpdateDto dto, MenuInfo menuInfo, String filePath) {
        return new Menu(
                null,
                store,
                menuInfo,
                null,
                dto.name(),
                dto.cost(),
                filePath,
                true,
                dto.usePlanner()
        );
    }

    public void setImage(String path) { image = path; }
    public void setSoldTrue() {
        sold = true;
    }
    public void setSoldFalse() {
        sold = false;
    }
    public void setMenuFood(MenuFood food) {
        menuFood = food;
    }
    public void clearMenuFood() {
        menuFood = null;
    }
    public void usePlanner() {
        usePlanner = true;
    }
    public void notUsePlanner() {
        usePlanner = false;
    }
}
