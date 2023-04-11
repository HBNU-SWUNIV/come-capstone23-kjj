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
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Manager manager;

    private String name;
    private String info;

    public static Store createStore(Long id, Manager manager, StoreDto dto) {
        return new Store(
                id,
                manager,
                dto.getName(),
                dto.getInfo()
        );
    }
}
