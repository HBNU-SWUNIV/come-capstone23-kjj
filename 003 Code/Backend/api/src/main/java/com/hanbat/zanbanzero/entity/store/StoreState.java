package com.hanbat.zanbanzero.entity.store;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Index;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private Boolean off;

    public void setOff(Boolean off) {
        this.off = off;
    }

    public static StoreState createNewOffStoreState(Store store, LocalDate date) {
        return new StoreState(
                null,
                store,
                date,
                false
        );
    }
}
