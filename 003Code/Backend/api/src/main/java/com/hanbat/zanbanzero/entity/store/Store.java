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
    public static final long FINAL_STORE_ID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String info;
    private String image;

    public static Store of(Long id, StoreDto dto) {
        return new Store(
                id,
                dto.name(),
                dto.info(),
                dto.image()
        );
    }

    public static Store of(Long id, StoreSettingDto dto) {
        return new Store(
                id,
                dto.name(),
                dto.info(),
                null
        );
    }

    public void setInfo(String info) {
        this.info = info;
    }
    public void setName(String name) { this.name = name; }
    public void setImage(String path) { image = path; }
}
