package com.tumi.haul.model.admin.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
@Component
public class IpWhiteListFilter extends OncePerRequestFilter {
private final List<String> whiteListedIps = List.of(
        "",
        ""
);
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
       String requestUri = request.getRequestURI();
        String clientIp = request.getRemoteAddr();
if(requestUri.startsWith("/admin") || requestUri.equals("/login")){


       if (!whiteListedIps.contains(clientIp)){
           response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
           return;
       }
}
       filterChain.doFilter(request, response);
    }
}
