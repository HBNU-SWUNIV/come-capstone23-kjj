package com.hanbat.zanbanzero.entity.user.manager;

import com.hanbat.zanbanzero.dto.user.manager.ManagerDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String roles;

    public static Manager createManager(ManagerDto dto) {
        return new Manager(
                dto.getId(),
                dto.getUsername(),
                dto.getPassword(),
                dto.getRoles()
        );
    }
}
