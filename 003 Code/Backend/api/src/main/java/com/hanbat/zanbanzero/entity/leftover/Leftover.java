package com.hanbat.zanbanzero.entity.leftover;

import com.hanbat.zanbanzero.dto.leftover.LeftoverDto;
import com.hanbat.zanbanzero.entity.store.Store;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Leftover {

    @Id
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leftover_pre_id")
    private LeftoverPre leftoverPre;

    private double leftover;

    public static Leftover createLeftover(LeftoverPre leftoverPre, LeftoverDto dto) {
        return new Leftover(null, leftoverPre, dto.getLeftover());
    }
}
