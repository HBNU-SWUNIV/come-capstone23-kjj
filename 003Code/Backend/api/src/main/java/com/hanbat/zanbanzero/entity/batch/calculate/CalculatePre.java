package com.hanbat.zanbanzero.entity.batch.calculate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CalculatePre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private Calculate calculate;

    private Integer predictUser;
    @Column(columnDefinition = "TEXT")
    private String predictFood;
    private String predictMenu;
}
