package com.hanbat.zanbanzero.entity.menu;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MenuFood {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Menu menu;

    private String food;

    public void setFood(String food) {
        this.food = food;
    }

    public static MenuFood of(Menu menu, String food) {
        return new MenuFood(
                null,
                menu,
                food
        );
    }
}
