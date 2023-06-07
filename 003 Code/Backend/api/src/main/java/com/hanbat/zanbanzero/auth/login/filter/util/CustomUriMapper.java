package com.hanbat.zanbanzero.auth.login.filter.util;

import com.hanbat.zanbanzero.exception.controller.exceptions.WrongParameter;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;

public class CustomUriMapper {
    private CreateTokenInterface createTokenInterface;

    private String loginEndPath = "/login";

    public CustomUriMapper(ServletRequest request) throws WrongParameter {
        if (((HttpServletRequest) request).getRequestURI().endsWith(loginEndPath)) {
            createTokenInterface = new CreateUserTokenImpl();
        }
        else {
            throw new WrongParameter("잘못된 주소입니다.");
        }
    }

    public CreateTokenInterface getLoginFilter() {
        return createTokenInterface;
    }
}
