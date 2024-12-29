package com.onlinecode.itnhaque.config;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class TokenCleanupFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestUri = request.getRequestURI();

        // Kiểm tra nếu request URI là /auth/login
        if ("/api/v1/auth/login".equals(requestUri)) {
            String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

            // Nếu có Bearer Token, xử lý nó
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                System.out.println("authorizationHeader111: " + authorizationHeader);
                // Loại bỏ Bearer token nếu cần (hoặc làm gì đó với token)
                // Đặt lại attribute nếu cần, hoặc không làm gì
                request.setAttribute("authorizationHeader", null); // Đặt null hoặc thực hiện hành động cần thiết
            }
        }

        // Tiếp tục chuỗi lọc
        filterChain.doFilter(request, response);
    }
}
