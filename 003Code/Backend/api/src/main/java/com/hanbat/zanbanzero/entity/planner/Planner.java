package com.hanbat.zanbanzero.entity.planner;

import com.hanbat.zanbanzero.dto.planner.PlannerDto;
import com.hanbat.zanbanzero.entity.store.Store;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(indexes ={
        @jakarta.persistence.Index(name = "planner_date_index", columnList = "date")
})
public class Planner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Store store;

    private LocalDate date;
    private String menus;

    public static Planner of(Store store, PlannerDto dto, LocalDate date){
        return new Planner(
                null,
                store,
                date,
                dto.menus()
        );
    }

    public void setMenus(String menus) {
        this.menus = menus;
    }
}
