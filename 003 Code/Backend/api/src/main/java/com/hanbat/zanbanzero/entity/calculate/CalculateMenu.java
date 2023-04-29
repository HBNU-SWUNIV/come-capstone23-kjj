package com.hanbat.zanbanzero.entity.calculate;

import com.hanbat.zanbanzero.entity.menu.Menu;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class CalculateMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Calculate calculate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    private int count;
    private int sales;
}
