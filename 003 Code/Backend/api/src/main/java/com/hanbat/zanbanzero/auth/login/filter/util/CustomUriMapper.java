package com.hanbat.zanbanzero.auth.login.filter.util;

import com.hanbat.zanbanzero.exception.controller.exceptions.WrongParameter;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;

public class CustomUriMapper {
    private CreateTokenInterface createTokenInterface;

    public CustomUriMapper(ServletRequest request) throws WrongParameter {
        if (((HttpServletRequest) request).getRequestURI().startsWith("/api/login/")) {
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
