package com.jd.warehouse.config;

import com.jd.controller.AuthController;
import com.jd.service.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("JwtInterceptor preHandle method called for path: {}", request.getRequestURI());
        String authHeader = request.getHeader("Authorization");
        logger.info("Auth header: {}", authHeader);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (jwtUtil.validateToken(token)) {
                String username = jwtUtil.getUsernameFromToken(token);
                List<String> roles = jwtUtil.getRolesFromToken(token);
                logger.info("Token validated for user: {}, roles: {}", username, roles);
                request.setAttribute("username", username);
                request.setAttribute("roles", roles);
                return true;
            }
        }
        logger.warn("Invalid token or no token present");
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Token");
        return false;
    }
}
