package com.hanbat.zanbanzero.entity.calculate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Calculate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = "calculate")
    List<CalculateMenu> calculateMenus;

    private LocalDate date;
    private int today;
    private int sales;

    public static Calculate createZeroCalculateData() {
        return new Calculate(
                null,
                null,
                LocalDate.now(),
                0,
                0
        );
    }
}
