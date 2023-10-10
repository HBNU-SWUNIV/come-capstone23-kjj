package com.hanbat.zanbanzero.auth.login.filter.util;

import com.hanbat.zanbanzero.exception.exceptions.WrongParameter;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;

public class CustomUriMapperV1 {
    private final CreateTokenInterface createTokenInterface;

    public CustomUriMapperV1(ServletRequest request) throws WrongParameter {
        String loginEndPath = "/login/id";
        if (((HttpServletRequest) request).getRequestURI().endsWith(loginEndPath)) {
            createTokenInterface = new CreateTokenInterfaceUserImpl();
        }
        else {
            throw new WrongParameter("잘못된 주소입니다.");
        }
    }

    public CreateTokenInterface getLoginFilter() {
        return createTokenInterface;
    }
}
