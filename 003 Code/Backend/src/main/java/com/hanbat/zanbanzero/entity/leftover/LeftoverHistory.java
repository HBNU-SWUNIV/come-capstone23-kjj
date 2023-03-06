package com.hanbat.zanbanzero.entity.leftover;

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
public class LeftoverHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String date;
    private Double leftover;

    public static LeftoverHistory createLeftoverHistory(LeftoverShow leftoverShow) {
        return new LeftoverHistory(null, leftoverShow.getUpdated(), leftoverShow.getLeftover());
    }
}
