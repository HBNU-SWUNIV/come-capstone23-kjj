package com.hanbat.zanbanzero.exception.handler.filter.custom;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanbat.zanbanzero.exception.exceptions.SetFilterErrorResponseException;
import com.hanbat.zanbanzero.exception.exceptions.WrongParameter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class SetFilterErrorResponse {
    private SetFilterErrorResponse() throws WrongParameter {
        throw new WrongParameter("SerFilterException can not init");
    }

    public static void setResponse(HttpServletRequest request, HttpServletResponse response, HttpStatus status, String message) throws SetFilterErrorResponseException {
        ObjectMapper objectMapper = new ObjectMapper();
        ExceptionTemplate exceptionTemplate = new ExceptionTemplate(new Date().toString(), message, request.getRequestURI(), status.value());
        String result;
        response.setStatus(status.value());

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            result = objectMapper.writeValueAsString(exceptionTemplate);
            PrintWriter printWriter = response.getWriter();
            printWriter.write(result);
        } catch (IOException | IllegalStateException ex) {
            throw new SetFilterErrorResponseException(ex);
        }
    }
}
