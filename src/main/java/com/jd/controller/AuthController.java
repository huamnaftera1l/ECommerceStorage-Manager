package com.jd.controller;

import com.jd.dto.LoginRequest;
import com.jd.model.JwtResponse;
import com.jd.service.JwtUtil;
import com.jd.service.UserService;
import com.jd.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        logger.info("Received registration request for user: {}", registerRequest.getUsername());
        try {
            userService.registerUser(registerRequest);
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            logger.error("Error during user registration", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            logger.info("User {} logged in with roles: {}", userDetails.getUsername(), roles);

            String jwt = jwtUtil.generateToken(userDetails.getUsername(), roles);

            return ResponseEntity.ok(new JwtResponse(jwt, "Bearer", userDetails.getUsername()));
        } catch (AuthenticationException e) {
            logger.error("Authentication failed for user: {}", loginRequest.getUsername(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
        }
    }

    @GetMapping("/some-endpoint")
    public ResponseEntity<?> someEndpoint(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        logger.info("Auth header in controller: {}", authHeader);

        List<String> roles = (List<String>) request.getAttribute("roles");
        String username = (String) request.getAttribute("username");
        logger.info("Username from request attribute: {}", username);
        logger.info("Roles from request attribute: {}", roles);

        if (roles != null && !roles.isEmpty()) {
            logger.info("Access granted for user with roles: {}", roles);
            return ResponseEntity.ok("Access granted");
        } else {
            logger.warn("Access denied. No roles found or roles is null");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
    }
}