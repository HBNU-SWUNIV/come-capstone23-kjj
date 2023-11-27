package com.hanbat.zanbanzero.entity.store;

import com.hanbat.zanbanzero.dto.store.StoreOffDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Index;

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
    private LocalDate date;
    private Boolean off;
    private String name;

    public void setOff(StoreOffDto dto) {
        off = dto.isOff();
        name = dto.getName();
    }

    public static StoreState createNewOffStoreState(Store store, LocalDate date, StoreOffDto dto) {
        return new StoreState(
                null,
                store,
                date,
                dto.isOff(),
                dto.getName()
        );
    }
}
