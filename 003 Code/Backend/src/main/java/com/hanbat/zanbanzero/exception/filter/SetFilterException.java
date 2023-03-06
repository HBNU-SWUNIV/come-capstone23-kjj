package com.hanbat.zanbanzero.exception.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class SetFilterException {

    public static void setResponse(HttpServletRequest request, HttpServletResponse response, HttpStatus status, String message) {
        ObjectMapper objectMapper = new ObjectMapper();
        ExceptionTemplate exceptionTemplate = new ExceptionTemplate(new Date().toString(), message, request.getRequestURI(), status.value());
        String result;
        response.setStatus(status.value());

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            result = objectMapper.writeValueAsString(exceptionTemplate);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
        try {
            PrintWriter printWriter = response.getWriter();
            printWriter.write(result);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalStateException e) {
        }
    }
}
