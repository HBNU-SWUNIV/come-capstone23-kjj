package com.hanbat.zanbanzero.entity.menu;

import com.hanbat.zanbanzero.auto_init.dto.Recipe;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MenuFood {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "menuFood")
    private List<Menu> menu;

    private String name;

    private String food;

    public static MenuFood of(String name, String food) {
        return new MenuFood(
                null,
                null,
                name,
                food
        );
    }

    public static MenuFood from(Recipe recipe) {
        return new MenuFood(
                null,
                null,
                recipe.getName(),
                recipe.getFood()
        );
    }
}