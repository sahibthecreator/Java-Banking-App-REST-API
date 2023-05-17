package com.bank.app.restapi.config;

import java.io.IOException;
import java.util.logging.Logger;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

@Component
@Order(1)
public class LargeRequestFilter implements Filter {
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        int size = request.getContentLength();
        logger.info("Request size: " + size);
        if (size > 1000) {
            logger.severe("request with size " + size + " was rejected");
            throw new ServletException("Request too large");
        } else {
            chain.doFilter(request, response); 
        }
    }
}
