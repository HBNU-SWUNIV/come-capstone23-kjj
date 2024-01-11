package com.hanbat.zanbanzero.entity.batch.predict;

import com.hanbat.zanbanzero.entity.batch.BatchDate;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WeeklyFoodPredict {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private BatchDate batchDate;

    private long entireMonday;
    private long entireTuesday;
    private long entireWednesday;
    private long entireThursday;
    private long entireFriday;

    private long monday;
    private long tuesday;
    private long wednesday;
    private long thursday;
    private long friday;
}