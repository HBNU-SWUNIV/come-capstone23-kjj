package com.hanbat.zanbanzero.auth.login.filter.util;

import com.hanbat.zanbanzero.exception.controller.exceptions.WrongParameter;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;

public class CustomUriMapper {
    private CreateTokenInterface createTokenInterface;

    public CustomUriMapper(ServletRequest request) {
        switch (((HttpServletRequest) request).getRequestURI()) {
            case "/login/user":
                createTokenInterface = new CreateUserTokenImpl();
                break;
            case "/login/manager":
                createTokenInterface = new CreateManagerTokenImpl();
                break;
            default:
                throw new WrongParameter("잘못된 주소입니다.");
        }
    }

    public CreateTokenInterface getLoginFilter() {
        return createTokenInterface;
    }
}
