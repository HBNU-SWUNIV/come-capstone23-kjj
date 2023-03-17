package com.hanbat.zanbanzero.auth.login.filter.util;

import com.hanbat.zanbanzero.exception.controller.exceptions.WrongParameter;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;

public class CustomUriMapper {
    private LoginFilterInterface loginFilterInterface;

    public CustomUriMapper(ServletRequest request) {
        switch (((HttpServletRequest) request).getRequestURI()) {
            case "/login/user":
                loginFilterInterface = new CreateUserToken();
                break;
            case "/login/manager":
                loginFilterInterface = new CreateManagerToken();
                break;
            default:
                throw new WrongParameter("잘못된 주소입니다.");
        }
    }

    public LoginFilterInterface getLoginFilter() {
        return loginFilterInterface;
    }
}
