package com.hanbat.zanbanzero.entity.batch.calculate;

import com.hanbat.zanbanzero.entity.batch.BatchDate;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CalculatePreWeek {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private BatchDate batchDate;
    private int monday;
    private int tuesday;
    private int wednesday;
    private int thursday;
    private int friday;
}
