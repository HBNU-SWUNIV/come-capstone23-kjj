package com.hanbat.zanbanzero.entity.leftover;

import com.hanbat.zanbanzero.entity.calculate.Calculate;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class LeftoverPre {

    @Id
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    private Calculate calculate;

    private double predict;
}
