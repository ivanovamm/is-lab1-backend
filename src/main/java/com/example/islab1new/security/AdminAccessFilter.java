package com.example.islab1new.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AdminAccessFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String role = (String) httpRequest.getSession().getAttribute("userRole");
        String token = (String) httpRequest.getSession().getAttribute("authToken");

        if (token == null || role == null || !role.equals("ADMIN")) {
            httpResponse.sendRedirect("/unauthorized");
            return;
        }

        chain.doFilter(request, response);
    }
}

