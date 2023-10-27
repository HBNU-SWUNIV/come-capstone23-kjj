package com.hanbat.zanbanzero.auto_init.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqMessage {
    private String role;
    private String content;
}
