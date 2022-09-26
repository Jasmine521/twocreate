package com.example.twocreate.support;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

public abstract class AbstractFilter extends LoggerSupport implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("init filter: {}...", getClass().getName());
    }

    @Override
    public void destroy() {
        logger.info("destroy filter: {}...", getClass().getName());
    }
}
