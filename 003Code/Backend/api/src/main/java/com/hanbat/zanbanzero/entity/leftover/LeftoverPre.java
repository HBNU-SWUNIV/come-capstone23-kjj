package com.hanbat.zanbanzero.entity.leftover;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(indexes ={
        @jakarta.persistence.Index(name = "leftover_pre_date_index", columnList = "date")
})
public class LeftoverPre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private double predict;
}
