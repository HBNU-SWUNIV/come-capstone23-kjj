package com.hanbat.zanbanzero.exception.handler.filter.template;

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
