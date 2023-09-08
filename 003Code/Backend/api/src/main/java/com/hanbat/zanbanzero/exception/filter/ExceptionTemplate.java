package com.hanbat.zanbanzero.exception.filter;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExceptionTemplate {
    private String date;
    private String message;
    private String uri;
    private int status;
}
