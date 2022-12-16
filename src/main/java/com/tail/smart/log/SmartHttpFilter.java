package com.tail.smart.log;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class SmartHttpFilter implements Filter {

    protected SmartLogConfig config;

    public SmartHttpFilter(SmartLogConfig config) {
        this.config = config;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String value = request.getHeader(config.getHeader());
        MDC.put(config.getHeader(), value);
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
