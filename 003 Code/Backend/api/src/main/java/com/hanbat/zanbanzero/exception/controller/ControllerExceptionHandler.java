package com.hanbat.zanbanzero.exception.controller;

import com.hanbat.zanbanzero.exception.controller.exceptions.*;
import com.hanbat.zanbanzero.exception.filter.ExceptionTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.FileNotFoundException;
import java.util.Date;

@RestController
@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
     HttpStatus status;

    @ExceptionHandler(SameNameException.class)
    public final ResponseEntity<Object> sameName(Exception ex, WebRequest request){
        status = HttpStatus.CONFLICT;
        ExceptionTemplate exceptionResponse = new ExceptionTemplate(new Date().toString(), ex.getMessage(), ((ServletWebRequest)request).getRequest().getRequestURI(), status.value());
        return new ResponseEntity<>(exceptionResponse, status);
    }

    @ExceptionHandler(JwtException.class)
    public final ResponseEntity<Object> jwt(Exception ex, WebRequest request){
        status = HttpStatus.FORBIDDEN;
        ExceptionTemplate exceptionResponse = new ExceptionTemplate(new Date().toString(), ex.getMessage(), ((ServletWebRequest)request).getRequest().getRequestURI(), status.value());
        return new ResponseEntity<>(exceptionResponse, status);
    }

    @ExceptionHandler(CantFindByIdException.class)
    public final ResponseEntity<Object> cantFindById(Exception ex, WebRequest request){
        status = HttpStatus.NOT_FOUND;
        ExceptionTemplate exceptionResponse = new ExceptionTemplate(new Date().toString(), "잘못된 id 입니다.", ((ServletWebRequest)request).getRequest().getRequestURI(), status.value());
        return new ResponseEntity<>(exceptionResponse, status);
    }

    @ExceptionHandler(WrongRequestDetails.class)
    public final ResponseEntity<Object> wrongRequestDetails(Exception ex, WebRequest request){
        status = HttpStatus.BAD_REQUEST;
        ExceptionTemplate exceptionResponse = new ExceptionTemplate(new Date().toString(), ex.getMessage(), ((ServletWebRequest)request).getRequest().getRequestURI(), status.value());
        return new ResponseEntity<>(exceptionResponse, status);
    }

    @ExceptionHandler(WrongParameter.class)
    public final ResponseEntity<Object> wrongParameter(Exception ex, WebRequest request){
        status = HttpStatus.BAD_REQUEST;
        ExceptionTemplate exceptionResponse = new ExceptionTemplate(new Date().toString(), ex.getMessage(), ((ServletWebRequest)request).getRequest().getRequestURI(), status.value());
        return new ResponseEntity<>(exceptionResponse, status);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public final ResponseEntity<Object> maxUploadSize(Exception ex, WebRequest request) {
        status = HttpStatus.BAD_REQUEST;
        ExceptionTemplate exceptionResponse = new ExceptionTemplate(new Date().toString(), "파일 크기가 너무 큽니다.", ((ServletWebRequest)request).getRequest().getRequestURI(), status.value());
        return new ResponseEntity<>(exceptionResponse, status);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public final ResponseEntity<Object> fileNotFound(Exception ex, WebRequest request) {
        status = HttpStatus.BAD_REQUEST;
        ExceptionTemplate exceptionResponse = new ExceptionTemplate(new Date().toString(), "파일을 찾을 수 없습니다.", ((ServletWebRequest)request).getRequest().getRequestURI(), status.value());
        return new ResponseEntity<>(exceptionResponse, status);
    }
}