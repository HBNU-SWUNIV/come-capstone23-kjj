package com.hanbat.zanbanzero.entity.store;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class StoreState {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String date;
    private Long congestion;
    private int today;

    public void setCongestion(Long congestion) {
        this.congestion = congestion;
    }
}
