package com.hanbat.zanbanzero.auto_init.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqBody {
    private String model;
    private boolean stream;
    private List<ReqMessage> messages;
}
