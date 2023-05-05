package com.hanbat.zanbanzero.entity.planner;

import com.hanbat.zanbanzero.dto.planner.PlannerDto;
import com.hanbat.zanbanzero.entity.menu.Menu;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Index;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Planner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    @Index(name = "planner_date_index")
    private String date;
    private String menus;

    public static Planner createPlanner(PlannerDto dto, Menu menu){
        return new Planner(
                null,
                menu,
                dto.getDate(),
                dto.getMenus()
        );
    }

    public void setMenus(String menus) {
        this.menus = menus;
    }
}
