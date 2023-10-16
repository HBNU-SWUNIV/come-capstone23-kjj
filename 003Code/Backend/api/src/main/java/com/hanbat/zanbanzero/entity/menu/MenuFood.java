package com.hanbat.zanbanzero.entity.menu;

import com.hanbat.zanbanzero.auto_init.dto.Recipe;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    private String name;

    private String food;

    public void setFood(String food) {
        this.food = food;
    }

    public static MenuFood of(String name, String food) {
        return new MenuFood(
                null,
                name,
                food
        );
    }

    public static MenuFood of(Recipe recipe) {
        return new MenuFood(
                null,
                recipe.getName(),
                recipe.getFood()
        );
    }
}