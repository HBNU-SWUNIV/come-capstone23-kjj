package com.hanbat.zanbanzero.auth.monitor;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.util.matcher.IpAddressMatcher;

import java.io.IOException;

public class IpCheckFilter implements Filter {

    private final String prometheusPath;
    private final String[] allowAddress;
    private final Logger logger = LoggerFactory.getLogger("errorLogger");

    public IpCheckFilter(String prometheusPath, String[] allowAddress) {
        this.prometheusPath = prometheusPath;
        this.allowAddress = allowAddress;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        if (servletRequest.getRequestURI().equals(prometheusPath)) {
            if (!check(allowAddress)) {
                logger.error("[WARN] Someone approaches Prometheus Matrix. IP = {}", request.getRemoteAddr());
                ((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN);
            }
            else chain.doFilter(request, response);
        }
        else chain.doFilter(request, response);
    }

    public boolean check(String[] allowAddress) {
        for (String address : allowAddress) {
            IpAddressMatcher matcher = new IpAddressMatcher(address);
            if (matcher.matches(address)) return true;
        }
        return false;
    }
}
