package com.hanbat.zanbanzero.entity.planner;

import com.hanbat.zanbanzero.dto.planner.PlannerDto;
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

    @Column(name = "date")
    @Index(name = "planner_date_index")
    private String date;
    private String menus;
    private boolean off;

    public static Planner createPlanner(PlannerDto dto){
        return new Planner(
                null,
                dto.getDate(),
                dto.getMenus(),
                dto.isOff()
        );
    }

    public void setMenus(String menus) {
        this.menus = menus;
    }
    public void setOff(boolean off) { this.off = off; }
}
