package com.hanbat.zanbanzero.entity.leftover;

import com.hanbat.zanbanzero.dto.leftover.LeftoverDto;
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
    private LeftoverPre leftoverPre;

    private double data;

    public static Leftover of(LeftoverPre leftoverPre, LeftoverDto dto) {
        return new Leftover(null, leftoverPre, dto.getLeftover());
    }

    public void setLeftover(double leftover) {
        this.data = leftover;
    }
}
