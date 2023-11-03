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
    private final String serverAddress;
    private final Logger logger = LoggerFactory.getLogger("errorLogger");

    public IpCheckFilter(String prometheusPath, String serverAddress) {
        this.prometheusPath = prometheusPath;
        this.serverAddress = serverAddress;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        if (servletRequest.getRequestURI().equals(prometheusPath)) {
            if (!check(servletRequest)) {
                logger.error("[WARN] Someone approaches Prometheus Matrix. IP = {}", request.getRemoteAddr());
                ((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN);
            }
            else chain.doFilter(request, response);
        }
        else chain.doFilter(request, response);
    }

    public boolean check(HttpServletRequest request) {
        IpAddressMatcher matcher = new IpAddressMatcher(serverAddress);
        return matcher.matches(request);
    }
}
