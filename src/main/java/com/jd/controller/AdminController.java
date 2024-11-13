package com.jd.controller;

import com.jd.aop.LoggingAspect;
import com.jd.dto.RoleDTO;
import com.jd.model.Role;
import com.jd.model.User;
import com.jd.service.RoleService;
import com.jd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController


@RequestMapping("/api/admin")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;

    @PostMapping("/assign-role")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> assignRoleToUser(@RequestParam String username, @RequestParam String roleName) {
        logger.info("用户角色分配中");
        userService.assignRoleToUser(username, roleName);
        return ResponseEntity.ok("Role assigned successfully");
    }

    @GetMapping("/user-roles")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUserRoles(@RequestParam String username) {
        logger.info("用户角色查询中");
        Set<Role> roles = roleService.getUserRoles(username);
        Set<RoleDTO> roleDTOs = roles.stream().map(RoleDTO::new).collect(Collectors.toSet());
        return ResponseEntity.ok(roleDTOs);
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin-function")
    public ResponseEntity<String> adminFunction() {
        logger.info("Accessing admin function");
        return ResponseEntity.ok("您现在是管理员\nHelloWorld!");
    }

}


