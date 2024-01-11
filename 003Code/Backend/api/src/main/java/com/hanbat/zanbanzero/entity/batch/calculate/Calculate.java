package com.hanbat.zanbanzero.entity.batch.calculate;

import com.hanbat.zanbanzero.entity.batch.BatchDate;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private List<CalculateMenu> calculateMenus;

    @OneToOne(fetch = FetchType.LAZY)
    private BatchDate batchDate;

    private int today;
    private int sales;

    public static Calculate createZeroCalculateData() {
        return new Calculate(
                null,
                null,
                null,
                0,
                0
        );
    }
}
