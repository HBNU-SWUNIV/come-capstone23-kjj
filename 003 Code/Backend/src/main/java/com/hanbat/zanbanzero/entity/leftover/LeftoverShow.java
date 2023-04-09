package com.hanbat.zanbanzero.entity.leftover;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LeftoverShow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double leftover;
    private Timestamp updated;

    public void update(Double leftover) {
        updated = new Timestamp(System.currentTimeMillis());
        this.leftover = leftover;
    }

}
