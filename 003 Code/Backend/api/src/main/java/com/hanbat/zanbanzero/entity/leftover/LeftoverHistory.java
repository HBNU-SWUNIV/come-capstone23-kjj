package com.hanbat.zanbanzero.entity.leftover;

import com.hanbat.zanbanzero.dto.leftover.LeftoverHistoryDto;
import com.hanbat.zanbanzero.entity.store.Store;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LeftoverHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Store store;

    private String date;
    private double leftover;

    public static LeftoverHistory createLeftoverHistory(LeftoverHistoryDto dto, Store store) {
        return new LeftoverHistory(null, store, dto.getDate(), dto.getLeftover());
    }
}
