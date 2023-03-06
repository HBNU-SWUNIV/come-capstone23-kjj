package com.hanbat.zanbanzero.entity.leftover;

import com.hanbat.zanbanzero.entity.store.Store;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LeftoverShow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Store store;

    private Double leftover;
    private String updated;

    public void update(Double leftover) {
        updated = new Date().toString();
        this.leftover = leftover;
    }

}
