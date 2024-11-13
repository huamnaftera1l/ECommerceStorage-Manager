package com.jd.warehouse.config;

import com.jd.model.Permission;
import com.jd.model.Role;
import com.jd.model.User;
import com.jd.repository.PermissionRepository;
import com.jd.repository.RoleRepository;
import com.jd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 创建权限
        Permission createProduct = createPermissionIfNotFound("CREATE_PRODUCT");
        Permission readProduct = createPermissionIfNotFound("READ_PRODUCT");
        Permission updateProduct = createPermissionIfNotFound("UPDATE_PRODUCT");
        Permission deleteProduct = createPermissionIfNotFound("DELETE_PRODUCT");
        Permission manageUsers = createPermissionIfNotFound("MANAGE_USERS");

        // 创建角色
        Set<Permission> adminPermissions = new HashSet<>(Arrays.asList(createProduct, readProduct, updateProduct, deleteProduct, manageUsers));
        Role adminRole = createRoleIfNotFound("ADMIN", adminPermissions);

        Set<Permission> stockerPermissions = new HashSet<>(Arrays.asList(createProduct, readProduct, updateProduct));
        createRoleIfNotFound("STOCKER", stockerPermissions);

        Set<Permission> unstockerPermissions = new HashSet<>(Arrays.asList(readProduct, deleteProduct));
        createRoleIfNotFound("UNSTOCKER", unstockerPermissions);

        // 创建管理员用户
        createAdminUserIfNotFound(adminRole);

        //创建ROLE_USER
        Set<Permission> userPermissions = new HashSet<>(Arrays.asList(readProduct));
        createRoleIfNotFound("USER", userPermissions);
    }

    private Permission createPermissionIfNotFound(String name) {
        return permissionRepository.findByName(name)
                .orElseGet(() -> permissionRepository.save(new Permission(name)));
    }

    private Role createRoleIfNotFound(String name, Set<Permission> permissions) {
        return roleRepository.findByName(name)
                .orElseGet(() -> {
                    Role role = new Role(name);
                    role.setPermissions(permissions);
                    return roleRepository.save(role);
                });
    }

    private void createAdminUserIfNotFound(Role adminRole) {
        if (!userService.existsByUsername("admin")) {
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword(passwordEncoder.encode("adminPassword"));
            adminUser.setEmail("admin@example.com");
            adminUser.setRoles(Collections.singleton(adminRole));
            userService.saveUser(adminUser);
        }
    }
}