package com.hanbat.zanbanzero.entity.store;

import com.hanbat.zanbanzero.dto.store.StoreDto;
import com.hanbat.zanbanzero.entity.user.manager.Manager;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Manager manager;

    private Long lat;
    private Long lon;

    public void setLocation(Long lat, Long lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public static Store createStore(StoreDto dto, Manager manager) {
        return new Store(
                dto.getId(),
                manager,
                dto.getLat(),
                dto.getLon()
        );
    }
}
