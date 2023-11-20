package com.batch.batch.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class Order {
    private Long userId;
    private int cost;
    private Long menu;
    private String orderDate;
    private boolean recognize;
}
