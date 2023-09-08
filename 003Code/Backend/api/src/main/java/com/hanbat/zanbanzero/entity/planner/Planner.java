package com.hanbat.zanbanzero.entity.planner;

import com.hanbat.zanbanzero.dto.planner.PlannerDto;
import com.hanbat.zanbanzero.entity.menu.Menu;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Index;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private String menus;

    public static Planner of(PlannerDto dto, Menu menu){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return new Planner(
                null,
                menu,
                LocalDate.parse(dto.getDate(), formatter),
                dto.getMenus()
        );
    }

    public void setMenus(String menus) {
        this.menus = menus;
    }
}
