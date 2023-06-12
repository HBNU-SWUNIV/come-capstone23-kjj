package com.hanbat.zanbanzero.entity.store;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Index;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StoreState {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Store store;

    @Index(name = "store_state_date_index")
    private String date;
    private Boolean off;

    public void setOff(Boolean off) {
        this.off = off;
    }

    public static StoreState createNewOffStoreState(Store store, String date) {
        return new StoreState(
                null,
                store,
                date,
                false
        );
    }
}
