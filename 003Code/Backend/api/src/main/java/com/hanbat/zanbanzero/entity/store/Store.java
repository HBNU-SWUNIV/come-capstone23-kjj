package com.hanbat.zanbanzero.entity.store;

import com.hanbat.zanbanzero.dto.store.StoreDto;
import com.hanbat.zanbanzero.dto.store.StoreSettingDto;
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
    private String image;

    public static Store of(Long id, StoreDto dto) {
        return new Store(
                id,
                dto.getName(),
                dto.getInfo(),
                dto.getImage()
        );
    }

    public static Store of(Long id, StoreSettingDto dto) {
        return new Store(
                id,
                dto.getName(),
                dto.getInfo(),
                null
        );
    }

    public void setInfo(String info) {
        this.info = info;
    }
    public void setName(String name) { this.name = name; }
    public void setImage(String path) { image = path; }
}
