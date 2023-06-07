package com.hanbat.zanbanzero.entity.store;

import com.hanbat.zanbanzero.dto.store.StoreDto;
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

    private String name;
    private String info;

    public static Store of(Long id, StoreDto dto) {
        return new Store(
                id,
                dto.getName(),
                dto.getInfo()
        );
    }

    public void setInfo(StoreDto dto) {
        info = dto.getInfo();
    }
    public void setName(StoreDto dto) { name = dto.getName(); }
}
