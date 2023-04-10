package com.hanbat.zanbanzero.exception.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionTemplate {
    private String date;
    private String message;
    private String uri;
    private int status;
}
